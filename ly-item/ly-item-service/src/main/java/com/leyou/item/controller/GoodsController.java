package com.leyou.item.controller;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 保存spu
     * @param spuBo
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void>saveGoods(@RequestBody SpuBo spuBo){
        goodsService.saveGoods(spuBo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("sku/{skuId}")
    public ResponseEntity<Sku>getSkuBySkuId(@PathVariable(value = "skuId")Long skuId){
        return ResponseEntity.ok(goodsService.getSkuBySkuId(skuId));
    }

    /**
     * 根据id查询sku
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>>getSkuBySpuId(@RequestParam("id")Long id){
        List<Sku>list = goodsService.getSkuBySpuId(id);
        return ResponseEntity.ok(list);
    }

    /**
     * 编辑商品
     * @param spuBo
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void>update(@RequestBody SpuBo spuBo){
        goodsService.updateSpu(spuBo);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
