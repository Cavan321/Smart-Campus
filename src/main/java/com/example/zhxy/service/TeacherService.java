package com.example.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.pojo.Teacher;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
public interface TeacherService extends IService<Teacher> {
    

    Teacher login(LoginForm loginForm);

    Teacher getTeacherById(Long userId);

    //点击“教师管理”后分页查询数据，并显示
    IPage<Teacher> getTeachersByOpr(Page<Teacher> page, Teacher teacher);
}
