package com.example.demo.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.ao.OrderAo;
import com.example.demo.ao.PayOrderAo;
import com.example.demo.entity.*;
import com.example.demo.mapper.OrderProductMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ShoppingOrderMapper;
import com.example.demo.mapper.ShoppingUserMapper;
import com.example.demo.service.OrderService;
import com.example.demo.service.TokenService;
import com.example.demo.vo.BaseVo;
import com.example.demo.vo.OrderList4ShopkeeperVo;
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
    private ShoppingUserMapper shoppingUserMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * @param orderAo
     * @return
     */
    /**当一个人买商品时，产生订单*/
    public BaseVo generateOrder(OrderAo orderAo){
        BaseVo result = new BaseVo();

        double orderAmount = 0d;

        int userId = Integer.parseInt(tokenService.getUseridFromToken(orderAo.getToken()));

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
                userId,
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
        shoppingOrderQueryWrapper.eq("id", newShoppingOrder.getId());
        ShoppingOrder queryOrder = shoppingOrderMapper.selectOne(shoppingOrderQueryWrapper);

        OrderProduct newOrder;
        QueryWrapper<Product> productQueryWrapper1;
        Product queryProducts;

        int refund = 0;

        for(int i = 0; i < orderAo.getProductIds().size(); i++){

            productQueryWrapper1 = Wrappers.query();
            productQueryWrapper1.eq("id", orderAo.getProductIds().get(i));
            queryProducts = productMapper.selectOne(productQueryWrapper1);

            if(queryProducts.getNumbers() < orderAo.getNum().get(i)){
                refund += orderAo.getNum().get(i) * queryProducts.getProductPrice();
                result.setMessage(queryProducts.getProductDesc() + "库存不足，无法购买！");
            }

            newOrder = new OrderProduct(queryOrder.getId(), orderAo.getProductIds().get(i), false, queryProducts.getPublishUserId(), false, orderAo.getNum().get(i));
            orderProductMapper.insert(newOrder);

            Product newProduct = new Product(
                    queryProducts.getId(),
                    queryProducts.getProductDesc(),
                    queryProducts.getProductImage(),
                    queryProducts.getProductPrice(),
                    queryProducts.getProductTypeId(),
                    queryProducts.getBrandId(),
                    queryProducts.getPublishUserId(),
                    queryProducts.getProductAddress(),
                    queryProducts.getCreateTime(),
                    queryProducts.getUpdateTime(),
                    queryProducts.getNumbers() - orderAo.getNum().get(i),
                    queryProducts.getProductRuleId(),
                    queryProducts.getProductRule()
            );

            productMapper.update(newProduct, productQueryWrapper1);
        }



        result.setCode(0);
        if(result.getMessage() == null){
            result.setMessage("购买成功");
        }

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
        shoppingOrderQueryWrapper.eq("id", payOrderAo.getId());
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
    /**发货*/
    public BaseVo sendingProduct(int id){
        BaseVo result = new BaseVo();

        QueryWrapper<OrderProduct> orderProductQueryWrapper = Wrappers.query();
        orderProductQueryWrapper.eq("id", id);
        OrderProduct queryOrderProduct = orderProductMapper.selectOne(orderProductQueryWrapper);

        OrderProduct orderProduct = new OrderProduct(
                queryOrderProduct.getId(),
                queryOrderProduct.getOrderId(),
                queryOrderProduct.getProductId(),
                true,
                queryOrderProduct.getShopkeeperId(),
                queryOrderProduct.isFinished(),
                queryOrderProduct.getNum()
        );

        orderProductMapper.update(orderProduct, orderProductQueryWrapper);

        QueryWrapper<ShoppingOrder> shoppingOrderQueryWrapper = Wrappers.query();
        shoppingOrderQueryWrapper.eq("id", queryOrderProduct.getOrderId());
        ShoppingOrder queryOrder = shoppingOrderMapper.selectOne(shoppingOrderQueryWrapper);

        orderProductQueryWrapper = Wrappers.query();
        orderProductQueryWrapper.eq("order_id", queryOrder.getId());
        List<OrderProduct> orderProductList = orderProductMapper.selectList(orderProductQueryWrapper);

        boolean allSent = true;
        for(int i = 0; i < orderProductList.size(); i++){
            if(!orderProductList.get(i).isSentProduct()){
                allSent = false;
            }
        }

        if(allSent){
            ShoppingOrder newOrder = new ShoppingOrder(
                    queryOrder.getId(),
                    queryOrder.getBuyingUserId(),
                    4,
                    queryOrder.getPayStatus(),
                    queryOrder.getOrderAmount(),
                    queryOrder.getPayAmount(),
                    queryOrder.getPayTime(),
                    queryOrder.getCompletionTime(),
                    queryOrder.getCreateTime()
            );


            shoppingOrderMapper.update(newOrder, shoppingOrderQueryWrapper);


            result.setCode(0);
            result.setMessage("已发货");
        }
        else {
            result.setCode(1);
            result.setMessage("未发货");
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

            QueryWrapper<OrderProduct> orderProductQueryWrapper = Wrappers.query();
            orderProductQueryWrapper.eq("order_id", queryOrder.getId());
            List<OrderProduct> orderProductList = orderProductMapper.selectList(orderProductQueryWrapper);

            OrderProduct newOrderProduct;
            QueryWrapper<OrderProduct> orderProductQueryWrapper1;

            for(int i = 0; i < orderProductList.size(); i++){
                newOrderProduct = new OrderProduct(
                        orderProductList.get(i).getOrderId(),
                        orderProductList.get(i).getProductId(),
                        orderProductList.get(i).isSentProduct(),
                        orderProductList.get(i).getShopkeeperId(),
                        true,
                        orderProductList.get(i).getNum()
                );

                orderProductQueryWrapper1 = Wrappers.query();
                orderProductQueryWrapper1.eq("id", orderProductList.get(i).getOrderId());
                orderProductMapper.update(newOrderProduct, orderProductQueryWrapper1);

            }

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
        else if(queryOrder.getTradeStatus() == 2){
            result.setCode(1);
            result.setMessage("订单已取消");
        }
        else if(queryOrder.getTradeStatus() == 3){
            result.setCode(1);
            result.setMessage("订单已结算");
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

            QueryWrapper<OrderProduct> orderProductQueryWrapper = Wrappers.query();
            orderProductQueryWrapper.eq("order_id", queryOrder.getId());
            List<OrderProduct> orderProductList = orderProductMapper.selectList(orderProductQueryWrapper);

            OrderProduct newOrderProduct;
            QueryWrapper<OrderProduct> orderProductQueryWrapper1;

            for(int i = 0; i < orderProductList.size(); i++){
                newOrderProduct = new OrderProduct(
                        orderProductList.get(i).getId(),
                        orderProductList.get(i).getOrderId(),
                        orderProductList.get(i).getProductId(),
                        orderProductList.get(i).isSentProduct(),
                        orderProductList.get(i).getShopkeeperId(),
                        true,
                        orderProductList.get(i).getNum()
                );

                orderProductQueryWrapper1 = Wrappers.query();
                orderProductQueryWrapper1.eq("id", orderProductList.get(i).getId());
                orderProductMapper.update(newOrderProduct, orderProductQueryWrapper1);

            }

            result.setCode(0);
            result.setMessage("确认收货");
        }

        return result;
    }

    /**
     * @param id
     * @return
     */
    /**商家查找所有购买自己商品的订单*/
    public OrderList4ShopkeeperVo getOrder4Shopkeeper(int id){
        OrderList4ShopkeeperVo result = new OrderList4ShopkeeperVo();

        QueryWrapper<OrderProduct> orderProductQueryWrapper = Wrappers.query();
        orderProductQueryWrapper.eq("shopkeeper_id", id);
        List<OrderProduct> queryOrderProduct = orderProductMapper.selectList(orderProductQueryWrapper);

        if(queryOrderProduct == null){
            result.setCode(1);
            result.setMessage("没有此订单");
        }
        else {

            List<Order4Shopkeeper> order4Shopkeepers = new ArrayList<>();

            QueryWrapper<Product> productQueryWrapper;
            Product queryProduct;
            QueryWrapper<ShoppingOrder> shoppingOrderQueryWrapper;
            ShoppingOrder queryOrder;
            QueryWrapper<ShoppingUser> shoppingUserQueryWrapper;
            ShoppingUser queryUser;

            for(int i = 0; i < queryOrderProduct.size(); i++){

                productQueryWrapper = Wrappers.query();
                productQueryWrapper.eq("id", queryOrderProduct.get(i).getProductId());
                queryProduct = productMapper.selectOne(productQueryWrapper);

                shoppingOrderQueryWrapper = Wrappers.query();
                shoppingOrderQueryWrapper.eq("id", queryOrderProduct.get(i).getOrderId());
                queryOrder = shoppingOrderMapper.selectOne(shoppingOrderQueryWrapper);

                shoppingUserQueryWrapper = Wrappers.query();
                shoppingUserQueryWrapper.eq("id", queryOrder.getBuyingUserId());
                queryUser = shoppingUserMapper.selectOne(shoppingUserQueryWrapper);

                if(queryProduct != null && queryOrder != null && queryUser != null){
                    Order4Shopkeeper newOrder = new Order4Shopkeeper(
                            queryOrder.getId(),
                            queryOrderProduct.get(i).getProductId(),
                            queryProduct.getProductImage(),
                            queryProduct.getProductDesc(),
                            queryProduct.getProductPrice(),
                            queryUser.getId(),
                            queryUser.getAddress(),
                            queryUser.getUserName(),
                            queryUser.getTel(),
                            queryOrder.getTradeStatus(),
                            queryOrderProduct.get(i).getId()
                    );

                    order4Shopkeepers.add(newOrder);
                }

            }

            result.setCode(0);
            result.setMessage("查询成功");
            result.setOrder4Shopkeepers(order4Shopkeepers);

        }

        return result;
    }

    /**
     * @param userId
     * @return
     */
    /**查找某用户所有订单，仅包括未取消的订单*/
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
                if(queryOrder.get(i).getTradeStatus() == 2){
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
            List<Integer> productNum;
            for(int i = 0; i < extendShoppingOrders.size(); i++){
                productList = new ArrayList<Product>();
                productNum = new ArrayList<Integer>();

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
                        productNum.add(queryOrderProduct.get(j).getNum());
                    }
                }

                extendShoppingOrders.get(i).setProductList(productList);
                extendShoppingOrders.get(i).setProductNum(productNum);

            }

            result.setCode(0);
            result.setMessage("查找成功");
            result.setExtendShoppingOrders(extendShoppingOrders);
        }

        return result;
    }

}
