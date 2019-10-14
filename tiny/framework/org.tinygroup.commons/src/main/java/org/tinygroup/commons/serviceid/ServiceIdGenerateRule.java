package org.tinygroup.commons.serviceid;

import java.lang.reflect.Method;

public interface ServiceIdGenerateRule {

    String generateServiceId(Class type, Method method);

}
