package com.leyou.search.pojo;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;

import java.util.List;
import java.util.Map;

public class SearchResult<T> extends PageResult<Goods> {

    private List<Category> categories;

    private List<Brand> brands;

    private List<Map<String,String>> specs; // 规格参数过滤条件

    public SearchResult(Long total, Integer totalPage, List<Goods> items,
                        List<Category> categories, List<Brand> brands,
                        List<Map<String,String>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public void SearchResult(){
    }

    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Category> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, String>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, String>> specs) {
        this.specs = specs;
    }
}