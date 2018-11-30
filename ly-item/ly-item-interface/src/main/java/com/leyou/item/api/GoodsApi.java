package com.leyou.item.api;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public interface GoodsApi {

    /**
     * 根据id查询sku
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> getSkuBySpuId(@RequestParam("id")Long id);

    /**
     * 分页查询spu
     * @param key
     * @param page
     * @param row
     * @param saleable
     * @return
     */
    @GetMapping("spu/page")
    PageResult<SpuBo> getSpuPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "pqge",defaultValue = "1")Integer page,
            @RequestParam(value = "row",defaultValue = "5")Integer row,
            @RequestParam(value="saleable",required = false)Boolean saleable
    );

    /**
     * 根据spuId查询商品详情
     * @param spuId
     * @return
     */
    @RequestMapping("spu/detail/{spuId}")
    SpuDetail getDetailBySpuId(@PathVariable("spuId")Long spuId);

    /**
     * 根据多个id查询品牌集合
     * @param ids
     * @return
     */
    @GetMapping("brand/list")
    List<Brand> getBrandsByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 根据spuid查询spu
     * @param spuId
     * @return
     */
    @GetMapping("spu/{spuId}")
    Spu getSpuById(@PathVariable(value = "spuId")Long spuId);

    /**
     * 根据skuId查询sku
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    Sku getSkuBySkuId(@PathVariable(value = "skuId")Long skuId);
}
