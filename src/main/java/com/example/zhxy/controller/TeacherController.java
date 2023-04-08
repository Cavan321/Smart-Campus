package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zhxy.pojo.Teacher;
import com.example.zhxy.service.TeacherService;
import com.example.zhxy.util.MD5;
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
@Api(tags = "教师信息管理控制器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;



    //DELETE   sms/teacherController/deleteTeacher
    @ApiOperation("删除一个或多个教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(@ApiParam("要删除的教师的id合集") @RequestBody List<Integer> ids){
        teacherService.removeByIds(ids);
        return Result.ok();
    }


    //POST   sms/teacherController/saveOrUpdateTeacher
    @ApiOperation("添加或修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@ApiParam("要添加或修改的JSON格式的Teacher对象") @RequestBody Teacher teacher){
        //当id为空或者为0时表示添加，需要将密码转为密文
        if(null == teacher.getId() || 0 == teacher.getId()){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }


    //    sms/teacherController/getTeachers/1/3
    //点击"教师管理”后分页查询数据，并显示
    @ApiOperation("获取教师信息,分页带条件")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @PathVariable(value = "pageNo") Integer pageNo,
            @PathVariable(value = "pageSize") Integer pageSize,
            Teacher teacher
    ) {
        Page<Teacher> page = new Page<>(pageNo, pageSize);
        IPage<Teacher> teacherIPage = teacherService.getTeachersByOpr(page, teacher);

        return Result.ok(teacherIPage);
    }




}
