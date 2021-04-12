package com.example.demo.interceptor;
import com.example.demo.annotation.PassToken;
import com.example.demo.annotation.UserLoginToken;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.TokenService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

        System.out.println(request.getHeader("token"));
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        /** 检查有没有需要用户权限的注解 */
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            /** Token 验证 */
            String token = request.getHeader(tokenService.getHeader());
            if (StringUtils.isEmpty(token)) {
                token = request.getParameter(tokenService.getHeader());
            }
            if (StringUtils.isEmpty(token)) {
                response.sendError(401, "token信息不能为空");
                return false;
            }
            String userid = tokenService.getUseridFromToken(token);
            String compareToken = tokenService.getTokenMap().get(userid);
            if (compareToken != null && !compareToken.equals(token)) {
                response.sendError(400, "token已经失效,请重新登录");
                return false;
            }

            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                Claims claims = null;
                try {
                    claims = tokenService.getTokenClaim(token);
                    if (claims == null || tokenService.isTokenExpired(claims.getExpiration())) {
                        response.sendError(400, "token已经失效,请重新登录");
                        return false;
                    }
                } catch (Exception e) {
                    response.sendError(400, "token已经失效,请重新登录");
                    return false;
                }
                /** 设置 identityId 用户身份ID */
                request.setAttribute("identityId", claims.getSubject());
                return true;
            }
            if (compareToken == null) {
                // 由于服务器war重新上传导致临时数据丢失,需要重新存储
                tokenService.getTokenMap().put(userid, token);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
