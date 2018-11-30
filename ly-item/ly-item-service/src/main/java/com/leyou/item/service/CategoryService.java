package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findCategoryByParentId(Long pid);

    List<Category> findCategoryByBid(Integer pid);

    List<Category> queryNameByIds(List<Long> asList);
}
