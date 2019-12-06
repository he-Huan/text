package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {
    Page<CheckGroup> findPage(String queryString);

    void add(CheckGroup checkGroup);

    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkItemId") Integer checkitemId);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsById(Integer id);

    // 更新检查组
    void update(CheckGroup checkGroup);

    // 根据id删除已有关系
    void deleteCheckItemById(Integer checkGroupId);

    // 查询所有
    List<CheckGroup> findAll();
}
