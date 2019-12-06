package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> map) throws Exception {
        Jedis jedis = jedisPool.getResource();
        // 验证码有效性
        String telephone = map.get("telephone");
        String key = "order_"+ RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        // 获取redis中的验证码
        String codeRedis = jedis.get(key);
        if (StringUtils.isEmpty(codeRedis)) {
            // redis中没有验证码,或者过时
            return new Result(false,"请点击获取验证码");
        }
        // 有值
        if (!codeRedis.equals(map.get("validateCode"))) {
            // 输入的验证码和redis中的验证码不一致
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 清除redis中的验证码
        jedis.del(key);
        // 调用业务服务
        map.put("orderType","微信预约");
        Order order = orderService.submit(map);
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS,order);
    }
    @GetMapping("/findById")
    public Result findById(int id) {
        // 根据所传入的id查询调用业务层查询预约信息
        Map<String,Object> map = orderService.findById(id);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
    }
}
