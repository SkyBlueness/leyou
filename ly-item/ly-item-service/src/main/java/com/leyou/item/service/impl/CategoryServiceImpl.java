package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findCategoryByParentId(Long pid) {
        Category t = new Category();
        t.setParentId(pid);
        return categoryMapper.select(t);
    }

    @Override
    public List<Category> findCategoryByBid(Integer bid) {
        return categoryMapper.findCategoryByBid(bid);
    }

    @Override
    public List<Category> queryNameByIds(List<Long> asList) {
        return categoryMapper.selectByIdList(asList);
    }
}
