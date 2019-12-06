package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.exception.MyException;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Override
    public Order submit(Map<String, String> map) throws MyException,Exception {
        // 判断是否可以预约，用日期判断
        String orderDateStr = map.get("orderDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDate = sdf.parse(orderDateStr);
        OrderSetting os = orderSettingDao.findByOrderDate(orderDate);
        if (null == os) {
            // 所选日期不能进行体检预约
            throw new MyException(MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        if (os.getNumber() < os.getReservations()) {
            // 说明当日预约已满
            throw new MyException(MessageConstant.ORDER_FULL);
        }
        // 可以预约，判断是否为会员,通过手机号码
        String telephone = map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        // 如果不是会员，则注册成为会员
        if (null == member) {
            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            member.setSex(map.get("sex"));
            member.setName(map.get("name"));
            member.setIdCard(map.get("idCard"));
            memberDao.add(member);
        }
        // 是会员，则取出他的id，以便后续使用
        int memberId = member.getId();
        // 判断是否已经预约过了
        Order order = new Order();
        order.setMemberId(memberId);
        order.setPackageId(Integer.valueOf(map.get("packageId")));
        order.setOrderDate(orderDate);
        List<Order> orders = orderDao.findByCondition(order);
        // 如果已预约，则报错.没预约则可以预约成功
        if (null != orders && orders.size() > 0) {
            // 说明已经预约
            throw new MyException(MessageConstant.HAS_ORDERED);
        }
        // 说明未预约，则调用dao去预约
        order.setOrderStatus("未到诊");
        order.setOrderType(map.get("orderType"));
        orderDao.add(order);
        // 更新t_orderSetting的已预约人数
        orderSettingDao.editReservationsByOrderDate(os);
        orderSettingDao.editNumberByOrderDate(os);
        return order;
    }

    @Override
    public Map<String, Object> findById(int id) {
        return orderDao.findById(id);
    }
}
