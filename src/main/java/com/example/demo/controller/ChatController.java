package com.example.demo.controller;

import com.example.demo.entity.Chat;
import com.example.demo.service.ChatService;
import com.example.demo.service.TokenService;
import com.example.demo.vo.ChatDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ChatController {
    @Autowired
    ChatService chatService;
    @Autowired
    TokenService tokenService;

    @GetMapping("/chat")
    @ResponseBody
    public List<Chat> showAllChat(HttpServletRequest request){
        String token=request.getHeader(tokenService.getHeader());
        int userId=Integer.parseInt(tokenService.getUseridFromToken(token));
        return chatService.showAllChat(userId);
    }

    @GetMapping("/chat/{chatId}")
    @ResponseBody
    public List<ChatDetailVo> showDetail(@PathVariable("chatId")int chatId){

        return chatService.showDetail(chatId);
    }
}
