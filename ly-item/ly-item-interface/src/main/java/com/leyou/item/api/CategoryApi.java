package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {


    /**
     *根据ids查询分类名称
     * @param ids
     * @return
     */
    @GetMapping("category/names")
    List<Category> findCategoryByIds(@RequestParam(value = "ids")List<Long>ids);

    /**
     * 根据品牌id查询分类
     * @param bid
     * @return
     */
    @GetMapping("category/bid/{bid}")
    List<Category> findCategoryByBid(@PathVariable(value = "bid")Integer bid);
}
