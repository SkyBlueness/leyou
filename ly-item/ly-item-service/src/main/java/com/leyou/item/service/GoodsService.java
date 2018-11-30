package com.leyou.item.service;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuBo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GoodsService {
    void saveGoods(SpuBo spuBo);

    List<Sku> getSkuBySpuId(Long spuId);

    void updateSpu(SpuBo spuBo);

    void deleteSpuBySpuId(Long spuId);

    void soldOut(Long id);

    void putAway(Long id);

    Sku getSkuBySkuId(Long skuId);
}
