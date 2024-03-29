package com.example.demo.service.implement;

import com.aliyuncs.auth.SHA256withRSASigner;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Chat;
import com.example.demo.entity.ChatDetail;
import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.ChatDetailMapper;
import com.example.demo.mapper.ChatMapper;
import com.example.demo.mapper.ShoppingUserMapper;
import com.example.demo.service.ChatService;
import com.example.demo.vo.ChatDetailVo;
import com.example.demo.vo.ChatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChatServiceImp implements ChatService {
    @Autowired
    ChatMapper chatMapper;
    @Autowired
    ChatDetailMapper chatDetailMapper;
    @Autowired
    ShoppingUserMapper shoppingUserMapper;

    /**
     * @param userId
     * @return
     */
    @Override
    public List<ChatVo> showAllChat(int userId){
        List<ChatVo> result=new ArrayList<>();
        QueryWrapper<Chat> chatQueryWrapper=new QueryWrapper<>();
        chatQueryWrapper.eq("user_id",userId);
        List<Chat> userChats=chatMapper.selectList(chatQueryWrapper);
        for(Chat userChat:userChats){
            QueryWrapper<ShoppingUser> shoppingUserQueryWrapper=new QueryWrapper<>();
            shoppingUserQueryWrapper.eq("id",userChat.getAnotherUserId());
            ShoppingUser user=shoppingUserMapper.selectOne(shoppingUserQueryWrapper);
            result.add(new ChatVo(userChat.getId(),userChat.getUserId(),userChat.getAnotherUserId(),user.getUserName(),userChat.getProductId()));
        }
        QueryWrapper<Chat> anotherChatQueryWrapper=new QueryWrapper<>();
        anotherChatQueryWrapper.eq("another_user_id",userId);
        List<Chat> anotherUserChats=chatMapper.selectList(anotherChatQueryWrapper);
        for(Chat anotherUserChat:anotherUserChats){
            QueryWrapper<ShoppingUser> shoppingUserQueryWrapper=new QueryWrapper<>();
            shoppingUserQueryWrapper.eq("id",anotherUserChat.getUserId());
            ShoppingUser user=shoppingUserMapper.selectOne(shoppingUserQueryWrapper);
            result.add(new ChatVo(anotherUserChat.getId(),anotherUserChat.getAnotherUserId(),anotherUserChat.getUserId(),user.getUserName(),anotherUserChat.getProductId()));
        }
        return result;
    }

    /**
     * @param chatId
     * @return
     */
    @Override
    public List<ChatDetailVo> showDetail(int chatId){
        List<ChatDetailVo> result=new ArrayList<>();
        QueryWrapper<ChatDetail> chatDetailQueryWrapper=new QueryWrapper<>();
        chatDetailQueryWrapper.eq("chat_id",chatId);
        List<ChatDetail> chatDetails=chatDetailMapper.selectList(chatDetailQueryWrapper);
        for(ChatDetail chatDetail:chatDetails){
            ShoppingUser user=shoppingUserMapper.selectById(chatDetail.getUserId());
            result.add(new ChatDetailVo(user.getToken(),chatDetail.getUserName(),chatDetail.getContent()));
        }
        return result;
    }

    /**
     * @param chatId
     * @param userId
     * @param content
     */
    @Override
    public void insertChat(int chatId,int userId,String content){
        //获取当前时间
        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date());
        Timestamp time = Timestamp.valueOf(current);
        ShoppingUser shoppingUser=shoppingUserMapper.selectById(userId);
        ChatDetail chatDetail=new ChatDetail(chatId,userId,shoppingUser.getUserName(),content,time);
        chatDetailMapper.insert(chatDetail);
    }

    @Override
    public int newChat(int businessId,int userId,int productId){
        if(userId==businessId){
            return -1;
        }
        QueryWrapper<Chat> chatQueryWrapper=new QueryWrapper<>();
        chatQueryWrapper.eq("user_id",businessId).or(wrapper->wrapper.eq("another_user_id",businessId));
        Chat chat=chatMapper.selectOne(chatQueryWrapper);
        if(chat==null){
            Chat newAChat=new Chat(userId,businessId,productId);
            chatMapper.insert(newAChat);
            return newAChat.getId();
        }else{
            return chat.getId();
        }
    }
}
