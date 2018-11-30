package com.leyou.page.service.Impl;

import com.leyou.item.pojo.*;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecifcationClient;
import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecifcationClient specifcationClient;

    @Autowired
    private CategoryClient categoryClient;

    @Override
    public Map<String, Object> getSpuById(Long id) {
        try {
            Map<String,Object>map = new HashMap<>();
            //查询spu
            Spu spu = goodsClient.getSpuById(id);
            //获取sku集合
            List<Sku> skuList = spu.getSkus();
            //获取spu详情
            SpuDetail spuDetail = spu.getSpuDetail();
            //商品的分类
            List<Category>categories = categoryClient.findCategoryByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
            //查询品牌
            List<Brand> brands = goodsClient.getBrandsByIds(Arrays.asList(Long.valueOf(spu.getBrandId())));
            //查询组内参数
            List<SpecGroup> specGroups = specifcationClient.getGroupByCid(spu.getCid3());
            //查询规格参数
            List<SpecParam> params = specifcationClient.getParam(null, spu.getCid3(), null);
            //处理规格参数
            Map<Long,String>paramMap = new HashMap<>();
            params.forEach(param -> paramMap.put(param.getId(),param.getName()));
            map.put("spu", spu);
            map.put("spuDetail", spuDetail);
            map.put("skus", skuList);
            map.put("brand", brands.get(0));
            map.put("categories", categories);
            map.put("groups", specGroups);
            map.put("params", paramMap);
            return map;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
