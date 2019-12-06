package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Package;
import com.itheima.service.PackageService;
import com.itheima.utils.QiNiuUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/package")
public class PackageController {
    @Reference
    private PackageService packageService;
    @GetMapping("/getPackage")
    public Result getPackage() {
        List<Package> lists = packageService.findAll();
        if (null != lists) {
            // 拼接图片完整路径
            lists.forEach(pkg -> {
                pkg.setImg("http://"+ QiNiuUtil.DOMAIN+"/"+pkg.getImg());
            });
        }
        return new Result(true, MessageConstant.QUERY_PACKAGELIST_SUCCESS,lists);
    }
    @GetMapping("/findById")
    public Result findById(int id) {
        // 调用业务层方法查询套餐
        Package pkg = packageService.findById(id);
        // 拼接图片完整路径
        pkg.setImg("http://"+ QiNiuUtil.DOMAIN+"/"+pkg.getImg());
        return new Result(true,MessageConstant.QUERY_PACKAGE_SUCCESS,pkg);
    }
    @GetMapping("/findPackageById")
    public Result findPackageById (int id) {
       Package pkg = packageService.findPackageById(id);
       pkg.setImg("http://"+ QiNiuUtil.DOMAIN+"/"+pkg.getImg());
       return new Result(true,MessageConstant.QUERY_PACKAGE_SUCCESS,pkg);
    }
}
