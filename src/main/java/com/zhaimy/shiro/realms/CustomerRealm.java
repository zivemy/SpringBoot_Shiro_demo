package com.zhaimy.shiro.realms;

import com.zhaimy.dao.UserDao;
import com.zhaimy.entity.Pers;
import com.zhaimy.entity.Role;
import com.zhaimy.entity.User;
import com.zhaimy.service.UserService;
import com.zhaimy.shiro.salt.MyByteSource;
import com.zhaimy.utils.ApplicationContextUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 实现自定义realm
 */
public class CustomerRealm extends AuthorizingRealm {


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取身份信息
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        //根据主身份信息获取角色和权限信息,这里模拟查询数据库的操作
        //在工厂类中获取service对象
        UserService userService = (UserService) ApplicationContextUtils.getBean("userServiceImpl");

        //返回User对象，包含roles信息
        User user = userService.findRolesByUserName(primaryPrincipal);
        List<Role> roles = user.getRoles();


        if (!CollectionUtils.isEmpty(roles)){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            //给用户添加角色
            roles.forEach(role -> {
                simpleAuthorizationInfo.addRole(role.getName());

                //权限信息
                List<Pers> pers = userService.findPersByRoleId(role.getId());
                if (!CollectionUtils.isEmpty(pers)){
                    pers.forEach(pers1 -> {
                        simpleAuthorizationInfo.addStringPermission(pers1.getName());
                    });
                }


            });
            //simpleAuthorizationInfo.addStringPermission("user:*:*");
            return simpleAuthorizationInfo;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();

        //在工厂类中获取service对象
        UserService userService = (UserService) ApplicationContextUtils.getBean("userServiceImpl");

        User user = userService.findByUserName(principal);


        if (!ObjectUtils.isEmpty(user)){
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getUsername(),
                    user.getPassword(),
                    new MyByteSource(user.getSalt()),
                    this.getName());
            return simpleAuthenticationInfo;
        }
        return null;
    }
}
