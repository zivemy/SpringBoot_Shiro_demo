package com.zhaimy.config;

import com.zhaimy.shiro.cache.RedisCacheManager;
import com.zhaimy.shiro.realms.CustomerRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

/**
 * 用来整合Shiro框架相关的配置类
 */
@Configuration
public class ShiroConfig {
    //1.创建shiroFilter 负责拦截所有请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        Map<String,String> map = new HashMap<>();
        //配置系统公共资源
        map.put("/user/getImage","anon");
        map.put("/user/login","anon");
        map.put("/user/register","anon");
        map.put("/register.jsp","anon");
        //配置系统受限资源
        map.put("/**","authc");//authc 请求这个资源需要认证和授权
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        //默认认证界面路径,当访问到受限资源又没有认证时会自动跳到此页面，不配的话默认是login.jsp
        shiroFilterFactoryBean.setLoginUrl("/login.jsp");


        return shiroFilterFactoryBean;
    }

    //2.创建安全管理器 securityManager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        //给安全管理器设置realm
        defaultWebSecurityManager.setRealm(realm);
        return  defaultWebSecurityManager;
    }

    //3.创建自定义realm
    @Bean
    @Primary
    public Realm getRealm(){
        CustomerRealm customerRealm = new CustomerRealm();
        //修改默认的凭证校验匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为md5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(credentialsMatcher);

        //开启缓存管理
        customerRealm.setCacheManager(new RedisCacheManager());
        //开启全局的缓存
        customerRealm.setCachingEnabled(true);
        //开启认证缓存
        customerRealm.setAuthenticationCachingEnabled(true);
        //设置缓存名称
        customerRealm.setAuthenticationCacheName("authenticationCache");
        //开启授权缓存
        customerRealm.setAuthorizationCachingEnabled(true);
        //设置缓存名称
        customerRealm.setAuthorizationCacheName("authorizationCache");

        return customerRealm;
    }
}
