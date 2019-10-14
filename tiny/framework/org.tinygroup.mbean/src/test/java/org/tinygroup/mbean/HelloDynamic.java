/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.mbean;

import javax.management.*;

public class HelloDynamic implements DynamicMBean {

    private String name = "我叫小明";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void printName() {
        System.out.println(name);
    }

    public Object getAttribute(String attribute)
            throws AttributeNotFoundException, MBeanException,
            ReflectionException {
        if (attribute == null) {
            throw new AttributeNotFoundException();
        }
        if ("name".equalsIgnoreCase(attribute)) {
            return getName();
        }
        throw new AttributeNotFoundException();
    }

    public void setAttribute(Attribute attribute)
            throws AttributeNotFoundException, InvalidAttributeValueException,
            MBeanException, ReflectionException {
        String name = attribute.getName();
        Object value = attribute.getValue();
        if ("name".equalsIgnoreCase(name)) {
            this.setName(String.valueOf(value));
            return;
        }
        throw new AttributeNotFoundException();
    }

    public AttributeList getAttributes(String[] attributes) {
        return null;
    }

    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    public Object invoke(String actionName, Object[] params, String[] signature)
            throws MBeanException, ReflectionException {
        if ("printName".equals(actionName)) {
            printName();
        }
        return null;
    }

    public MBeanInfo getMBeanInfo() {
        MBeanAttributeInfo[] dAttributes = new MBeanAttributeInfo[]{new MBeanAttributeInfo(
                "name", "String", "缓存名称", true, true, false)};
        MBeanOperationInfo opers[] = {new MBeanOperationInfo("printName",
                "print", null, "void", MBeanOperationInfo.ACTION)};

        MBeanInfo info = new MBeanInfo(this.getClass().getName(),
                "HelloDynamic", dAttributes, null, opers, null);
        return info;
    }
}
