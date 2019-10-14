package org.tinygroup.service.test.service.classpublish;

import java.util.List;

public class GenericTypeServiceImpl implements  GenericTypeService {
    @Override
    public List<GenericTypeService> returnService() {
        return null;
    }

    @Override
    public List<List<GenericTypeService>> returnService2() {
        return null;
    }

    @Override
    public List returnService3() {
        return null;
    }

    @Override
    public void paramService(List<GenericTypeService> list) {

    }

    @Override
    public void paramService2(List<List<GenericTypeService>> list) {

    }

    @Override
    public void paramService3(List list) {

    }
}
