package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.zhxy.pojo.Admin;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.pojo.Student;
import com.example.zhxy.pojo.Teacher;
import com.example.zhxy.service.AdminService;
import com.example.zhxy.service.StudentService;
import com.example.zhxy.service.TeacherService;
import com.example.zhxy.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Cavan
 * @date 2023-03-16
 * @qq 2069543852
 */
@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    //POST    sms/system/updatePwd/123456/admin
    @ApiOperation("更新用户密码的处理器")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token口令") @RequestHeader("token") String token,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            //token过期
            return Result.fail().message("token失效，请重新登陆后修改密码");
        }
        //通过token获取用户类型
        Integer userType = JwtHelper.getUserType(token);
        //通过token获取当前用户id
        Long userId = JwtHelper.getUserId(token);
        //将传入的新旧密码转为密文
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);
        switch (userType){
            case 1:  //管理员类型
                QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id",userId.intValue()).eq("password",oldPwd);
                Admin admin = adminService.getOne(queryWrapper1);
                if(admin != null){
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else {
                    return Result.fail().message("用户名或密码错误");
                }
                break;

            case 2:  //学生类型
                QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue()).eq("password",oldPwd);
                Student student = studentService.getOne(queryWrapper2);
                if(student != null){
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else {
                    return Result.fail().message("用户名或密码错误");
                }
                break;

            case 3:  //教师类型
                QueryWrapper<Teacher> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("id",userId.intValue()).eq("password",oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper3);
                if(teacher != null){
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else {
                    return Result.fail().message("用户名或密码错误");
                }
                break;
        }
        return Result.ok();
    }


    //sms/system/headerImgUpload
    //头像上传
    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("头像文件") @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ) {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String newFileName = uuid + originalFilename.substring(i);
        //保存文件
        String portraitPath = "D:/code/atguigu/smart_campus/code/zhxy/target/classes/public/upload/" + newFileName;
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //响应图片路径
        String path = "upload/" + newFileName;
        return Result.ok(path);
    }

    @ApiOperation("通过token信息获取当前登录的用户信息的方法")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@ApiParam("token口令") @RequestHeader("token") String token) {
        //校验token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        //Token没过期时从token中解析出用户id和用户的类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String, Object> map = new LinkedHashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType", 1);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType", 2);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType", 3);
                map.put("user", teacher);
                break;
        }

        return Result.ok(map);
    }

    //校验登录是否成功
    @ApiOperation("校验登录是否成功")
    @PostMapping("/login")
    public Result login(
            @ApiParam("登录提交信息的form表单") @RequestBody LoginForm loginForm,
            HttpServletRequest request) {

        //验证码校验
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if ("".equals(sessionVerifiCode) || null == sessionVerifiCode) {
            return Result.fail().message("验证码已失效，请刷新后重试");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)) {
            return Result.fail().message("验证码有误，请重新输入");
        }
        //从session域中移除现有验证码
        session.removeAttribute("verifiCode");

        //用户类型校验
        //准备一个map用户存放响应的数据
        Map<String, Object> map = new LinkedHashMap<>();
        switch (loginForm.getUserType()) {
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null != admin) {
                        //用户的类型和用户的id转换成一个密文，以token的，名称向客户端反馈
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(), 1));
                    } else {
                        throw new RuntimeException("用户名或密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 2:
                try {
                    //查询数据库中是否有此学生
                    Student student = studentService.login(loginForm);
                    //如果有此学生
                    if (null != student) {
                        map.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
                    } else {
                        //如果没有此学生，则抛出一个运行时异常
                        throw new RuntimeException("用户名或密码错误");
                    }
                    //没有出现异常则返回Result类型的结果
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    //如果出现异常，则打印此异常，并返回Result类型失败的结果
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }


//                try {
//                    Student student = studentService.login(loginForm);
//                    if (null != student) {
//                        //用户的类型和用户的id转换成一个密文，以token的，名称向客户端反馈
//                        map.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
//                    } else {
//                        throw new RuntimeException("用户名或密码错误");
//                    }
//                    return Result.ok(map);
//                } catch (RuntimeException e) {
//                    e.printStackTrace();
//                    return Result.fail().message(e.getMessage());
//                }

            case 3:
                try {
                    //通过Service查询数据库中是否有此老师
                    Teacher teacher = teacherService.login(loginForm);
                    //如果数据库中有此老师，则向客户端反馈一个token
                    if (null != teacher) {
                        map.put("token", JwtHelper.createToken(teacher.getId().longValue(), 3));
                    } else { //如果查询不到此老师，则抛出一个运行时异常
                        throw new RuntimeException("用户名或密码错误");
                    }
                    //如果没有出现异常则返回一个Result类型的成功的结果
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    //若出现异常，打印异常信息，并返回一个Result类型的失败的结果
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());

                }

//                try {
//                    Teacher teacher = teacherService.login(loginForm);
//                    if (null != teacher) {
//                        //用户的类型和用户的id转换成一个密文，以token的，名称向客户端反馈
//                        map.put("token", JwtHelper.createToken(teacher.getId().longValue(), 3));
//                    } else {
//                        throw new RuntimeException("用户名或密码错误");
//                    }
//                    return Result.ok(map);
//                } catch (RuntimeException e) {
//                    e.printStackTrace();
//                    return Result.fail().message(e.getMessage());
//                }

        }
        //没有找到用户，返回一个失败的信息
        return Result.fail().message("查无此人");
    }



    //验证码
    @ApiOperation("获取图片验证码")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证文本放入session域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);
        //将验证码图片相应给浏览器
        try {
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
