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
package org.tinygroup.webservice.instanceresolver;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.InstanceResolver;
import com.sun.xml.ws.api.server.Invoker;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.api.server.WSWebServiceContext;
import org.tinygroup.cepcore.util.CEPCoreExecuteUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.webservice.util.WebserviceUtil;

import javax.xml.ws.Provider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 执行tiny服务的实例化解析器
 *
 * @param <T>
 * @author renhui
 */
public class TinyInstanceResolver<T> extends InstanceResolver<T> {

    private static Logger logger = LoggerFactory
            .getLogger(TinyInstanceResolver.class);

    public TinyInstanceResolver(Class<T> clazz) {
    }

    @Override
    public T resolve(Packet request) {
        return null;
    }

    @Override
    public Invoker createInvoker() {
        return new Invoker() {
            @Override
            public void start(@NotNull WSWebServiceContext wsc,
                              @NotNull WSEndpoint endpoint) {
                TinyInstanceResolver.this.start(wsc, endpoint);
            }

            @Override
            public void dispose() {
                TinyInstanceResolver.this.dispose();
            }

            @Override
            public Object invoke(Packet p, Method m, Object... args)
                    throws InvocationTargetException, IllegalAccessException {
                logger.logMessage(LogLevel.DEBUG, "webservice开始执行 ,方法名：{0}",
                        m.getName());
                if (args.length == 0) {
                    logger.logMessage(LogLevel.DEBUG, "无参数");
                } else {
                    logger.logMessage(LogLevel.DEBUG, "执行参数:");
                    for (Object arg : args) {
                        logger.logMessage(LogLevel.DEBUG, arg + "");
                    }
                }
                String serviceId = WebserviceUtil.getServiceIdByMethodName(m.getName());
                if (StringUtil.isBlank(serviceId)) {
                    throw new RuntimeException("方法名:" + m.getName() + "未发布成服务");
                }

                Object ret = CEPCoreExecuteUtil.execute(serviceId, args, this
                        .getClass().getClassLoader());
                // owner.getInvoker(req).invoke(req, method, args);
                logger.logMessage(LogLevel.DEBUG,
                        "webservice执行完毕,方法名：{0},执行结果:{1}", m.getName(), ret);
                return ret;
            }

            @Override
            public <U> U invokeProvider(@NotNull Packet p, U arg) {
                T t = resolve(p);
                try {
                    return ((Provider<U>) t).invoke(arg);
                } finally {
                    postInvoke(p, t);
                }
            }

            public String toString() {
                return "Default Invoker over "
                        + TinyInstanceResolver.this.toString();
            }
        };
    }

}
