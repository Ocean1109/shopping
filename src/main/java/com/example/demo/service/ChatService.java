package com.example.demo.service;

import com.example.demo.entity.Chat;
import com.example.demo.entity.ChatDetail;
import com.example.demo.vo.ChatDetailVo;

import java.util.List;

public interface ChatService {
    /**
     * @param userId
     * @return
     */
    List<Chat> showAllChat(int userId);

    /**
     * @param chatId
     * @return
     */
    List<ChatDetailVo> showDetail(int chatId);

    /**
     * @param chatId
     * @param userId
     * @param content
     */
    void insertChat(int chatId,int userId,String content);
}
