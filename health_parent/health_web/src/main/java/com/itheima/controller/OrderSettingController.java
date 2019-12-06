package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.apache.ibatis.ognl.enhance.OrderedReturn;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService oss;
    @PostMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        // 解析excel
        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);
            // 将其转成实体orderSetting的list
            List<OrderSetting> list = new ArrayList<>();
            OrderSetting os = null;
            // 日期转化
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            for (String[] arr : strings) {
                // 日期  可预约人数
                os = new OrderSetting(sdf.parse(arr[0]),Integer.valueOf(arr[1]));
                list.add(os);
            }
            // 调用业务服务
            if (list.size() > 0) {
                oss.doImport(list);
                return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }
    @PostMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month) {
        // 调用service查询，返回orderSetting集合
        List<OrderSetting> list = oss.getOrderSettingByMonth(month);
        // 转成leftobj的格式{date：6，number：120，reservations：1}
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        // 每日的数据
        // 数据格式的转换
        if (null != list && list.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("d");
            for (OrderSetting os : list) {
                Map<String,Object> dayData = new HashMap<>();
                dayData.put("number",os.getNumber());
                dayData.put("reservations",os.getReservations());
                // 获取日期中的天，并转成整形
                dayData.put("date",Integer.valueOf(sdf.format(os.getOrderDate())));
                resultList.add(dayData);
            }
        }
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,resultList);
    }
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        // 调用业务层方法
        oss.editNumberByDate(orderSetting);
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);

    }
}
