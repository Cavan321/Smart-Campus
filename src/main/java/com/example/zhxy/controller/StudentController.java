package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zhxy.pojo.Student;
import com.example.zhxy.service.StudentService;
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
@Api(tags = "学生管理器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;


    //sms/studentController/delStudentById
    @ApiOperation("删除一个或多个学生信息")
    @DeleteMapping("delStudentById")
    public Result delStudentById(@ApiParam("要删除的所有student的id的JSON合集") @RequestBody List<Integer> ids){
        studentService.removeByIds(ids);
        return Result.ok();
    }


    //sms/studentController/addOrUpdateStudent
    @ApiOperation("添加或修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@ApiParam("添加或修改的JSON格式的学生信息") @RequestBody Student student) {

        //当id为空或者为0时，表示添加学生信息，此时需要把密码从明文转成密文
        Integer id = student.getId();
        if (null == id || 0 == id) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }


    //sms/studentController/getStudentByOpr/1/3
    //分页查询
    @ApiOperation("分页带条件查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("页码数") @PathVariable(value = "pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable(value = "pageSize") Integer pageSize,
            @ApiParam("查询的条件") Student student
    ) {
        Page<Student> pageParam = new Page<>(pageNo, pageSize);
        IPage<Student> studentPage = studentService.getStudentByOpr(pageParam, student);
        return Result.ok(studentPage);
    }


}
