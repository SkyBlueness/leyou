package com.leyou.item.service;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

public interface BrandService {
    PageResult<Brand> getBrandByPage(Integer page, String key, Integer row, String sortBy, Boolean desc);

    void saveBrand(Brand brand, List<Integer> csid);

    void updateBrand(Brand brand, List<Integer> cids);

    void deleteBrandById(Long bid);

    List<Brand> getBrandByCid(Long cid);

    List<Brand> getBrandsByIds(List<Long> ids);
}
