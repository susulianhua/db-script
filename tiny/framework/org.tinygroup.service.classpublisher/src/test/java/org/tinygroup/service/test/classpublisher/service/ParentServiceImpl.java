package org.tinygroup.service.test.classpublisher.service;

/**
 * Created by qiucn on 2018/3/20.
 */
public class ParentServiceImpl implements ParaentService {
    private String consts;

    public String getConsts() {
        return consts;
    }

    public void setConsts(String consts) {
        this.consts = consts;
    }

    @Override
    public String paraent(String name) {
        System.out.println("父类服务:" + name);
        return name;
    }

}
