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
package org.tinygroup.flow.util;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.beancontext.BeanExceptionCatchedContextImpl;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.el.EL;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class FlowElUtil {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FlowElUtil.class);

    private static Context utilContext;
    private static String UTIL_MAP = "tiny_util_map";

    static {
        utilContext = ContextFactory.getContext();
        utilContext.put("StringUtil", StringUtil.class);
    }

    public static boolean executeCondition(String condition, Context context,
                                           ClassLoader loader) {
        EL el = BeanContainerFactory.getBeanContainer(loader).getBean(
                EL.EL_BEAN);
        BeanExceptionCatchedContextImpl beanContext = null;
        if (context instanceof BeanExceptionCatchedContextImpl) {
            beanContext = (BeanExceptionCatchedContextImpl) context;
        } else {
            beanContext = new BeanExceptionCatchedContextImpl(context);
        }
        beanContext.putSubContext(UTIL_MAP, utilContext);
        boolean result = (Boolean) el.execute(condition, beanContext);
        beanContext.removeSubContext(UTIL_MAP);
        return result;
    }

    public static Object execute(String expression, Context context,
                                 ClassLoader loader) {
        BeanExceptionCatchedContextImpl beanContext = null;
        if (context instanceof BeanExceptionCatchedContextImpl) {
            beanContext = (BeanExceptionCatchedContextImpl) context;
        } else {
            beanContext = new BeanExceptionCatchedContextImpl(context);
        }
        try {
            beanContext.putSubContext(UTIL_MAP, utilContext);
            EL el = BeanContainerFactory.getBeanContainer(loader).getBean(
                    EL.EL_BEAN);
            Object result = el.execute(expression, beanContext);
            beanContext.removeSubContext(UTIL_MAP);
            return result;
        } catch (Exception e) {
            LOGGER.errorMessage("执行el表达式时出错", e, expression);
            return null;
        }
    }

    public static Object executeNotCatchException(String expression,
                                                  Context context, ClassLoader loader) {
        BeanExceptionCatchedContextImpl beanContext = null;
        if (context instanceof BeanExceptionCatchedContextImpl) {
            beanContext = (BeanExceptionCatchedContextImpl) context;
        } else {
            beanContext = new BeanExceptionCatchedContextImpl(context);
        }
        beanContext.putSubContext(UTIL_MAP, utilContext);
        EL el = BeanContainerFactory.getBeanContainer(loader).getBean(
                EL.EL_BEAN);
        Object result = el.execute(expression, beanContext);
        beanContext.removeSubContext(UTIL_MAP);
        return result;
    }

    public static void putUtilClass(String name, Class<?> clazz) {
        utilContext.put(name, clazz);
    }


}
