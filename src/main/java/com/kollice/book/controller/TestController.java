package com.kollice.book.controller;

import com.kollice.book.dao.PersonDao;
import com.kollice.book.dao.TPermissionDao;
import com.kollice.book.dao.TPermissionRolesDao;
import com.kollice.book.dao.TRolesDao;
import com.kollice.book.domain.Person;
import com.kollice.book.domain.TPermission;
import com.kollice.book.domain.TPermissionRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 白建业 on 2016/10/9.
 */
@RestController
public class TestController {
    //1 Spring Data JPA已自动为你注册bean，所以可自动注入
    @Autowired
    PersonDao personDao;

    @Autowired
    TPermissionDao tPermissionDao;

    @Autowired
    TPermissionRolesDao tPermissionRolesDao;


    @Autowired
    TRolesDao tRolesDao;

    /**
     * 保存
     * save支持批量保存：<S extends T> Iterable<S> save(Iterable<S> entities);
     *
     * 删除：
     * 删除支持使用id，对象以，批量删除及删除全部：
     * void delete(ID id);
     * void delete(T entity);
     * void delete(Iterable<? extends T> entities);
     * void deleteAll();
     *
     */
    @RequestMapping("/save")
    public Person save(String name,String address,Integer age){

        Person p = personDao.save(new Person(null, name, age, address));

        return p;

    }

    @RequestMapping("/savePermission")
    public TPermission savePermission(String permissionname,String url){
        TPermission p = tPermissionDao.save(new TPermission(null,permissionname,url));
        return p;
    }

    @RequestMapping("/queryPermission")
    public List<TPermission> queryPermission(String permissionname){
        List<TPermission> result = tPermissionDao.findByPermissionname(permissionname);
        return result;
    }

    @RequestMapping("/savePermissionRoles")
    public TPermissionRoles savePermissionRoles(String permissionid,String roleid){
        TPermissionRoles p = tPermissionRolesDao.save(new TPermissionRoles(null,permissionid,roleid));
        return p;
    }

    @RequestMapping("/queryPermissionRoles")
    public List<TPermissionRoles> queryPermissionRoles(String permissionid){
        List<TPermissionRoles> result = tPermissionRolesDao.findByPermissionid(permissionid);
        return result;
    }



    /**
     * 测试findByAddress
     */
    @RequestMapping("/q1")
    public List<Person> q1(String address){

        List<Person> people = personDao.findByAddress(address);

        return people;

    }

    /**
     * 测试findByNameAndAddress
     */
    @RequestMapping("/q2")
    public Person q2(String name,String address){

        Person people = personDao.findByNameAndAddress(name, address);

        return people;

    }

    /**
     * 测试withNameAndAddressQuery
     */
    @RequestMapping("/q3")
    public Person q3(String name,String address){

        Person p = personDao.withNameAndAddressQuery(name, address);

        return p;

    }

    /**
     * 测试withNameAndAddressNamedQuery
     */
    @RequestMapping("/q4")
    public Person q4(String name,String address){

        Person p = personDao.withNameAndAddressNamedQuery(name, address);

        return p;

    }

    /**
     * 测试排序
     */
    @RequestMapping("/sort")
    public List<Person> sort(){

        List<Person> people = personDao.findAll(new Sort(Sort.Direction.ASC,"age"));

        return people;

    }

    /**
     * 测试分页
     */
    @RequestMapping("/page")
    public Page<Person> page(){

        Page<Person> pagePeople = personDao.findAll(new PageRequest(1, 2));

        return pagePeople;

    }


    @RequestMapping("/auto")
    public Page<Person> auto(Person person){

        Page<Person> pagePeople = personDao.findByAuto(person, new PageRequest(0, 10));

        return pagePeople;

    }

}
