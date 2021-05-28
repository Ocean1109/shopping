package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.ao.LikeAo;
import com.example.demo.ao.UserInfoAo;
import com.example.demo.entity.Praise;
import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.PraiseMapper;
import com.example.demo.mapper.ShoppingUserMapper;
import com.example.demo.vo.BaseVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PraiseController {

    @Autowired
    private PraiseMapper praiseMapper;

    @Autowired
    private ShoppingUserMapper shoppingUserMapper;

    @PostMapping("/Like")
    @ResponseBody
    public BaseVo Like(@RequestBody LikeAo likeAo){
        BaseVo result = new BaseVo();

        QueryWrapper<Praise> praiseQueryWrapper = Wrappers.query();
        praiseQueryWrapper.eq("user_id", likeAo.getUserId()).eq("product_id", likeAo.getProductId());
        Praise queryPraise = praiseMapper.selectOne(praiseQueryWrapper);
        QueryWrapper<ShoppingUser> shoppingUserQueryWrapper = Wrappers.query();
        shoppingUserQueryWrapper.eq("id", likeAo.getUserId());
        ShoppingUser queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date());
        Timestamp time = Timestamp.valueOf(current);

        if(queryPraise == null){
            if(likeAo.isLike()){
                Praise newPraise = new Praise(
                        likeAo.getProductId(), likeAo.getUserId(), queryUser.getUserName(), 0, time
                );
                praiseMapper.insert(newPraise);
                result.setCode(0);
                result.setMessage("点赞成功");
            }
            else {
                Praise newPraise = new Praise(
                        likeAo.getProductId(), likeAo.getUserId(), queryUser.getUserName(), 1, time
                );
                praiseMapper.insert(newPraise);
                result.setCode(1);
                result.setMessage("取消点赞成功");
            }
        }
        else {
            if(likeAo.isLike()){
                Praise newPraise = new Praise(
                    queryPraise.getId(), queryPraise.getProductId(), queryPraise.getUserId(), queryPraise.getUserName(), 0, time
                );
                praiseMapper.update(newPraise, praiseQueryWrapper);
                result.setCode(0);
                result.setMessage("点赞成功");
            }
            else {
                Praise newPraise = new Praise(
                        queryPraise.getId(), queryPraise.getProductId(), queryPraise.getUserId(), queryPraise.getUserName(), 1, time
                );
                praiseMapper.update(newPraise, praiseQueryWrapper);
                result.setCode(1);
                result.setMessage("取消点赞成功");
            }
        }

        return result;
    }

}
