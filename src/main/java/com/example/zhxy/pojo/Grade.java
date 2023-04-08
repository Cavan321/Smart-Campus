package com.example.zhxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_grade")
public class Grade {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private String manager;
    private String email;
    private String telephone;
    private String introducation;

}
