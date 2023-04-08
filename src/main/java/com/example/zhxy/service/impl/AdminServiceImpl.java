package com.example.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zhxy.mapper.AdminMapper;
import com.example.zhxy.pojo.Admin;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.pojo.Student;
import com.example.zhxy.pojo.Teacher;
import com.example.zhxy.service.AdminService;
import com.example.zhxy.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
@Service(value = "adminServiceImpl")
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    //通过客户端传递的登录信息去数据库中进行比对，看数据库中是否存在此信息
    @Override
    public Admin login(LoginForm loginForm) {
        //通过queryWrapper进行条件查询
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        //loginForm.getPassword()：获得明文，使用MD5加密后进行比对
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Long userId) {
        //通过queryWrapper进行条件查询
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        //select * from 表名 where id = ?
        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    //分页带条件查询管理员信息
    @Override
    public IPage<Admin> getAdminByOpr(Page<Admin> pageParam, String adminName) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(adminName)){
            queryWrapper.like("name",adminName);
        }
        queryWrapper.orderByDesc("id");
        queryWrapper.orderByAsc("name");
        Page<Admin> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }


}
