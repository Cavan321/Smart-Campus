package com.example.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zhxy.pojo.Grade;

import java.util.List;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
public interface GradeService extends IService<Grade> {
    IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName);

    List<Grade> getGrades();
}
