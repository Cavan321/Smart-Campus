package com.example.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zhxy.mapper.TeacherMapper;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.pojo.Teacher;
import com.example.zhxy.service.TeacherService;
import com.example.zhxy.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
@Service(value = "teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher login(LoginForm loginForm) {
        //通过queryWrapper来设置查询条件
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password",MD5.encrypt(loginForm.getPassword()));

        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public Teacher getTeacherById(Long userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    //点击“年级管理”后分页查询数据，并显示
    @Override
    public IPage<Teacher> getTeachersByOpr(Page<Teacher> page, Teacher teacher) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        //如果teacher不为空，进行模糊查询
        if (teacher != null){
            //如果班级名称不为空，将其作为模糊查询条件
            String clazzName = teacher.getClazzName();
            if (!StringUtils.isEmpty(clazzName)){
                queryWrapper.like("clazz_name",clazzName);
            }
            //如果教师名称不为空，将其作为模糊查询条件
            String teacherName = teacher.getName();
            if (!StringUtils.isEmpty(teacherName)){
                queryWrapper.like("name",teacherName);
            }
            queryWrapper.orderByDesc("id");
            queryWrapper.orderByAsc("name");

        }
        Page<Teacher> teacherPage = baseMapper.selectPage(page, queryWrapper);
        return teacherPage;
    }
}
