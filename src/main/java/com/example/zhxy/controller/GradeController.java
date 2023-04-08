package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zhxy.pojo.Grade;
import com.example.zhxy.service.GradeService;
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
@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    //sms/gradeController/getGrades
    @ApiOperation("获取全部年级")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }

    //删除和批量删除年级主任
    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@ApiParam("要删除所有的grade的id的JSON集合") @RequestBody List<Integer> ids){
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    //添加和修改年级主任
    @ApiOperation("添加或修改grade信息，有id属性则是修改，无id属性则是添加")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@ApiParam("JSON格式的grade对象") @RequestBody Grade grade){
        //接收参数
        //调用服务层完成增加或修改
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    //点击“年级管理”后分页查询数据，并显示
    @ApiOperation("根据年级名称模糊查询并分页")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询的页码数") @PathVariable(value = "pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询模糊匹配的名称") @RequestParam(value = "gradeName",required = false) String gradeName){
        //分页带条件查询
        Page<Grade> page = new Page<>(pageNo,pageSize);
        //通过服务层查询
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page,gradeName);

        //封装result对象并返回
        return Result.ok(pageRs);
    }



}
