package com.leyou.cart.service.impl;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginIntereptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFIX = "ly:cart:uid";

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Override
    public void addCart(Cart cart) {
        //获取用户信息
        UserInfo user = LoginIntereptor.getLoginUser();
        //redis的key
        String key = KEY_PREFIX + user.getId();
        //获取redis的操作对象
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean hasKey = operations.hasKey(skuId.toString());
        //判断该sku的购物车是否存在
        if (hasKey){
            //存在，获取购物车
            String value = operations.get(skuId.toString()).toString();
            cart  = JsonUtils.parse(value, Cart.class);
            cart.setNum(cart.getNum()+num);
        }else {
            //不存在,查询该sku的信息
            Sku sku = goodsClient.getSkuBySkuId(skuId);
            cart.setImage(StringUtils.isBlank(sku.getImages())? "" : sku.getImages().split(",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
        //将购物车存入redis中
        operations.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    @Override
    public List<Cart> getCarts() {
        UserInfo user = LoginIntereptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        if (!redisTemplate.hasKey(key)){
            //不存在直接返回null
            return null;
        }else {
            BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
            List<Object> values = operations.values();
            List<Cart>carts = values.stream().map(value -> JsonUtils.parse(value.toString(),
                    Cart.class)).collect(Collectors.toList());
            return carts;
        }
    }

    @Override
    public void updateNum(Long skuId,Integer num) {
        UserInfo user = LoginIntereptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        Boolean hasKey = redisTemplate.hasKey(key);
        if (!hasKey){
            return;
        }else {
            BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
            //获取购物车
            String s= operations.get(skuId.toString()).toString();
            Cart cart = JsonUtils.parse(s, Cart.class);
            //重写num
            cart.setNum(num);
            //存入Redis
            operations.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
        }
    }

    @Override
    public void deleteCart(Long skuId) {
        UserInfo user = LoginIntereptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        operations.delete(skuId.toString());
    }
}
