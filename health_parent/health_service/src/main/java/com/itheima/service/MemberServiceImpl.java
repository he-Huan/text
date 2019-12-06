package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Override
    public void login(String telephone) {
        // 先通过手机号查询该手机号是否为会员，如果不是则添加会员
        Member member = memberDao.findByTelephone(telephone);
        if (null == member) {
            // 没有该会员，则添加为会员
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);
        }
    }

    /**
     * 会员数量统计
     * @return
     */
    @Override
    public Map<String, List<Object>> getMemberReport() {
        Map<String,List<Object>> map = new HashMap<>(12);
        // 1.获取上一年分的数据
        // 日历对象，java中来操作日期时间，当前系统时间
        Calendar car = Calendar.getInstance();
        // 减去12个月，回到十二个月之前那
        car.add(Calendar.MONTH,-12);
        // 2.循环12个月，每个月查询一次
        List<Object> months = new ArrayList<>();
        // 返回的会员数量集合
        List<Object> memberCount = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (int i = 1; i <= 12; i++) {
            // 计算当前月的值
            car.add(Calendar.MONTH,1);
            Date date = car.getTime();
            // 月份数据的字符串
            String monthStr = sdf.format(date);
            months.add(monthStr);

            // 查询指定日期之前的会员数量
            Integer monthCount = memberDao.findMemberCountBeforeDate(monthStr + "-31");
            memberCount.add(monthCount);
        }
        // 3.将查询到的数据封装到months list 月份，memberCounts list 到这个月为止的会员总数量
        map.put("months",months);
        map.put("memberCount",memberCount);
        return map;
    }
}
