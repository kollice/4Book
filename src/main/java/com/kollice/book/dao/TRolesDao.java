package com.kollice.book.dao;

import com.kollice.book.domain.TRoles;
import com.kollice.book.framework.base.CustomRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 白建业 on 2016/10/9.
 */
public interface TRolesDao extends CustomRepository<TRoles, String> {
    @Query("select r from TRoles r, TUsersRoles ur where r.id = ur.roleid and ur.userid= :userid")
    List<TRoles> findTRolesByUserid(@Param("userid")String userid);
}
