package org.tinygroup.service.test.service.classpublish;

import java.util.List;

public interface GenericTypeService {
    public List<GenericTypeService> returnService();
    public List<List<GenericTypeService>> returnService2();
    public List returnService3();
    public void paramService(List<GenericTypeService> list);
    public void paramService2(List<List<GenericTypeService>> list);
    public void paramService3(List list);
}
