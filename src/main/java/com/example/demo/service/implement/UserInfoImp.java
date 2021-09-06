package com.example.demo.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.ao.ChangeMailAo;
import com.example.demo.ao.ChangePasswordAo;
import com.example.demo.ao.UserInfoAo;
import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.ShoppingUserMapper;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.PatternMatchUtil;
import com.example.demo.util.SendMailUtil;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.UserInfoVo;
import org.apache.ibatis.ognl.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class UserInfoImp implements UserInfoService {

    @Autowired
    private ShoppingUserMapper shoppingUserMapper;

    @Autowired
    private TokenService tokenService;

    private final ReentrantLock lock = new ReentrantLock();

    /**
     * @param token
     * @return
     */
    /**
     * 展示用户信息
     */
    @Override
    @Async
    public UserInfoVo ShowUserInfo(String token) {
        UserInfoVo userInfoVo = new UserInfoVo();

        lock.lock();
        try {

        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper = Wrappers.query();
        int id = Integer.parseInt(tokenService.getUseridFromToken(token));
        shoppingUserQueryWrapper.eq("id", Integer.parseInt(tokenService.getUseridFromToken(token)));
        ShoppingUser queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

            userInfoVo.setUserName(queryUser.getUserName());
            userInfoVo.setAddress(queryUser.getAddress());
            userInfoVo.setAge(queryUser.getAge());
            userInfoVo.setGender(queryUser.getGender());
            userInfoVo.setMail(queryUser.getMail());
            userInfoVo.setTel(queryUser.getTel());
        } finally {
            lock.unlock();
        }

        return userInfoVo;
    }

    /**
     * @param userInfoAo
     * @return
     */
    /**
     * 更新用户信息
     */
    @Override
    @Async
    public BaseVo UpdateUserInfo(UserInfoAo userInfoAo) {
        BaseVo result = new BaseVo();

        lock.lock();
        try {
            QueryWrapper<ShoppingUser> shoppingUserQueryWrapper = Wrappers.query();
            shoppingUserQueryWrapper.eq("id", Integer.parseInt(tokenService.getUseridFromToken(userInfoAo.getToken())));
            ShoppingUser queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

            ShoppingUser newUserInfo = new ShoppingUser(
                    queryUser.getId(),
                    (userInfoAo.getTel() != null) ? userInfoAo.getTel() : queryUser.getTel(),
                    queryUser.getPassword(),
                    (userInfoAo.getUserName() != null) ? userInfoAo.getUserName() : queryUser.getUserName(),
                    queryUser.getToken(),
                    (userInfoAo.getAddress() != null) ? userInfoAo.getAddress() : queryUser.getAddress(),
                    (userInfoAo.getAge() != null) ? Integer.parseInt(userInfoAo.getAge()) : queryUser.getAge(),
                    (userInfoAo.getGender() != null) ? Integer.parseInt(userInfoAo.getGender()) : queryUser.getGender(),
                    queryUser.getMail(),
                    queryUser.getCode());

            shoppingUserMapper.update(newUserInfo, shoppingUserQueryWrapper);

            result.setCode(0);
            result.setMessage("修改信息成功");
        }
        finally {
            lock.unlock();
        }

        return result;
    }

    /**
     * @param changeMailAo
     * @return
     */
    /**
     * 更新邮箱
     */
    @Override
    @Async
    public BaseVo ChangeMail(ChangeMailAo changeMailAo) {
        BaseVo result = new BaseVo();

        lock.lock();
        try {
            QueryWrapper<ShoppingUser> shoppingUserQueryWrapper = Wrappers.query();
            shoppingUserQueryWrapper.eq("id", Integer.parseInt(tokenService.getUseridFromToken(changeMailAo.getToken())));
            ShoppingUser queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

            String password = queryUser.getPassword();

            if (!PatternMatchUtil.isMatchingMail(changeMailAo.getMail())) {
                result.setCode(1);
                result.setMessage("邮箱格式不正确");
            } else if (!password.equals(changeMailAo.getPassword())) {
                result.setCode(1);
                result.setMessage("密码不正确");
            } else {
                ShoppingUser newUserInfo = new ShoppingUser(
                        queryUser.getId(),
                        queryUser.getTel(),
                        queryUser.getPassword(),
                        queryUser.getUserName(),
                        queryUser.getToken(),
                        queryUser.getAddress(),
                        queryUser.getAge(),
                        queryUser.getGender(),
                        changeMailAo.getMail(),
                        queryUser.getCode());
                shoppingUserMapper.update(newUserInfo, shoppingUserQueryWrapper);

                result.setCode(0);
                result.setMessage("邮箱更改成功");
            }
        }
        finally {
            lock.unlock();
        }

        return result;
    }

    /**
     * @param id
     * @return
     */
    /**
     * 发送验证码
     */
    @Override
    @Async
    public BaseVo SendCode(int id) {
        BaseVo result = new BaseVo();

        lock.lock();
        try {

        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper = Wrappers.query();
        shoppingUserQueryWrapper.eq("id", id);
        ShoppingUser queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

            String code = SendMailUtil.sendCode(queryUser.getMail());

            DelCodeSubThread delCodeSubThread = new DelCodeSubThread(id, shoppingUserMapper);
            delCodeSubThread.start();

            if (code == "SendingException") {
                result.setCode(1);
                result.setMessage("发送失败");
            } else {
                ShoppingUser newUserInfo = new ShoppingUser(
                        queryUser.getId(),
                        queryUser.getTel(),
                        queryUser.getPassword(),
                        queryUser.getUserName(),
                        queryUser.getToken(),
                        queryUser.getAddress(),
                        queryUser.getAge(),
                        queryUser.getGender(),
                        queryUser.getMail(),
                        code);
                shoppingUserMapper.update(newUserInfo, shoppingUserQueryWrapper);
                result.setCode(0);
                result.setMessage("发送成功");
            }
        } finally {
            lock.unlock();
        }

        return result;
    }

    /**
     * @param changePasswordAo
     * @return
     */
    /**
     * 更改密码
     */
    @Override
    @Async
    public BaseVo ChangePassword(ChangePasswordAo changePasswordAo) {
        BaseVo result = new BaseVo();

        lock.lock();
        try {
            QueryWrapper<ShoppingUser> shoppingUserQueryWrapper = Wrappers.query();
            shoppingUserQueryWrapper.eq("id", Integer.parseInt(tokenService.getUseridFromToken(changePasswordAo.getToken())));
            ShoppingUser queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

            String code = queryUser.getCode();

            if (code == "SendingException") {
                result.setCode(1);
                result.setMessage("发送失败");
            } else if (!changePasswordAo.getCode().equals(code)) {
                result.setCode(1);
                result.setMessage("验证码错误");
            } else {
                ShoppingUser newUserInfo = new ShoppingUser(
                        queryUser.getId(),
                        queryUser.getTel(),
                        changePasswordAo.getPassword(),
                        queryUser.getUserName(),
                        queryUser.getToken(),
                        queryUser.getAddress(),
                        queryUser.getAge(),
                        queryUser.getGender(),
                        queryUser.getMail(),
                        "");
                shoppingUserMapper.update(newUserInfo, shoppingUserQueryWrapper);

                code = null;
                result.setCode(0);
                result.setMessage("修改成功");
            }
        }
        finally {
            lock.unlock();
        }

        return result;
    }

}


class DelCodeSubThread extends Thread {

    private ShoppingUserMapper shoppingUserMapper;

    private int id;

    /**
     * @param id
     * @param shoppingUserMapper
     */
    DelCodeSubThread(int id, ShoppingUserMapper shoppingUserMapper) {
        this.id = id;
        this.shoppingUserMapper = shoppingUserMapper;
    }

    /**
     *
     */
    /**
     * 当用户5分钟未输入验证码，使该验证码失效
     */
    @Override
    public void run() {
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper = Wrappers.query();
        shoppingUserQueryWrapper.eq("id", id);
        ShoppingUser queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

        ShoppingUser newUserInfo = new ShoppingUser(
                queryUser.getId(),
                queryUser.getTel(),
                queryUser.getPassword(),
                queryUser.getUserName(),
                queryUser.getToken(),
                queryUser.getAddress(),
                queryUser.getAge(),
                queryUser.getGender(),
                queryUser.getMail(),
                "");
        shoppingUserMapper.update(newUserInfo, shoppingUserQueryWrapper);
    }
}
