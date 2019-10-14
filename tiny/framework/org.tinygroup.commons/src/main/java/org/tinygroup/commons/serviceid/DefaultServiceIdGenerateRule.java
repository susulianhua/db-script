package org.tinygroup.commons.serviceid;

import java.lang.reflect.Method;

public class DefaultServiceIdGenerateRule extends  ClassMethodServiceIdGenerateRule {
    @Override
    public String generateServiceId(Class type, Method method) {
        return method.getName()+computePostStr(type,method);
    }
}
