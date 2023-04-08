package com.example.zhxy.pojo;

import lombok.Data;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
/*
*@Description: 用户登录表单信息
*@Author: Cavan
*@Date: 2023/3/16
*/
@Data
public class LoginForm {

    private String username;
    private String password;
    private String verifiCode;
    private Integer userType;

}
