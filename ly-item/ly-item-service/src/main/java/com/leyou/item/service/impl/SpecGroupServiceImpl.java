package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecGroupServiceImpl implements SpecGroupService {

    @Autowired
    private SpecGroupMapper specMapper;

    @Override
    public List<SpecGroup> getGroupByCid(Long cid) {
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        return specMapper.select(group);
    }

    @Transactional
    @Override
    public void saveGroup(SpecGroup specGroup) {
        specMapper.insert(specGroup);
    }

    @Transactional
    @Override
    public void updateGroup(SpecGroup specGroup) {
        specMapper.updateByPrimaryKey(specGroup);
    }

    @Override
    public void deleteGroup(Long id) {
        specMapper.deleteByPrimaryKey(id);
    }
}
