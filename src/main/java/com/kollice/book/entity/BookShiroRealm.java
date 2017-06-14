package com.kollice.book.entity;

import com.kollice.book.domain.TPermission;
import com.kollice.book.domain.TRoles;
import com.kollice.book.domain.TUsers;
import com.kollice.book.service.AuthorizationService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.CachingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 白建业 on 2016/10/8.
 */
public class BookShiroRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(BookShiroRealm.class);

    @Resource
    private AuthorizationService loginService;

    /**
     * 权限认证，为当前登录的Subject授予角色和权限
     * @see: 经测试：本例中该方法的调用时机为需授权资源被访问时
     * @see: 经测试：并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache
     * @see: 经测试：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("##################执行Shiro权限认证##################");
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            doClearCache(principalCollection);
            SecurityUtils.getSubject().logout();
            return null;
        }
//        TUsers user = (TUsers) getSession("currentUser");

        //获取当前登录输入的用户名，等价于(String) principalCollection.fromRealm(getName()).iterator().next();
        String loginName = (String)super.getAvailablePrincipal(principalCollection);
        //到数据库查是否有此对象
        List<TUsers> users=loginService.findByUsername(loginName);// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        if(users!=null && 1 == users.size()){
            //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            //用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
            List<TRoles> roleList=loginService.findRolesByUser(users.get(0));
            //用户的角色集合
            info.setRoles(roleList.stream().map(TRoles::getRolename).collect(Collectors.toSet()));
            for (TRoles role : roleList) {
                List<TPermission> permissionList = loginService.findPermissionByRole(role);
                info.addStringPermissions(permissionList.stream().map(TPermission::getUrl).collect(Collectors.toSet()));
            }
            // 或者按下面这样添加
            //添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色
//            simpleAuthorInfo.addRole("admin");
            //添加权限
//            simpleAuthorInfo.addStringPermission("admin:manage");
//            logger.info("已为用户[mike]赋予了[admin]角色和[admin:manage]权限");
            return info;
        }
        // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
        return null;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;

        logger.info("验证当前Subject时获取到token为：" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));

        //查出是否有此用户
        List<TUsers> users=loginService.findByUsername(token.getUsername());
        if(users!=null && 1 == users.size()){
            SimpleAuthenticationInfo result = new SimpleAuthenticationInfo(users.get(0).getUsername(), users.get(0).getPassword(), getName());
            // 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
            this.setSession("currentUser",users.get(0));
            return result;
        }
        return null;
    }

    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     * @see
     */
    private void setSession(Object key, Object value){
        Subject currentUser = SecurityUtils.getSubject();
        if(null != currentUser){
            Session session = currentUser.getSession();
            logger.info("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
            if(null != session){
                session.setAttribute(key, value);
            }
        }
    }


    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     * @see
     */
    private Object getSession(Object key){
        Subject currentUser = SecurityUtils.getSubject();
        if(null != currentUser){
            Session session = currentUser.getSession();
            logger.info("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
            if(null != session){
                return session.getAttribute(key);
            }
        }
        return null;
    }
}
