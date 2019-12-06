package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Package;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PackageDao {
    Page<Package> findPage(String queryString);

    void add(Package pkg);

    void addPackageCheckGroup(@Param("pkgId") Integer pkgId, @Param("checkgroupId") Integer checkgroupId);

    List<Package> findAll();

    Package findById(int id);

    Package findPackageById(int id);

    List<Map<String, Object>> getPackageReport();
}
