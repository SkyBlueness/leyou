package com.leyou.search.listener;

import com.leyou.item.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import com.rabbitmq.http.client.domain.ExchangeType;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoodsListener {

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository repository;

    /**
     * 消息消费者方法
     * 当添加或修改spu时更新索引库
     * @param id
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.create.index.queue",durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.update","item.insert"}))
    public void listenCreate(Long id) throws IOException {
        if(id == null){
            return;
        }
        Spu spu = goodsClient.getSpuById(id);
        Goods goods = searchService.buildGoods(spu);
        repository.save(goods);
    }

    /***
     * 消息消费者
     * 当删除spu时调用，删除对应的索引库
     * @param id
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.delete.index.queue",durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange",ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.delete"}))
    public void listenDelete(Long id) throws IOException {
        if(id == null){
            return;
        }
        repository.deleteById(id);
    }

}
