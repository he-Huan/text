package com.itheima.service;

import java.util.List;
import java.util.Map;

public interface MemberService {
    void login(String telephone);

    Map<String, List<Object>> getMemberReport();
}
