package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zhxy.pojo.Clazz;
import com.example.zhxy.pojo.Grade;
import com.example.zhxy.service.ClazzService;
import com.example.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
@Api(tags = "班级管理器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    //sms/clazzController/deleteClazz
    @ApiOperation("删除Clazz信息")
    @DeleteMapping("deleteClazz")
    public Result deleteClazz(@ApiParam("要删除所有的clazz的id的JSON合集") @RequestBody List<Integer> ids){
        clazzService.removeByIds(ids);
        return Result.ok();
    }

    //添加或修改班级信息
    //sms/clazzController/saveOrUpdateClazz
    @ApiOperation("添加或修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(@ApiParam("JSON格式的班级信息") @RequestBody Clazz clazz){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    //sms/clazzController/getClazzs
    @ApiOperation("查询所有班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> clazzs = clazzService.getClazzs();
        return Result.ok(clazzs);
    }

    // /sms/clazzController/getClazzsByOpr/1/3
    @ApiOperation("分页带条件查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            @ApiParam("分页查询的页码数") @PathVariable(value = "pageNo") Integer pageNo,
            @ApiParam("分页查询页的大小") @PathVariable(value = "pageSize") Integer pageSize,
            @ApiParam("分页查询的查询条件") Clazz clazz
    ) {
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        IPage<Clazz> iPage = clazzService.getClazzsByOpr(page,clazz);
        return Result.ok(iPage);
    }

}
