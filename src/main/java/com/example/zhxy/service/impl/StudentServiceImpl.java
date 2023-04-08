package com.example.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zhxy.mapper.StudentMapper;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.pojo.Student;
import com.example.zhxy.service.StudentService;
import com.example.zhxy.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import springfox.documentation.service.ApiListing;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
@Service(value = "studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student login(LoginForm loginForm) {
        //通过queryWrapper来进行条件查询
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Student student = baseMapper.selectOne(queryWrapper);

        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    //分页带条件查询班级信息
    @Override
    public IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        //如果学生不为空，进行条件查询
        if (student != null){
            //如果学生姓名不为空，则进行学生姓名模糊查询
            String name = student.getName();
            if (!StringUtils.isEmpty(name)){
                queryWrapper.like("name",name);
            }
            //如果学生班级不为空，则通过学生班级进行模糊查询
            String clazzName = student.getClazzName();
            if (!StringUtils.isEmpty(clazzName)){
                queryWrapper.like("clazz_name",clazzName);
            }
        }
        //按学号降序排列
        queryWrapper.orderByDesc("id");
        Page<Student> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }
}
