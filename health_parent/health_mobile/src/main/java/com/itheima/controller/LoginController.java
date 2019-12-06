package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;
    @PostMapping("/check")
    public Result check(@RequestBody Map<String,Object> map, HttpServletResponse res) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        // 校验验证码
        Jedis jedis = jedisPool.getResource();
        String key = "login_"+ RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        String code = jedis.get(key);
        if (StringUtils.isEmpty(code)) {
            // redis中没有验证码,或者过时
            return new Result(false,"请点击获取验证码");
        }
        // 有值
        if (!code.equals(validateCode)) {
            // 输入的验证码和redis中的验证码不一致
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 清除redis中的验证码
        jedis.del(key);
        // 调用业务层处理登录请求
        memberService.login(telephone);
        // 跟踪用户行为
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(60*60*24*30);// 有效期为1个月
        cookie.setPath("/");// 访问哪个网页时会带上这个cookie  / 根目录，所有
        res.addCookie(cookie);
        return  new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
