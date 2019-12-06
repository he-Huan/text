package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao osd;
    @Override
    @Transactional
    public void doImport(List<OrderSetting> list) {
        // 遍历list
        for (OrderSetting orderSetting : list) {
            // 调用dao按日期查询是否存在，存在则更新，不存在则插入
            OrderSetting osInDb = osd.findByOrderDate(orderSetting.getOrderDate());
            if (osInDb != null) {
                // 更新数据库
                osInDb.setNumber(orderSetting.getNumber());
                osd.updateNumber(osInDb);
            }else {
                // 插入数据
                osd.add(orderSetting);
            }
        }
    }

    @Override
    public List<OrderSetting> getOrderSettingByMonth(String month) {
        // 按时间范围查询
        String startDate = month +"-01";
        String endDate = month + "-31";
        // 调用dao层范围查询
        List<OrderSetting> list = osd.getOrderSettingBetweenDate(startDate,endDate);
        return list;
    }

    /***
     * 通过日期预约设置人数
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        // 判断是否存在数据
        OrderSetting os = osd.findByOrderDate(orderSetting.getOrderDate());
        if (null != os) {
            // 说明已经存在这个日子了，更新可预约人数数据
            orderSetting.setId(os.getId());
            osd.updateNumber(orderSetting);
        }else {
            // 说明不存在，则添加数据
            os.setNumber(orderSetting.getNumber());
            osd.add(os);
        }
    }
}
