package com.itheima.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PackageDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Package;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = PackageService.class)
public class PackageServiceImpl implements PackageService {
    @Autowired
    private PackageDao packageDao;

    @Override
    public PageResult<Package> findPage(QueryPageBean queryPageBean) {
        // 判断是否有查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            // 有查询条件
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        // 用分页插件进行分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 调用dao层方法进行分页查询，返回page对象
        Page<Package> page = packageDao.findPage(queryPageBean.getQueryString());
        // 将所查询到的分页数据都封装在pageResult里面
        PageResult<Package> pageResult = new PageResult<>(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public void add(Package pkg,Integer[] checkgroupIds) {
        // 调用dao层添加套餐信息
        packageDao.add(pkg);
        // 获取套餐的id
        Integer pkgId = pkg.getId();
        // 遍历检查组的id
        if (null != checkgroupIds) {
            for (Integer checkgroupId : checkgroupIds) {
                // 调用dao层添加套餐与检查组的关系
                packageDao.addPackageCheckGroup(pkgId,checkgroupId);
            }
        }
    }

    @Override
    public List<Package> findAll() {
        List<Package> packageList = packageDao.findAll();
        return packageList;
    }

    @Override
    public Package findById(int id) {
        return packageDao.findById(id);
    }
    /**
     * 查询套餐没有详情
     */
    @Override
    public Package findPackageById(int id) {
        return packageDao.findPackageById(id);
    }
}
