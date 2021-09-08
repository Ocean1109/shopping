package com.example.demo.service;

import com.example.demo.ao.OrderAo;
import com.example.demo.ao.PayOrderAo;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.OrderList4ShopkeeperVo;
import com.example.demo.vo.OrderListVo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {
    /**
     * @param orderAo
     * @return
     */
    /**当一个人买商品时，产生订单,需要每个人产生订单的间隔大于一秒*/
    @Async
    public BaseVo generateOrder(OrderAo orderAo);

    /**
     * @param payOrderAo
     * @return
     */
    /**支付订单*/
    @Async
    public BaseVo payOrder(PayOrderAo payOrderAo);

    /**
     * @param id
     * @return
     */
    /**发货*/
    @Async
    public BaseVo sendingProduct(int id);

    /**
     * @param id
     * @return
     */
    /**取消订单*/
    @Async
    public BaseVo cancelOrder(int id);

    /**
     * @param id
     * @return
     */
    /**确认收货*/
    @Async
    public BaseVo completeOrder(int id);

    /**
     * @param id
     * @return
     */
    /**商家查找所有购买自己商品的订单*/
    @Async
    public OrderList4ShopkeeperVo getOrder4Shopkeeper(int id);

    /**
     * @param userId
     * @return
     */
    /**查找某用户所有订单，仅包括未取消和为完成的订单*/
    @Async
    public OrderListVo getOrder(int userId);
}
