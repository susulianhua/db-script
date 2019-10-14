package org.tinygroup.service.test.service.classpublish;

import java.io.Serializable;

/**
 * Created by qiucn on 2018/3/20.
 */
public class TestUser implements Serializable{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
