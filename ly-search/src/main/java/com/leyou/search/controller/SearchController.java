package com.leyou.search.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private IndexService indexService;

    /**
     * 搜索商品方法
     * @param searchRequest
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<SearchResult<Goods>> getGoodsPage(@RequestBody SearchRequest searchRequest){
        SearchResult<Goods> list = indexService.getGoodsPage(searchRequest);
        if (list == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
}
