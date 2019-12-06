package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        // 判断查询条件是否为空
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            // 不为空则需要模糊查询
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        // 使用分页插件pageHelper进行分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 查询语句会被分页
        Page<CheckGroup> checkGroups = checkGroupDao.findPage(queryPageBean.getQueryString());
        // 所有的分页结果都封装在pageResult里面
        PageResult pageResult = new PageResult(checkGroups.getTotal(),checkGroups.getResult());
        return pageResult;
    }

    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 调用dao层方法添加数据，并且拿到mybatis返回的checkGroup的id
        checkGroupDao.add(checkGroup);
        Integer checkGroupId = checkGroup.getId();
        // 给表添加关系
        // 遍历获取到的checkitemIds
        if (null != checkitemIds) {
            for (Integer checkitemId : checkitemIds) {
                // 添加表关系
                checkGroupDao.addCheckGroupCheckItem(checkGroupId,checkitemId);
            }
        }
    }

    @Override
    public CheckGroup findById(Integer id) {
        // 根据检查组id查询一项检查组
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsById(Integer id) {
        return checkGroupDao.findCheckItemIdsById(id);
    }

    @Transactional
    @Override
    public void update(CheckGroup checkGroup, Integer[] checkItemIds) {
        // 根据传入的检查组数据执行更新操作
        checkGroupDao.update(checkGroup);
        // 获取更新的检查组的id，先删除已有关系
        Integer checkGroupId = checkGroup.getId();
        checkGroupDao.deleteCheckItemById(checkGroupId);
        // 添加新关系
        if (null != checkItemIds) {
            for (Integer checkItemId : checkItemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroupId,checkItemId);
            }
        }
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
