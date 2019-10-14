package org.tinygroup.service.test.service.classpublish;

/**
 * Created by qiucn on 2018/3/20.
 */
public class SameNameServiceImpl implements SameNameService {
    @Override
    public void sameName() {
        System.out.println("void");
    }

    @Override
    public void sameName(String name) {
        System.out.println("void param");
    }
}
