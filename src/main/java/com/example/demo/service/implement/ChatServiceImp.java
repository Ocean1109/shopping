package com.example.demo.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Chat;
import com.example.demo.mapper.ChatMapper;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImp implements ChatService {
    @Autowired
    ChatMapper chatMapper;

    @Override
    public List<Chat> showAllChat(int userId){
        List<Chat> result=new ArrayList<>();
        QueryWrapper<Chat> chatQueryWrapper=new QueryWrapper<>();
        chatQueryWrapper.eq("user_id",userId);
        result=chatMapper.selectList(chatQueryWrapper);
        return result;
    }
}
