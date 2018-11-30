package com.leyou.item.service.impl;

import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import com.leyou.item.service.GoodsService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Transactional
    @Override
    public void saveGoods(SpuBo spuBo) {
        //保存spu
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuMapper.insert(spuBo);
        //保存sku
        List<Sku> skus = spuBo.getSkus();
        List<Stock> stocks = new ArrayList<>();
        for (Sku sku:skus){
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setEnable(false);
            skuMapper.insert(sku);
            //保存库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocks.add(stock);
        }
        //批量插入库存
        stockMapper.insertList(stocks);
        //保存detail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        spuDetailMapper.insert(spuDetail);
        sendMessage(spuBo.getId(),"insert");
    }

    @Override
    public List<Sku> getSkuBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        for (Sku sku1 : skuList){
            Stock stock = stockMapper.selectByPrimaryKey(sku1.getId());
            sku1.setStock(stock.getStock());
        }
        return skuList;
    }

    @Transactional
    public void updateSpu(SpuBo spu) {
        // 查询以前sku
        List<Sku> skus = this.getSkuBySpuId(spu.getId());
        // 如果以前存在，则删除
        if(!CollectionUtils.isEmpty(skus)) {
            List<Long> ids = skus.stream().map(s -> s.getId()).collect(Collectors.toList());
            // 删除以前库存
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId", ids);
            this.stockMapper.deleteByExample(example);

            // 删除以前的sku
            Sku record = new Sku();
            record.setSpuId(spu.getId());
            this.skuMapper.delete(record);

        }
        // 新增sku和库存
        saveSkuAndStock(spu.getSkus(), spu.getId());

        // 更新spu
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        spu.setValid(null);
        spu.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spu);

        // 更新spu详情
        this.spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        sendMessage(spu.getId(),"update");
    }

    private void saveSkuAndStock(List<Sku> skus, Long spuId) {
        for (Sku sku : skus) {
            if (!sku.getEnable()) {
                continue;
            }
            // 保存sku
            sku.setSpuId(spuId);
            // 初始化时间
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insert(sku);

            // 保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insert(stock);
        }
    }

    @Transactional
    @Override
    public void deleteSpuBySpuId(Long spuId) {
        //删除detail
        spuDetailMapper.deleteByPrimaryKey(spuId);
        //删除spu
        spuMapper.deleteByPrimaryKey(spuId);
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(sku);
        for(Sku s:skus){
            //删除sku
            skuMapper.deleteByPrimaryKey(s.getId());
            //删除stock
            stockMapper.deleteByPrimaryKey(s.getId());
        }
        sendMessage(spuId,"detele");
    }

    @Transactional
    @Override
    public void soldOut(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setSaleable(false);
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Transactional
    @Override
    public void putAway(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setSaleable(true);
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public Sku getSkuBySkuId(Long skuId) {
        return skuMapper.selectByPrimaryKey(skuId);
    }

    /**
     * 发送消息方法
     */
    public void sendMessage(Long spuId,String type){
        try {
            amqpTemplate.convertAndSend("item."+type,spuId);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
