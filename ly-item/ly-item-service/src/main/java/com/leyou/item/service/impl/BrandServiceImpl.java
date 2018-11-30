package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> getBrandByPage(Integer page, String key, Integer row, String sortBy, Boolean desc) {
        //分页
        PageHelper.startPage(page,row);
        //筛选
        Example example = new Example(Brand.class);
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("name","%"+key+"%").orEqualTo("letter",key);
        }
        //排序
        if(StringUtils.isNotBlank(sortBy)){
            String orderBy = sortBy + (desc ? " DESC":" ASC");
            example.setOrderByClause(orderBy);
        }
        //查询
        List<Brand> list = brandMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        return new PageResult(pageInfo.getTotal(), list);
    }

    @Transactional
    @Override
    public void saveBrand(Brand brand, List<Integer> cids) {
        brandMapper.insert(brand);
        for (Integer cid:cids){
            brandMapper.save_cids(brand.getId(),cid);
        }
    }

    @Transactional
    @Override
    public void updateBrand(Brand brand, List<Integer> cids) {
        brandMapper.updateByPrimaryKey(brand);
        for (Integer cid:cids){
            brandMapper.update_csid(brand.getId(),cid);
        }
    }

    @Transactional
    @Override
    public void deleteBrandById(Long bid) {
        Brand brand = new Brand();
        brand.setId(bid);
        brandMapper.delete(brand);
        brandMapper.deleteCategoryBrand(bid);
    }

    @Override
    public List<Brand> getBrandByCid(Long cid) {
        return brandMapper.getBrandByCid(cid);
    }

    @Override
    public List<Brand> getBrandsByIds(List<Long> ids) {
        return brandMapper.selectByIdList(ids);
    }


}
