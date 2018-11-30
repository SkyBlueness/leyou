package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {

    /**
     * 查询分类的分组
     * @param cid
     * @return
     */
    @GetMapping("spec/groups/{cid}")
    List<SpecGroup> getGroupByCid(@PathVariable(value = "cid")Long cid);

    /**
     * 根据查询规格参数
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    @GetMapping("spec/params")
    List<SpecParam> getParam(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching
    );
}
