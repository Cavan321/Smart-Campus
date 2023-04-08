package com.example.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zhxy.mapper.GradeMapper;
import com.example.zhxy.pojo.Grade;
import com.example.zhxy.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
@Service(value = "gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade>
        implements GradeService {

    //分页带条件查询
    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> pageParam, String gradeName) {

        //QueryWrapper其实可以理解成一个放查询条件的盒子，
        // 我们把查询条件放在里面，他就会自动按照对应的条件进行查询数据
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();

        //如果gradeName不为空，则进行模糊查询
        if(!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("name",gradeName);
        }
        //将查询结果按id降序排列
        queryWrapper.orderByDesc("id");

        Page<Grade> page = baseMapper.selectPage(pageParam, queryWrapper);

        return page;
    }

    @Override
    public List<Grade> getGrades() {
        return baseMapper.selectList(null);
    }
}
