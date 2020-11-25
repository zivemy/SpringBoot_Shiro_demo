package com.zhaimy.controller;

import com.zhaimy.entity.User;
import com.zhaimy.service.UserService;
import com.zhaimy.utils.ValidateImageCodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 生成验证码的方法
     */
    @RequestMapping("getImage")
    public void getImage(HttpSession session, HttpServletResponse response) throws IOException {
        //生成验证码
        String securityCode = ValidateImageCodeUtils.getSecurityCode();
        BufferedImage image = ValidateImageCodeUtils.createImage(securityCode);
        session.setAttribute("code",securityCode);//存入session作用域中
        //响应图片
        ServletOutputStream os = response.getOutputStream();
        ImageIO.write(image,"png",os);
    }

    /**
     * 是先注册功能
     * @param user
     * @return
     */
    @RequestMapping("/register")
    public String register(User user){
        try{
            userService.register(user);
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/register.jsp";
        }
        return "redirect:/login.jsp";
    }


    /**
     * 实现退出
     * @return
     */
    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();//退出登录
        return "redirect:/login.jsp";

    }

    /**
     * 用来处理身份认证
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public String login(String username,String password,String code,HttpSession session ){

        //比较验证码
        String codes = (String) session.getAttribute("code");
        try {
        if (codes.equalsIgnoreCase(code)){
            //获取主体对象
            Subject subject = SecurityUtils.getSubject();
            //创建令牌
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);


                subject.login(token);
                return "redirect:/index.jsp";


        }else{
            throw new RuntimeException("验证码错误");
        }
        }catch (UnknownAccountException e){
            e.printStackTrace();
            System.out.println("用户名错误");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("密码错误");
        }catch (RuntimeException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
            return "redirect:/login.jsp";
        }


}
