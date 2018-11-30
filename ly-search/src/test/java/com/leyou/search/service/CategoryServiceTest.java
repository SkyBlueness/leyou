package com.leyou.search.service;

import com.leyou.LySearchService;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.SpuBo;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SpringBootTest(classes = LySearchService.class)
@RunWith(SpringRunner.class)
public class CategoryServiceTest {

    @Autowired
    private CategoryClient categoryService;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void testQueryCategory(){
        List<Category> categorys = categoryService.findCategoryByIds(Arrays.asList(1L, 2L, 3L));
        for (Category category:categorys){
            System.out.println(category);
        }
    }

    @Test
    public void createIndex(){
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void loadData(){
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            // 查询分页数据
            List<SpuBo> spus = this.goodsClient.getSpuPage(null,page,rows,true).getItems();
            size = spus.size();
            // 创建Goods集合
            List<Goods> goodsList = new ArrayList<>();
            // 遍历spu
            for (SpuBo spu : spus) {
                try {
                    Goods goods = this.searchService.buildGoods(spu);
                    goodsList.add(goods);
                } catch (Exception e) {
                    break;
                }
            }
            this.goodsRepository.saveAll(goodsList);
            page++;
        } while (size == 100);
    }
}