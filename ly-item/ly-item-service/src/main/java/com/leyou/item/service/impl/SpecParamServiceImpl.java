package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecParamServiceImpl implements SpecParamService {

    @Autowired
    private SpecParamMapper specParamMapper;

    @Transactional
    @Override
    public void saveParam(SpecParam specParam) {
        specParamMapper.insert(specParam);
    }

    @Transactional
    @Override
    public void updateParam(SpecParam specParam) {
        specParamMapper.updateByPrimaryKey(specParam);
    }

    @Transactional
    @Override
    public void deleteParamById(Long id) {
        specParamMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<SpecParam> getParamByList(Long gid, Long cid, Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);
        return specParamMapper.select(param);
    }
}
