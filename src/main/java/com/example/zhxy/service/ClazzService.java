package com.example.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zhxy.pojo.Clazz;

import java.util.List;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
public interface ClazzService extends IService<Clazz> {
    IPage<Clazz> getClazzsByOpr(Page<Clazz> pageParam, Clazz clazz);

    List<Clazz> getClazzs();
}
