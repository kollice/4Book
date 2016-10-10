package com.kollice.book.service;

import com.kollice.book.dao.TPermissionDao;
import com.kollice.book.dao.TRolesDao;
import com.kollice.book.dao.TUsersDao;
import com.kollice.book.domain.TPermission;
import com.kollice.book.domain.TRoles;
import com.kollice.book.domain.TUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 00259 on 2016/10/10.
 */
@Service
@Transactional
public class LoginService {
    @Autowired
    TUsersDao tUsersDao;
    @Autowired
    TRolesDao tRolesDao;
    @Autowired
    TPermissionDao tPermissionDao;

    /**
     * 根据用户名取得用户对象
     * @param loginName
     * @return
     */
    public List<TUsers> findByUsername(String loginName) {
        return tUsersDao.findByUsername(loginName);
    }

    /**
     * 根据用户id取得角色对象列表
     * @param userId
     * @return
     */
    public List<TRoles> findRolesByUserId(String userId) {
        return  tRolesDao.findTRolesByUserid(userId);
    }

    /**
     * 根据用户对象取得角色对象列表
     * @param user
     * @return
     */
    public List<TRoles> findRolesByUser(TUsers user) {
        return findRolesByUserId(user.getId());
    }

    /**
     * 根据角色取得权限
     * @param role
     * @return
     */
    public List<TPermission> findPermissionByRole(TRoles role) {
        return findPermissionByRole(role.getId());
    }

    /**
     * 根据角色取得权限
     * @param roleId
     * @return
     */
    public List<TPermission> findPermissionByRole(String roleId) {
        return tPermissionDao.findPermissionByRoleId(roleId);
    }

}