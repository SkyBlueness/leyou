package com.leyou.item.service;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;

public interface SpuService {
    PageResult<SpuBo> getSpuPage(String key, Integer page, Integer row, Boolean saleable);

    SpuDetail getDetailBySpuId(Long spuId);

    ResponseEntity<Spu> getSpuById(Long spuId);
}
