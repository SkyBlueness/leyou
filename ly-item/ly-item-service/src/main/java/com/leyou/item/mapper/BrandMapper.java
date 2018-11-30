package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> , SelectByIdListMapper<Brand,Long> {

    @Insert("insert into tb_category_brand (category_id,brand_id) values(#{cid},#{bid}) ")
    void save_cids(@Param(value = "bid") Long bid, @Param(value = "cid") Integer cid);

    @Update("UPDATE tb_category_brand SET category_id = #{cid} WHERE brand_id = #{bid}")
    void update_csid(@Param(value = "bid") Long bid, @Param(value = "cid") Integer cid);

    @Delete("DELETE FROM tb_category_brand WHERE brand_id = #{bid}")
    void deleteCategoryBrand(Long bid);

    @Select("SELECT b.* FROM tb_category_brand cb INNER JOIN tb_brand b ON cb.brand_id = b.id WHERE cb.category_id = #{cid}")
    List<Brand> getBrandByCid(@Param(value = "cid")Long cid);
}
