package com.example.demo.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.ao.OrderAo;
import com.example.demo.ao.PayOrderAo;
import com.example.demo.entity.ExtendShoppingOrder;
import com.example.demo.entity.OrderProduct;
import com.example.demo.entity.Product;
import com.example.demo.entity.ShoppingOrder;
import com.example.demo.mapper.OrderProductMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ShoppingOrderMapper;
import com.example.demo.service.OrderService;
import com.example.demo.service.TokenService;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.OrderListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private ShoppingOrderMapper shoppingOrderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * @param orderAo
     * @return
     */
    /**当一个人买商品时，产生订单,需要每个人产生订单的间隔大于一秒*/
    public BaseVo generateOrder(OrderAo orderAo){
        BaseVo result = new BaseVo();

        double orderAmount = 0d;

        QueryWrapper<Product> productQueryWrapper;

        for(int i = 0; i < orderAo.getProductIds().size(); i++){
            productQueryWrapper = Wrappers.query();
            productQueryWrapper.eq("id", orderAo.getProductIds().get(i));
            Product queryProduct = productMapper.selectOne(productQueryWrapper);

            orderAmount += queryProduct.getProductPrice();
        }

        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date());
        Timestamp time = Timestamp.valueOf(current);

        ShoppingOrder newShoppingOrder = new ShoppingOrder(
                Integer.parseInt(tokenService.getUseridFromToken(orderAo.getToken())),
                1,
                1,
                orderAmount,
                null,
                null,
                null,
                time
        );

        shoppingOrderMapper.insert(newShoppingOrder);

        QueryWrapper<ShoppingOrder> shoppingOrderQueryWrapper = Wrappers.query();
        shoppingOrderQueryWrapper.eq("buying_user_id", Integer.parseInt(tokenService.getUseridFromToken(orderAo.getToken()))).eq("create_time", time);
        ShoppingOrder queryOrder = shoppingOrderMapper.selectOne(shoppingOrderQueryWrapper);

        OrderProduct newOrder;


        for(int i = 0; i < orderAo.getProductIds().size(); i++){
            newOrder = new OrderProduct(queryOrder.getId(), orderAo.getProductIds().get(i));
            orderProductMapper.insert(newOrder);
        }

        result.setCode(0);
        result.setMessage("购买成功");

        return result;
    }

    /**
     * @param id
     * @return
     */
    /**支付订单*/
    public BaseVo payOrder(PayOrderAo payOrderAo){
        BaseVo result = new BaseVo();

        QueryWrapper<ShoppingOrder> shoppingOrderQueryWrapper = Wrappers.query();
        shoppingOrderQueryWrapper.eq("id", Integer.parseInt(tokenService.getUseridFromToken(payOrderAo.getToken())));
        ShoppingOrder queryOrder = shoppingOrderMapper.selectOne(shoppingOrderQueryWrapper);

        if(queryOrder == null){
            result.setCode(1);
            result.setMessage("没有此订单");
        }
        else if(queryOrder.getTradeStatus() == 2){
            result.setCode(1);
            result.setMessage("订单已取消");
        }
        else {
            String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Timestamp time = Timestamp.valueOf(current);

            ShoppingOrder newOrder = new ShoppingOrder(
                    queryOrder.getId(),
                    queryOrder.getBuyingUserId(),
                    queryOrder.getTradeStatus(),
                    3,
                    queryOrder.getOrderAmount(),
                    payOrderAo.getPayAmount(),
                    time,
                    queryOrder.getCompletionTime(),
                    queryOrder.getCreateTime()
            );

            shoppingOrderMapper.update(newOrder, shoppingOrderQueryWrapper);

            result.setCode(0);
            result.setMessage("已支付订单");
        }

        return result;
    }

    /**
     * @param id
     * @return
     */
    /**取消订单*/
    public BaseVo cancelOrder(int id){
        BaseVo result = new BaseVo();

        QueryWrapper<ShoppingOrder> shoppingOrderQueryWrapper = Wrappers.query();
        shoppingOrderQueryWrapper.eq("id", id);
        ShoppingOrder queryOrder = shoppingOrderMapper.selectOne(shoppingOrderQueryWrapper);

        if(queryOrder == null){
            result.setCode(1);
            result.setMessage("没有此订单");
        }
        else if(queryOrder.getTradeStatus() == 3){
            result.setCode(1);
            result.setMessage("订单已结束");
        }
        else {
            String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date());
            Timestamp time = Timestamp.valueOf(current);

            ShoppingOrder newOrder = new ShoppingOrder(
                    queryOrder.getId(),
                    queryOrder.getBuyingUserId(),
                    2,
                    queryOrder.getPayStatus(),
                    queryOrder.getOrderAmount(),
                    queryOrder.getPayAmount(),
                    queryOrder.getPayTime(),
                    time,
                    queryOrder.getCreateTime()
            );

            shoppingOrderMapper.update(newOrder, shoppingOrderQueryWrapper);

            result.setCode(0);
            result.setMessage("取消成功");
        }

        return result;
    }

    /**
     * @param id
     * @return
     */
    /**确认收货*/
    public BaseVo completeOrder(int id){
        BaseVo result = new BaseVo();

        QueryWrapper<ShoppingOrder> shoppingOrderQueryWrapper = Wrappers.query();
        shoppingOrderQueryWrapper.eq("id", id);
        ShoppingOrder queryOrder = shoppingOrderMapper.selectOne(shoppingOrderQueryWrapper);

        if(queryOrder == null){
            result.setCode(1);
            result.setMessage("没有此订单");
        }
        else {
            String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Timestamp time = Timestamp.valueOf(current);

            ShoppingOrder newOrder = new ShoppingOrder(
                    queryOrder.getId(),
                    queryOrder.getBuyingUserId(),
                    3,
                    queryOrder.getPayStatus(),
                    queryOrder.getOrderAmount(),
                    queryOrder.getPayAmount(),
                    queryOrder.getPayTime(),
                    time,
                    queryOrder.getCreateTime()
            );

            shoppingOrderMapper.update(newOrder, shoppingOrderQueryWrapper);

            result.setCode(0);
            result.setMessage("确认收货");
        }

        return result;
    }

    /**
     * @param userId
     * @return
     */
    /**查找某用户所有订单，仅包括未取消和为完成的订单*/
    public OrderListVo getOrder(int userId){
        OrderListVo result = new OrderListVo();

        QueryWrapper<ShoppingOrder> shoppingOrderQueryWrapper = Wrappers.query();
        shoppingOrderQueryWrapper.eq("buying_user_id", userId);
        List<ShoppingOrder> queryOrder = shoppingOrderMapper.selectList(shoppingOrderQueryWrapper);

        if(queryOrder == null){
            result.setCode(1);
            result.setMessage("没有此用户");
            result.setExtendShoppingOrders(null);
        }
        else {
            List<ExtendShoppingOrder> extendShoppingOrders = new ArrayList<ExtendShoppingOrder>();
            ExtendShoppingOrder extendShoppingOrder;
            for(int i = 0; i < queryOrder.size(); i++){
                if(queryOrder.get(i).getTradeStatus() == 2 || queryOrder.get(i).getTradeStatus() == 3){
                    queryOrder.remove(i);
                    i--;
                }
                else {
                    extendShoppingOrder = new ExtendShoppingOrder(
                            queryOrder.get(i).getId(),
                            queryOrder.get(i).getBuyingUserId(),
                            queryOrder.get(i).getTradeStatus(),
                            queryOrder.get(i).getPayStatus(),
                            queryOrder.get(i).getOrderAmount(),
                            queryOrder.get(i).getPayAmount(),
                            queryOrder.get(i).getPayTime(),
                            queryOrder.get(i).getCompletionTime(),
                            queryOrder.get(i).getCreateTime());
                    extendShoppingOrders.add(extendShoppingOrder);
                }
            }

            QueryWrapper<OrderProduct> orderProductQueryWrapper;
            QueryWrapper<Product> productQueryWrapper;
            Product queryProduct;
            List<Product> productList;
            for(int i = 0; i < extendShoppingOrders.size(); i++){
                productList = new ArrayList<Product>();

                orderProductQueryWrapper = Wrappers.query();
                orderProductQueryWrapper.eq("order_id", extendShoppingOrders.get(i).getId());
                List<OrderProduct> queryOrderProduct = orderProductMapper.selectList(orderProductQueryWrapper);

                for(int j = 0; j < queryOrderProduct.size(); j++){
                    productQueryWrapper = Wrappers.query();
                    productQueryWrapper.eq("id", queryOrderProduct.get(j).getProductId());
                    queryProduct = productMapper.selectOne(productQueryWrapper);

                    if(queryProduct == null){
                        continue;
                    }
                    else {
                        productList.add(queryProduct);
                    }
                }

                extendShoppingOrders.get(i).setProductList(productList);

            }

            result.setCode(0);
            result.setMessage("查找成功");
            result.setExtendShoppingOrders(extendShoppingOrders);
        }

        return result;
    }

}
