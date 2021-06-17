package com.example.demo.controller;


import com.example.demo.entity.Chat;
import com.example.demo.service.ChatService;
import com.example.demo.service.TokenService;
import com.example.demo.vo.ChatDetailVo;
import com.example.demo.vo.ChatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ChatController {
    @Autowired
    ChatService chatService;
    @Autowired
    TokenService tokenService;

    /**
     * @param request
     * @return
     */
    /**列出所有聊天列表*/
    @GetMapping("/chat")
    @ResponseBody
    public List<ChatVo> showAllChat(HttpServletRequest request){
        String token=request.getHeader(tokenService.getHeader());
        int userId=Integer.parseInt(tokenService.getUseridFromToken(token));
        return chatService.showAllChat(userId);
    }

    /**
     *
     * @param businessId
     * @param token
     * @param productId
     * @return
     */
    @PostMapping("/newChat")
    @ResponseBody
    public int newChat(@RequestParam("businessId")int businessId,@RequestParam("token")String token,@RequestParam("productId")int productId){
        int userId=Integer.parseInt(tokenService.getUseridFromToken(token));
        return chatService.newChat(userId,businessId,productId);
    }

    /**
     * @param chatId
     * @return
     */
    /**列出单个具体的聊天框*/
    @GetMapping("/chat/{chatId}")
    @ResponseBody
    public List<ChatDetailVo> showDetail(@PathVariable("chatId")int chatId){
        return chatService.showDetail(chatId);
    }

    /**
     * @return
     */
    @RequestMapping("/testChat")
    public String index(){
        return "index";
    }
}
