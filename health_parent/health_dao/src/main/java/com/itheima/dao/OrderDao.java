package com.itheima.dao;

import com.itheima.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    void add(Order order);
    List<Order> findByCondition(Order order);
    Map findById4Detail(Integer id);
    Integer findOrderCountByDate(String date);
    Integer findOrderCountAfterDate(String date);
    Integer findVisitsCountByDate(String date);
    Integer findVisitsCountAfterDate(String date);
    List<Map<String, Object>> findHotPackage();

    Map<String, Object> findById(int id);

    Integer findOrderCountBetweenDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
