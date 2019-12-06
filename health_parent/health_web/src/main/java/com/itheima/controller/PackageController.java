package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Package;
import com.itheima.service.PackageService;
import com.itheima.utils.QiNiuUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MultipartFilter;
import redis.clients.jedis.JedisPool;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/package")
public class PackageController {
    @Autowired
    private JedisPool jedisPool;
    @Reference(interfaceClass = PackageService.class)
    private PackageService packageService;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        // 调用业务层方法执行分页
        PageResult<Package> pageResult = packageService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_PACKAGE_SUCCESS,pageResult);
    }

    /**
     * 图片上传
     * @param imgFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile) {
        // 拿到图片原名称
        String originalFilename = imgFile.getOriginalFilename();
        // 产生唯一key值，保证图片名不重复
        UUID uuid = UUID.randomUUID();
        // 截取文件扩展名
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = uuid.toString()+extension;
        // 调用七牛云上传图片
        try {
            QiNiuUtil.uploadViaByte(imgFile.getBytes(),newFilename);
            // 上传成功，把所有的图片保存到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFilename);
            // 封装返回结果
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("picName",newFilename);
            resultMap.put("domain",QiNiuUtil.DOMAIN);
            // 成功则返回给页面：图片名（picName）和domain（域名）
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,resultMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 图片上传失败
        return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**
     * 添加套餐
     */
    @PostMapping("/add")
    public Result add(@RequestBody Package pkg, Integer[] checkgroupIds) {
        // 调用业务层方法执行添加操作
        packageService.add(pkg,checkgroupIds);
        // 上传图片成功，把图片保存到redis,db集合
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pkg.getImg());
        return new Result(true,MessageConstant.ADD_PACKAGE_SUCCESS);
    }
}
