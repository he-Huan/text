package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.dao.CheckItemDao;
import com.itheima.exception.MyException;
import com.itheima.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        // 进行分页查询
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            // 如果有查询条件就进行模糊查询
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        // 使用分页插件进行分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 查询语句会被分页
        Page<CheckItem> page = checkItemDao.findPage(queryPageBean.getQueryString());
        // 分页的结果封装在pageResult里面
        PageResult<CheckItem> pageResult = new PageResult<>(page.getTotal(), page.getResult());
        return pageResult;

    }

    @Override
    public void deleteById(int id) throws MyException {
        // 调用dao方法删除检查项，如果检查项没有被检查组引用，可以删除，否则不能删除
        int count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
            // 说明检查项已经被引用了，不能删除,抛自定义异常
            throw  new MyException(MessageConstant.DELETE_CHECKITEM_FAIL);
        }else {
            // 可以删除
            checkItemDao.deleteById(id);
        }
    }

    @Override
    public CheckItem findById(int id) {
        // 调用dao层方法根据id查询数据，返回checkItem对象
        return checkItemDao.findById(id);
    }

    @Override
    public void update(CheckItem checkItem) {
        // 调用dao层方法执行更新操作
        checkItemDao.update(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

}
