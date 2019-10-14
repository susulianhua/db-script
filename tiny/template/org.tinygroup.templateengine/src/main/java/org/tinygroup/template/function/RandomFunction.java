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
package org.tinygroup.template.function;

import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;

import java.util.Random;
import java.util.UUID;

/**
 * 生成随机值的函数
 *
 * @author yancheng11334
 */
public class RandomFunction extends AbstractTemplateFunction {

    public RandomFunction() {
        super("rand,random");
    }

    public Object execute(Template template, TemplateContext context,
                          Object... parameters) throws TemplateException {
        if (parameters == null || parameters.length < 1) {
            return nextInt();
        } else {
            String type = (String) parameters[0];
            if (type == null || type.equalsIgnoreCase("int")) {
                return nextInt();
            } else if (type.equalsIgnoreCase("long")) {
                return nextLong();
            } else if (type.equalsIgnoreCase("uuid")) {
                return nextUUID();
            } else if (type.equalsIgnoreCase("double")) {
                return nextDouble();
            } else if (type.equalsIgnoreCase("float")) {
                return nextFloat();
            }
            //没有任何匹配返回Null值
            return null;
        }
    }

    /**
     * 生成int型随机数
     *
     * @return
     */
    private Integer nextInt() {
        Random r = new Random();
        return Math.abs(r.nextInt());
    }

    /**
     * 生成long型随机数
     *
     * @return
     */
    private Long nextLong() {
        Random r = new Random();
        return Math.abs(r.nextLong());
    }

    /**
     * 生成float型随机数
     *
     * @return
     */
    private Float nextFloat() {
        Random r = new Random();
        return Math.abs(r.nextFloat());
    }

    /**
     * 生成double型随机数
     *
     * @return
     */
    private Double nextDouble() {
        Random r = new Random();
        return Math.abs(r.nextDouble());
    }

    /**
     * 生成String型随机数
     *
     * @return
     */
    private String nextUUID() {
        return UUID.randomUUID().toString();
    }

}
