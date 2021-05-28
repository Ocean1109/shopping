package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDetail {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private int chatId;
    private int userId;
    private String userName;
    private String content;
    private Timestamp createTime;

    public ChatDetail(int chatId, int userId, String userName, String content, Timestamp createTime) {
        this.chatId = chatId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.createTime = createTime;
    }
}
