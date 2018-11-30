package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> findCategoryByParentId(@RequestParam("pid")Long pid){
        return ResponseEntity.ok(categoryService.findCategoryByParentId(pid));
    }

    /**
     * 根据品牌id查询分类
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> findCategoryByBid(@PathVariable(value = "bid")Integer bid){
        return ResponseEntity.ok(categoryService.findCategoryByBid(bid));
    }

    /**
     *根据ids查询分类名称
     * @param ids
     * @return
     */
    @GetMapping("names")
    public ResponseEntity<List<Category>> findCategoryByIds(@RequestParam(value = "ids")List<Long>ids){
        return ResponseEntity.ok(categoryService.queryNameByIds(ids));
    }
}
