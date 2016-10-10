package com.kollice.book.dao;

import com.kollice.book.domain.TRoles;
import com.kollice.book.framework.base.CustomRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 00259 on 2016/10/9.
 */
public interface TRolesDao extends CustomRepository<TRoles, String> {
    @Query("select r from TRoles r join TUsersRoles ur where ur.userid= :userid")
    List<TRoles> findTRolesByUserid(String userid);
}
