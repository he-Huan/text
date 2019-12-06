package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;

    @GetMapping("/getUsername")
    public Result getUsername() {// 展示用户名
        // SecurityContextHolder拥有security的所有信息（配置，用户权限）
        // getAuthentication:获取认证信息->登录用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }
   /* @GetMapping("/getMenu")
    public Result getMenu() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findMenuByUsername(username);
        //Map<String, List<String>> map = new HashMap<>();
        List<Map<String,Object>> list = new ArrayList<>();
        //List list = new ArrayList();
        for (Role role : user.getRoles()) {
            for (Menu menu : role.getMenus()) {
                Map<String,Object> map = new HashMap<>();
                List<Map<String,Object>> childrenList = new ArrayList<>();
                // 如果路径以"/"开头，则表示是子菜单children
                if (menu.getPath().startsWith("/")) {
                    HashMap<String, Object> childrenMap = new HashMap<>();
                    childrenMap.put("title",menu.getName());
                    childrenMap.put("path",menu.getPath());
                    childrenMap.put("linkUrl",menu.getLinkUrl());
                    childrenMap.put("children","");
                    childrenList.add(childrenMap);
                }
                map.put("title",menu.getName());
                map.put("path",menu.getPath());
                map.put("linkUrl",menu.getLinkUrl());
                map.put("children",childrenList);
                list.add(map);
            }
        }
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,list);    }*/


   /* @GetMapping("/getMenu")
    public Result getMenu() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findMenuByUsername(username);

        List<Map<String, Object>> list = new ArrayList<>();
        // 遍历得到该用户所有角色
        for (Role role : user.getRoles()) {
            // 根据角色查找该用户的菜单,遍历菜单
            for (Menu menu : role.getMenus()) {
                // 一个菜单就是一个map
                HashMap<String, Object> parent = new HashMap<>();
                List<Map<String, Object>> childList = new ArrayList<>();
                HashMap<String, Object> child = new HashMap<>();
                if (null != menu.getParentMenuId()) {
                    if (menu.getParentMenuId() == menu.getId()) {
                        // 如果父菜单id=id，说明该菜单为其下的子菜单
                    }
                    // 说明该菜单为子菜单,并且将其添加到对应的父菜单id里面去
                    child.put("path", menu.getPath());
                    child.put("title", menu.getName());
                    child.put("linkUrl", menu.getLinkUrl());
                    child.put("children", "");
                    childList.add(child);
                }
                // 说明该菜单为父菜单
                parent.put("path", menu.getPath());
                parent.put("title", menu.getName());
                parent.put("icon", menu.getIcon());
                // 将子菜单添加到父菜单里面去
                parent.put("children", childList);

                list.add(parent);
            }
        }
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, list);
    }*/
}
