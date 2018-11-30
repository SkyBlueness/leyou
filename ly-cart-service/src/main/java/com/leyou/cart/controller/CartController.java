package com.leyou.cart.controller;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     * @param cart
     * @return
     */
    @PostMapping()
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询购物车
     */
    @GetMapping()
    public ResponseEntity<List<Cart>> getCarts(){
        List<Cart>carts = cartService.getCarts();
        if (CollectionUtils.isEmpty(carts)){
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 修改购物车sku数量
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("skuId")Long skuId,@RequestParam("num")Integer num){
        cartService.updateNum(skuId,num);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除购物车
     * @param skuId
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId")Long skuId){
        cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }
}
