package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("securityUserServiceImpl")
public class SecurityUserServiceImpl implements UserDetailsService {
    @Reference
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.认证
        // 通过用户名查询用户是否存在数据库，，调用业务服务
        User user = userService.findByUsername(username);
        // 如果用户不存在，返回null
        if (null == user) {
            return null;
        }
        // 2，授权
        // 用户存在
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 获取用户所拥有的角色
        Set<Role> roles = user.getRoles();
        if (null != roles) {
            GrantedAuthority authority = null;
            for (Role role : roles) {
                authority = new SimpleGrantedAuthority(role.getKeyword());
                // 添加用户角色列表
                authorities.add(authority);
                // 判断角色下是有拥有权限
                if (null != role.getPermissions()) {
                    // 该角色拥有权限
                    for (Permission p : role.getPermissions()) {
                        // 添加用户的权限
                        authority = new SimpleGrantedAuthority(p.getKeyword());
                        authorities.add(authority);
                    }
                }
            }
        }
        // 让security帮我们校验用户名和密码，调用encoder的matches方法
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("admin"));
    }
}
