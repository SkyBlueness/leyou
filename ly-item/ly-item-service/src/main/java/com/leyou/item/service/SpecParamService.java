package com.leyou.item.service;

import com.leyou.item.pojo.SpecParam;

import java.util.List;

public interface SpecParamService {

    void saveParam(SpecParam specParam);

    void updateParam(SpecParam specParam);

    void deleteParamById(Long id);

    List<SpecParam> getParamByList(Long gid, Long cid, Boolean searching);
}
