package com.zhaimy.service.impl;

import com.zhaimy.dao.UserDao;
import com.zhaimy.entity.Pers;
import com.zhaimy.entity.Role;
import com.zhaimy.entity.User;
import com.zhaimy.service.UserService;
import com.zhaimy.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public void register(User user) {
        //处理业务
        //对用户传过来的明文密码进行md5+salt+hash散列
        //1.生成随机盐
        String salt = SaltUtils.getSalt(8);
        //2.将随机盐保存到数据库
        user.setSalt(salt);
        //根据明文密码进行md5+salt+hash散列
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
        user.setPassword(md5Hash.toHex());

        userDao.save(user);

    }

    @Override
    public User findByUserName(String username) {
        User user = userDao.findByUserName(username);
        return user;
    }

    @Override
    public User findRolesByUserName(String username) {
        return userDao.findRolesByUserName(username);
    }

    @Override
    public List<Pers> findPersByRoleId(String id) {
        return userDao.findPersByRoleId(id);
    }
}
