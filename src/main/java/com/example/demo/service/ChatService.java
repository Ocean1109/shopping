package com.example.demo.service;

import com.example.demo.entity.Chat;
import com.example.demo.vo.ChatDetailVo;

import java.util.List;

public interface ChatService {
    List<Chat> showAllChat(int userId);
    List<ChatDetailVo> showDetail(int chatId);
}
