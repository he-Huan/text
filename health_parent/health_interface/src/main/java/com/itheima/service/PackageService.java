package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Package;

import java.util.List;

public interface PackageService {
    PageResult<Package> findPage(QueryPageBean queryPageBean);

    void add(Package pkg, Integer[] checkgroupIds);

    List<Package> findAll();

    Package findById(int id);

    Package findPackageById(int id);
}
