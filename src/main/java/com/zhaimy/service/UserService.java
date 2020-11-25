package com.zhaimy.service;

import com.zhaimy.entity.Pers;
import com.zhaimy.entity.Role;
import com.zhaimy.entity.User;

import java.util.List;

public interface UserService {

    //注册方法的service接口
    void register(User user);
    //根据用户名查询业务的方法
    User findByUserName(String username);
    User findRolesByUserName(String username);
    List<Pers> findPersByRoleId(String id);
}
