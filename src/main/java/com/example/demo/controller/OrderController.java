package com.example.demo.controller;

import com.example.demo.ao.OrderAo;
import com.example.demo.ao.PayOrderAo;
import com.example.demo.service.OrderService;
import com.example.demo.service.TokenService;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.OrderListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理订单逻辑
 */
@Controller
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    TokenService tokenService;

    /**
     * @param orderAo
     * @return
     */
    /**当一个人买商品时，产生订单,需要每个人产生订单的间隔大于一秒*/
    @PostMapping("/generateOrder")
    @ResponseBody
    public BaseVo generateOrder(@RequestBody OrderAo orderAo){
        return orderService.generateOrder(orderAo);
    }

    /**
     * @param payOrderAo
     * @return
     */
    /**支付订单*/
    @PostMapping("/payOrder")
    @ResponseBody
    public BaseVo payOrder(@RequestBody PayOrderAo payOrderAo){
        return orderService.payOrder(payOrderAo);
    }

    /**
     * @param id
     * @return
     */
    /**发货*/
    @PostMapping("/sendingProduct")
    @ResponseBody
    public BaseVo sendingProduct(@RequestBody int id){
        return orderService.sendingProduct(id);
    }

    /**
     * @param id
     * @return
     */
    /**取消订单*/
    @PostMapping("/cancelOrder")
    @ResponseBody
    public BaseVo cancelOrder(@RequestBody int id){
        return orderService.cancelOrder(id);
    }

    /**
     * @param id
     * @return
     */
    /**确认收货*/
    @PostMapping("/completeOrder")
    @ResponseBody
    public BaseVo completeOrder(@RequestBody int id){
        return orderService.completeOrder(id);
    }

    /**
     * @param userId
     * @return
     */
    /**查找某用户所有订单，仅包括未取消和为完成的订单*/
    @PostMapping("/getOrders")
    @ResponseBody
    public OrderListVo getOrder(@RequestParam("token") String token){
        int userId=Integer.parseInt(tokenService.getUseridFromToken(token));
        return orderService.getOrder(userId);
    }

}
