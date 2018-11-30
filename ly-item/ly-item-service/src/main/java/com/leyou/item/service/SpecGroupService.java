package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;

import java.util.List;

public interface SpecGroupService {
    List<SpecGroup> getGroupByCid(Long cid);

    void saveGroup(SpecGroup specGroup);

    void updateGroup(SpecGroup specGroup);

    void deleteGroup(Long id);
}
