package org.tinygroup.service.test.classpublisher.service;

import org.tinygroup.service.test.classpublisher.TestUser;

import java.util.List;

/**
 * Created by qiucn on 2018/3/19.
 */
public interface TestService {

    public String stringService(String name);

    public List<String> listService(List<String> list);

    public String[] arrayService(String[] strs);

    public TestUser userService(TestUser user);

    public List<TestUser> listUserService(List<TestUser> userList);

    public TestUser[] arrayUserService(TestUser[] users);
}
