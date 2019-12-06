package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
    @GetMapping("/send4Order")
    public Result send4Order(String telephone) {
        // 获取redis
        Jedis jedis = jedisPool.getResource();
        String key = "order_"+ RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        // 判断是否发送过了，redis中存在key？
        if (null != jedis.get(key)) {
            // redis中有值，已经发送过了
            return new Result(true, MessageConstant.SENT_VALIDATECODE);
        }
        // 生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        // 发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code+"");
            // 存入redis,设置验证码有效时间为5分钟。超时会自动删除
            jedis.setex(key,5*60,code+"");

            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
    }

    /**
     * 发送登陆时的验证码
     * @param telephone
     * @return
     */
    @GetMapping("/send4Login")
    public Result send4Login(String telephone) {
        Jedis jedis = jedisPool.getResource();
        String key = "login_"+ RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        if (null != jedis.get(key)) {
            // 说明redis中已经有值了
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        }
        // 生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        // 发送验证码到用户手机上
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code+"");
            // 存入redis,设置验证码有效时间为5分钟。超时会自动删除
            jedis.setex(key,5*60,code+"");

            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
    }
}
