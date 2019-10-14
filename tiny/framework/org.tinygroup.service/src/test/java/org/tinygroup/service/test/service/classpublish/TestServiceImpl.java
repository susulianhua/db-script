package org.tinygroup.service.test.service.classpublish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiucn on 2018/3/19.
 */
public class TestServiceImpl implements TestService {


    @Override
    public String stringService(String name) {
        System.out.println("hello :" + name);
        return name;
    }

    @Override
    public List<String> listService(List<String> list) {
        System.out.println("hello:" + list.size());
        list.add("张三");
        list.add("李四");
        return list;
    }

    @Override
    public String[] arrayService(String[] strs) {
        return strs;
    }

    @Override
    public TestUser userService(TestUser user) {
        System.out.println(user.getName());
        user.setName("张三");
        user.setAge(18);
        return user;
    }

    @Override
    public List<TestUser> listUserService(List<TestUser> userList) {
        System.out.println(userList.size());
        List<TestUser> list = new ArrayList<TestUser>();
        TestUser user = new TestUser();
        user.setName("张三");
        user.setAge(18);
        list.add(user);
        return list;
    }

    @Override
    public TestUser[] arrayUserService(TestUser[] users) {
        System.out.println(users.length);
        return users;
    }
}
