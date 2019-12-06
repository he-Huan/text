package com.itheima.job;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiNiuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class MyJob {
    @Autowired
    private JedisPool jedisPool;
    public void doAbc() {
        Jedis jedis = jedisPool.getResource();
        // 取出redis中所有图片的集合减去保存到数据库中的图片的集合
        Set<String> need2delete = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        // 调用七牛工具删除服务器上的图片
        QiNiuUtil.removeFiles(need2delete.toArray(new String[] {}));
        // 成功后要删除redis中的缓存，所有的和保存到数据库中的
        jedis.del(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
    }

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring-job.xml");
    }
}
