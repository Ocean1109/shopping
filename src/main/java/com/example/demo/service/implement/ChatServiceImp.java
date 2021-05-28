package com.example.demo.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Chat;
import com.example.demo.entity.ChatDetail;
import com.example.demo.mapper.ChatDetailMapper;
import com.example.demo.mapper.ChatMapper;
import com.example.demo.service.ChatService;
import com.example.demo.vo.ChatDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImp implements ChatService {
    @Autowired
    ChatMapper chatMapper;
    @Autowired
    ChatDetailMapper chatDetailMapper;

    @Override
    public List<Chat> showAllChat(int userId){
        List<Chat> result=new ArrayList<>();
        QueryWrapper<Chat> chatQueryWrapper=new QueryWrapper<>();
        chatQueryWrapper.eq("user_id",userId);
        result=chatMapper.selectList(chatQueryWrapper);
        return result;
    }
    @Override
    public List<ChatDetailVo> showDetail(int chatId){
        List<ChatDetailVo> result=new ArrayList<>();
        QueryWrapper<ChatDetail> chatDetailQueryWrapper=new QueryWrapper<>();
        chatDetailQueryWrapper.eq("chat_id",chatId);
        List<ChatDetail> chatDetails=chatDetailMapper.selectList(chatDetailQueryWrapper);
        for(ChatDetail chatDetail:chatDetails){
            result.add(new ChatDetailVo(chatDetail.getUserId(),chatDetail.getUserName(),chatDetail.getContent(),chatDetail.getCreateTime()));
        }
        return result;
    }
}
