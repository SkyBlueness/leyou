package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecGroupService;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecController {

    @Autowired
    private SpecGroupService specGroupService;

    @Autowired
    private SpecParamService specParamService;

    /**
     * 查询分类的分组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> getGroupByCid(@PathVariable(value = "cid")Long cid){
        List<SpecGroup>list = specGroupService.getGroupByCid(cid);
        return ResponseEntity.ok(list);
    }

    /**
     * 添加分组
     * @param specGroup
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> saveGroup(@RequestBody SpecGroup specGroup){
        specGroupService.saveGroup(specGroup);
        return ResponseEntity.ok().build();
    }

    /**
     * 修改分组
     * @param specGroup
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> updateGroup(@RequestBody SpecGroup specGroup){
        specGroupService.updateGroup(specGroup);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据id删除分组
     * @param id
     * @return
     */
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable(value = "id")Long id){
        specGroupService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据分组id查询商品参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> getParam(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching
    ){
        List<SpecParam>list = specParamService.getParamByList(gid,cid,searching);
        return ResponseEntity.ok(list);
    }

    /**
     * 新增商品参数
     * @param specParam
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> saveParam(@RequestBody SpecParam specParam){
        specParamService.saveParam(specParam);
        return ResponseEntity.ok().build();
    }

    /**
     * 编辑参数
     * @param specParam
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateParam(@RequestBody SpecParam specParam){
        specParamService.updateParam(specParam);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteParamById(@PathVariable(value = "id") Long id){
        specParamService.deleteParamById(id);
        return ResponseEntity.ok().build();
    }
}
