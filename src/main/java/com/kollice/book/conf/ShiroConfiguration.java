package com.kollice.book.conf;

import com.kollice.book.entity.BookShiroFilterFactoryBean;
import com.kollice.book.entity.BookShiroRealm;
import com.kollice.book.entity.CustomCredentialsMatcher;
import com.kollice.book.entity.CustomPermissionsAuthorizationFilter;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.*;

/**
 * Created by 白建业 on 2016/10/8.
 */
@Configuration
public class ShiroConfiguration{
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    @Bean
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }

    @Bean(name = "customCredentialsMatcher")
    public CustomCredentialsMatcher customCredentialsMatcher() {
        CustomCredentialsMatcher customCredentialsMatcher = new CustomCredentialsMatcher();
        customCredentialsMatcher.setHashAlgorithmName("md5");
        customCredentialsMatcher.setHashIterations(2);
        customCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return customCredentialsMatcher;
    }

    @Bean(name = "bookShiroRealm")
    public BookShiroRealm bookShiroRealm(CustomCredentialsMatcher customCredentialsMatcher) {
        BookShiroRealm realm = new BookShiroRealm();
        realm.setCacheManager(getEhCacheManager());
        realm.setCachingEnabled(true);
        realm.setCredentialsMatcher(customCredentialsMatcher);
        return realm;
    }

//    @Value("${spring.redis.host}")
//    private String host;
//
//    @Value("${spring.redis.port}")
//    private int port;

//    @Bean(name="redisCacheManager")
//    public RedisManager redisCacheManager( @SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
//        return new org.springframework.data.redis.cache.RedisCacheManager(redisTemplate);
//    }

//    @Bean(name = "redisManager")
//    public org.crazycake.shiro.RedisManager redisManager() {
//        RedisManager redisManager = new RedisManager();
//        return redisManager;
//    }
//
//    @Bean(name = "shiroCacheManager")
//    public CacheManager cacheManager(RedisManager redisManager) {
//        org.crazycake.shiro.RedisCacheManager cacheManager = new org.crazycake.shiro.RedisCacheManager();
//        cacheManager.setRedisManager(redisManager);
//        return cacheManager;
//    }
//
//    @Bean(name = "lifecycleBeanPostProcessor")
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    @Bean(name = "bookShiroRealm")
//    @DependsOn("lifecycleBeanPostProcessor")
//    public BookShiroRealm bookShiroRealm(CacheManager shiroCacheManager) {
//        BookShiroRealm realm = new BookShiroRealm();
//        realm.setCacheManager(shiroCacheManager);
//        realm.setCachingEnabled(true);
//        return realm;
//    }

    /**
     * 注册DelegatingFilterProxy（Shiro）
     * 集成Shiro有2种方法：
     * 1. 按这个方法自己组装一个FilterRegistrationBean（这种方法更为灵活，可以自己定义UrlPattern，
     * 在项目使用中你可能会因为一些很但疼的问题最后采用它， 想使用它你可能需要看官网或者已经很了解Shiro的处理原理了）
     * 2. 直接使用ShiroFilterFactoryBean（这种方法比较简单，其内部对ShiroFilter做了组装工作，无法自己定义UrlPattern，
     * 默认拦截 /*）
     *
     * @return
     * @author SHANHY
     * @create 2016年1月13日
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");// 可以自己灵活的定义很多，避免一些根本不需要被Shiro处理的请求被包含进来
        return filterRegistration;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean(name="authenticationStrategy")
    public AuthenticationStrategy authenticationStrategy() {
        return new FirstSuccessfulStrategy();
    }


    @Bean(name="modularRealmAuthenticator")
    public ModularRealmAuthenticator modularRealmAuthenticator(AuthenticationStrategy authenticationStrategy) {
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(authenticationStrategy);
        return modularRealmAuthenticator;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(BookShiroRealm bookShiroRealm,CacheManager shiroCacheManager, ModularRealmAuthenticator modularRealmAuthenticator) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setAuthenticator(modularRealmAuthenticator);
        dwsm.setCacheManager(getEhCacheManager());
//        dwsm.setCacheManager(shiroCacheManager);
        List<Realm> realms = new ArrayList<Realm>();
        realms.add(bookShiroRealm);
        dwsm.setRealms(realms);
//      <!-- 用户授权/认证信息Cache, 采用EhCache 缓存 -->
//        dwsm.setCacheManager(getEhCacheManager());
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }

    @Bean(name = "customPermissionsAuthorizationFilter")
    public CustomPermissionsAuthorizationFilter customPermissionsAuthorizationFilter() {
        return new CustomPermissionsAuthorizationFilter();
    }

    /**
     * 加载shiroFilter权限控制规则（从数据库读取然后配置）
     *
     * @author SHANHY
     * @create 2016年1月14日
     */
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        filterChainDefinitionMap.put("/manage/*", "user,customPermissionsAuthorizationFilter");// 这里为了测试，只限制/user，实际开发中请修改为具体拦截的请求规则
        // anon：它对应的过滤器里面是空的,什么都没做
        filterChainDefinitionMap.put("/manage/login", "anon");
        filterChainDefinitionMap.put("/api/*", "anon");
        filterChainDefinitionMap.put("/*", "anon");//anon 可以理解为不拦截
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

//        Map<String,Filter> filterChainDefinitionMap = new LinkedHashMap<String,Filter>();
//        filterChainDefinitionMap.put("/manage/*", getCustomPermissionsAuthorizationFilter());// 这里为了测试，只限制/user，实际开发中请修改为具体拦截的请求规则
//        // anon：它对应的过滤器里面是空的,什么都没做
//        filterChainDefinitionMap.put("/manage/login", "anon");
//        filterChainDefinitionMap.put("/*", "anon");//anon 可以理解为不拦截
//
//        shiroFilterFactoryBean.setFilters(filterChainDefinitionMap);
    }


    @Bean
    public CustomPermissionsAuthorizationFilter getCustomPermissionsAuthorizationFilter() {
        return new CustomPermissionsAuthorizationFilter();
    }


    /**
     * ShiroFilter<br/>
     * 注意这里参数中的 StudentService 和 IScoreDao 只是一个例子，因为我们在这里可以用这样的方式获取到相关访问数据库的对象，
     * 然后读取数据库相关配置，配置到 shiroFilterFactoryBean 的访问规则中。实际项目中，请使用自己的Service来处理业务逻辑。
     *
     * @param securityManager
     * @return
     * @author SHANHY
     * @create 2016年1月14日
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new BookShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/index");
        // 登录成功后要跳转的连接
        shiroFilterFactoryBean.setSuccessUrl("/main");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        loadShiroFilterChain(shiroFilterFactoryBean);

        Map<String, Filter> filters = new HashMap<>();
        filters.put("bookfilter", getCustomPermissionsAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filters);

        return shiroFilterFactoryBean;
    }

}
