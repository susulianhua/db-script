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
package org.tinygroup.parsedsql.parser;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.lang.reflect.Method;

public class VisitorHandler implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(VisitorHandler.class);

    private final StringBuilder hierarchyIndex = new StringBuilder();

    private Integer depth = 0;

    public Object intercept(final Object enhancedObject, final Method method,
                            final Object[] arguments, final MethodProxy methodProxy)
            throws Throwable {
        if (isPrintable(method)) {
            hierarchyIn();
            LOGGER.traceMessage("{0} visit node: {1}", hierarchyIndex,
                    arguments[0].getClass());
            LOGGER.traceMessage("{0} visit argument: {1}", hierarchyIndex,
                    arguments[0]);
        }
        Object result = methodProxy.invokeSuper(enhancedObject, arguments);
        if (isPrintable(method)) {
            SQLVisitor visitor = (SQLVisitor) enhancedObject;
            LOGGER.traceMessage("{0} endVisit node: {1}", hierarchyIndex,
                    arguments[0].getClass());
            LOGGER.traceMessage("{0} endVisit SQL: {1}", hierarchyIndex,
                    visitor.getSQLBuilder());
            hierarchyOut();
        }
        return result;
    }

    private boolean isPrintable(final Method method) {
        return LOGGER.isEnabled(LogLevel.TRACE)
                && "visit".equals(method.getName());
    }

    private void hierarchyIn() {
        hierarchyIndex.append("  ").append(++depth).append(" ");
    }

    private void hierarchyOut() {
        hierarchyIndex.delete(hierarchyIndex.length() - 3
                - depth.toString().length(), hierarchyIndex.length());
        depth--;
    }
}