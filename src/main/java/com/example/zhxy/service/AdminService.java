package com.example.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zhxy.pojo.Admin;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.pojo.Student;
import com.example.zhxy.pojo.Teacher;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
public interface AdminService extends IService<Admin> {
    //通过客户端传递的登录信息去数据库中进行比对，看数据库中是否存在此信息
    Admin login(LoginForm loginForm);

    Admin getAdminById(Long userId);


    //分页带条件查询管理员信息
    IPage<Admin> getAdminByOpr(Page<Admin> pageParam, String adminName);
}
