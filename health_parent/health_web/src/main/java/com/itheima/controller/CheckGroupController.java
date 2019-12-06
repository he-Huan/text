package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.GET;
import java.util.List;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Reference(interfaceClass = CheckGroupService.class)
    private CheckGroupService checkGroupService;

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<CheckGroup> pageResult = checkGroupService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
    }
    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds) {
        // 调用业务层方法执行添加操作
        checkGroupService.add(checkGroup, checkitemIds);
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
    @GetMapping("/findById")
    public Result findById(Integer id) {
        // 调用业务层方法查询检查组
        CheckGroup checkGroup = checkGroupService.findById(id);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }
    @GetMapping("/findCheckItemIdsById")
    public Result findCheckItemIdsById(int id) {
        List<Integer> list = checkGroupService.findCheckItemIdsById(id);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
    }
    @PostMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup,Integer[] checkItemIds) {
        checkGroupService.update(checkGroup,checkItemIds);
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }
    @PostMapping("/findAll")
    public Result findAll() {
        List<CheckGroup> list = checkGroupService.findAll();
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
    }
}
