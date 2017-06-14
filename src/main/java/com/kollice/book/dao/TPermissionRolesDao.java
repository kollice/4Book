package com.kollice.book.dao;

import com.kollice.book.domain.TPermissionRoles;
import com.kollice.book.framework.base.CustomRepository;

import java.util.List;

/**
 * Created by 白建业 on 2016/10/9.
 */
public interface TPermissionRolesDao extends CustomRepository<TPermissionRoles, String> {
    List<TPermissionRoles> findByPermissionid(String permissionid);
}

