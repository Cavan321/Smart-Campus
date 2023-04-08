package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zhxy.pojo.Admin;
import com.example.zhxy.service.AdminService;
import com.example.zhxy.util.MD5;
import com.example.zhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
@Api(tags = "管理员管理器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // GET  sms/adminController/getAllAdmin/1/3
    //分页带条件查询管理员信息
    @ApiOperation("分页获取所有Admin信息【带条件】")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("管理员姓名") String adminName
    ){
        Page<Admin> pageParam = new Page<>(pageNo, pageSize);
        IPage<Admin> page = adminService.getAdminByOpr(pageParam,adminName);
        return Result.ok(page);
    }

    // POST  sms/adminController/saveOrUpdateAdmin
    @ApiOperation("添加或修改管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@ApiParam("JSON格式的Admin对象") @RequestBody Admin admin){
        Integer id = admin.getId();
        if (id == null || 0==id){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return  Result.ok();
    }


    // DELETE   sms/adminController/deleteAdmin
    @ApiOperation("删除一个或多个Admin信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@ApiParam("要删除的admin的JSON格式的id合集") @RequestBody List<Integer> ids){
        adminService.removeByIds(ids);
        return Result.ok();
    }

}
