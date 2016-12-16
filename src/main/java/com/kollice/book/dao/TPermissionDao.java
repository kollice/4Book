package com.kollice.book.dao;

import com.kollice.book.domain.TPermission;
import com.kollice.book.framework.base.CustomRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 00259 on 2016/10/9.
 */
public interface TPermissionDao extends CustomRepository<TPermission, String> {
    List<TPermission> findByPermissionname(String permissionname);

    @Query("select p from TPermission p , TPermissionRoles pr where p.id = pr.permissionid and pr.roleid= :roleId")
    List<TPermission> findPermissionByRoleId(String roleId);
}
