package com.kollice.book.service;

import com.kollice.book.dao.TPermissionDao;
import com.kollice.book.domain.TPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by 00259 on 2016/10/12.
 */
@Service
@Transactional
public class PermissionService {
    @Autowired
    TPermissionDao tPermissionDao;



}
