package com.zhaimy.dao;

import com.zhaimy.entity.Pers;
import com.zhaimy.entity.Role;
import com.zhaimy.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface UserDao {

    void save(User user);
    User findByUserName(String username);
    User findRolesByUserName(String username);
    List<Pers> findPersByRoleId(String id);
}
