package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {
    OrderSetting findByOrderDate(Date orderDate);

    void updateNumber(OrderSetting osInDb);

    void add(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingBetweenDate(@Param("startDate") String startDate,@Param("endDate") String endDate);

    //更新可预约人数
    void editNumberByOrderDate(OrderSetting orderSetting);
    //更新已预约人数
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
