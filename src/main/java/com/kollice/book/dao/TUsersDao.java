package com.kollice.book.dao;

import com.kollice.book.domain.TRoles;
import com.kollice.book.domain.TUsers;
import com.kollice.book.framework.base.CustomRepository;

import java.util.List;

/**
 * Created by 白建业 on 2016/10/9.
 */
public interface TUsersDao extends CustomRepository<TUsers, String> {
    List<TUsers> findByUsername(String username);
}
