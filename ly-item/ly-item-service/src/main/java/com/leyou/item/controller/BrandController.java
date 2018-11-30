package com.leyou.item.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 品牌分页查询
     * @param page
     * @param key
     * @param row
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>>getBrandByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "rew",defaultValue = "5")Integer row,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "desc",defaultValue = "true")Boolean desc
    ){
        return ResponseEntity.ok(brandService.getBrandByPage(page,key,row,sortBy,desc));
    }

    /**
     * 保存商品
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> save(Brand brand, @RequestParam(value = "cids") List<Integer> cids){
        brandService.saveBrand(brand,cids);
        return ResponseEntity.ok().build();
    }

    /**
     * 修改商品
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping()
    public ResponseEntity<Void> update(Brand brand, @RequestParam(value = "cids") List<Integer> cids){
        brandService.updateBrand(brand,cids);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据品牌id删除
     * @param bid
     * @return
     */
    @DeleteMapping("{bid}")
    public ResponseEntity<Void> deleteById(@PathVariable(value = "bid")Long bid){
        brandService.deleteBrandById(bid);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据分类id查询品牌
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> getBrandByCid(@PathVariable(value = "cid") Long cid){
        List<Brand>list = brandService.getBrandByCid(cid);
        return ResponseEntity.ok(list);
    }

    @GetMapping("list")
    public ResponseEntity<List<Brand>> getBrandsByIds(@RequestParam(value = "ids")List<Long>ids){
        return ResponseEntity.ok(brandService.getBrandsByIds(ids));
    }

}
