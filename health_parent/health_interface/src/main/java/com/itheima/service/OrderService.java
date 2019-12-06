package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.exception.MyException;
import com.itheima.pojo.Order;

import java.text.ParseException;
import java.util.Map;


public interface OrderService {
    Order submit(Map<String, String> map) throws MyException, Exception;

    Map<String, Object> findById(int id);
}
