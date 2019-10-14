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
package org.tinygroup.beancontext;

import org.tinygroup.context.Context;

public class BeanExceptionCatchedContextImpl extends BeanContextImpl {
    public BeanExceptionCatchedContextImpl(Context context) {
        super(context);
    }

    public <T> T get(String name) {
        if (context.exist(name)) {
            return (T) context.get(name);
        } else {
            try {
                // 如果没有，则返回null
                T t = (T) beanContainer.getBean(name);
                context.put(name, t);
                return t;
            } catch (Exception e) {
                return null;
            }

        }
    }
}
