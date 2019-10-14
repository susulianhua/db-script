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
package org.tinygroup.flow.test.newtestcase.simpleflow.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;

/**
 * 用于测试流程编排基础功能的组件
 *
 * @author zhangliang08072
 * @version $Id: SimpleFlowComponent.java, v 0.1 2016年4月28日 上午9:37:55 zhangliang08072 Exp $
 */
public class SimpleFlowComponent implements ComponentInterface {
    Integer age;
    String name;

    public void execute(Context context) {
        //此部分是用于测试组件参数赋值
        context.put("age", age);
        context.put("name", name);
        context.put("simpleflowresult", name + age + "岁");

        //此部分是用于测试流程循环
        Integer count = context.get("count");
        Integer sum = context.get("sum");
        if (count != null) {
            count++;
            sum++;
        }
        context.put("count", count);
        context.put("sum", sum);

    }

    /**
     * Getter method for property <tt>age</tt>.
     *
     * @return property value of age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Setter method for property <tt>age</tt>.
     *
     * @param age value to be assigned to property age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * Getter method for property <tt>name</tt>.
     *
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>name</tt>.
     *
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }
}
