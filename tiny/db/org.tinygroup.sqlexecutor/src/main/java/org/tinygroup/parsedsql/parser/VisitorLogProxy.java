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

import net.sf.cglib.proxy.Enhancer;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parsedsql.exception.ParsedSqlException;

/**
 * SQL解析日志打印.
 *
 * @author renhui
 */
public final class VisitorLogProxy {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(VisitorLogProxy.class);

    private VisitorLogProxy() {
        super();
    }

    /**
     * 打印SQL解析调用树.
     *
     * @param target
     *            待增强类
     * @param <T>
     *            泛型
     * @return 增强后的新类的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T enhance(final Class<T> target) {
        if (LOGGER.isEnabled(LogLevel.TRACE)) {
            Enhancer result = new Enhancer();
            result.setSuperclass(target);
            result.setCallback(new VisitorHandler());
            return (T) result.create();
        } else {
            try {
                return target.newInstance();
            } catch (final InstantiationException ex) {
                LOGGER.errorMessage("create Visitor exception", ex);
                throw new ParsedSqlException(ex);
            } catch (IllegalAccessException ex) {
                LOGGER.errorMessage("create Visitor exception", ex);
                throw new ParsedSqlException(ex);
            }
        }
    }

}
