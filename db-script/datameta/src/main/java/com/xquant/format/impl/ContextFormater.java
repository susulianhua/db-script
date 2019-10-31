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
package com.xquant.format.impl;

import com.xquant.common.context.Context;
import com.xquant.format.FormatProvider;
import com.xquant.format.exception.FormatException;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 上下文格式化提供者<br>
 * 支持Bean的读法，即属下下去的方式
 *
 * @author luoguo
 */
public class ContextFormater implements FormatProvider {

    public String format(Context context, String string) throws FormatException {
        Object obj = context.get(string);
        if (obj != null) {
            return obj.toString();
        }
        int index = string.indexOf('.');
        if (index > 0) {
            String name = string.substring(0, index);
            obj = context.get(name);
            if (obj != null) {
                String property = string.substring(index + 1);
                try {
                    return BeanUtils.getProperty(obj, property).toString();
                } catch (Exception e) {
                    throw new FormatException(e);
                }
            }
        }
        return null;
    }
}
