package com.leyou.item.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import com.leyou.item.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("spu")
public class SpuController {

    @Autowired
    private SpuService spuService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询spu
     * @param key
     * @param page
     * @param row
     * @param saleable
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<SpuBo>>getSpuPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "row",defaultValue = "5")Integer row,
            @RequestParam(value="saleable",required = false)Boolean saleable
    ){
        PageResult<SpuBo>list = spuService.getSpuPage(key,page,row,saleable);
        if (list.getItems() == null || list.getItems().size() == 0){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据spuId查询商品详情
     * @param spuId
     * @return
     */
    @RequestMapping("detail/{spuId}")
    public ResponseEntity<SpuDetail>getDetailBySpuId(@PathVariable("spuId")Long spuId){
        SpuDetail detail = spuService.getDetailBySpuId(spuId);
        return ResponseEntity.ok(detail);
    }

    /**
     * 根据spuId删除spuId
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void>deleteSpuBySpuId(@PathVariable(value = "id")Long id){
        goodsService.deleteSpuBySpuId(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 下架spu
     */
    @PutMapping("soldOut/{id}")
    public ResponseEntity<Void>soldOut(@PathVariable(value = "id")Long id){
        goodsService.soldOut(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 上架spu
     */
    @PutMapping("putAway/{id}")
    public ResponseEntity<Void>putAway(@PathVariable(value = "id")Long id){
        goodsService.putAway(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 根据spuid查询spu
     * @param spuId
     * @return
     */
    @GetMapping("{spuId}")
    public ResponseEntity<Spu> getSpuById(@PathVariable(value = "spuId")Long spuId){
        return spuService.getSpuById(spuId);
    }
}
