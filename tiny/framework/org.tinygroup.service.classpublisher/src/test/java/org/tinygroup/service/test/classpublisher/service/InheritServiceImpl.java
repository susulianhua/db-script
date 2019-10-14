package org.tinygroup.service.test.classpublisher.service;

/**
 * Created by qiucn on 2018/3/20.
 */
public class InheritServiceImpl extends ParentServiceImpl implements InheritService {

    @Override
    public String childService(String name) {
        System.out.println("子类服务：" + name);
        return name;
    }
}
