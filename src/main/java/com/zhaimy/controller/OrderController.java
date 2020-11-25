package com.zhaimy.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("order")
@Controller
public class OrderController {


    /**
     * 基于代码的权限判断
     * @return
     */
    @RequestMapping("save")
    public String save(){

        //获取主体对象
        Subject subject = SecurityUtils.getSubject();

        //代码判断是否据有权限
        if (subject.hasRole("admin")){
            System.out.println("保存订单");
        }else {
            System.out.println("没有操作权限");
        }

        //也可以基于权限字符串

        return "redirect:/index.jsp";
    }


    /**
     * 基于注解的权限判断，如果没有admin角色将不会执行此方法
     * @return
     */
    @RequestMapping("update")
    @RequiresRoles(value = {"admin"})
    public String update(){

        System.out.println("执行此方法说明有更改权限！");

        return "redirect:/index.jsp";
    }


    /**
     * 基于注解的权限字符串判断，如果没有admin角色将不会执行此方法
     * @return
     */
    @RequestMapping("delete")
    @RequiresPermissions("user:delete:*")
    public String delete(){

        System.out.println("执行此方法说明有删除权限！");

        return "redirect:/index.jsp";
    }



}
