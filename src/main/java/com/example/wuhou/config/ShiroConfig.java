package com.example.wuhou.config;

import com.example.wuhou.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);
        //添加shiro的内置过滤器，拦截
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/webapp/pages/*", "authc");
        filterMap.put("/webapp/index.html", "authc");
        bean.setFilterChainDefinitionMap(filterMap);
        //设置登录失败的跳转页面
        bean.setLoginUrl("/webapp/login.html");


        return bean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //关联realm
        securityManager.setRealm(realm);


        return securityManager;
    }

    //创建realm对象，需要自定义类1
    @Bean
    public Realm getrRealm(){
        UserRealm userRealm = new UserRealm();
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数为1024
        credentialsMatcher.setHashIterations(5);
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return userRealm;
    }
}
