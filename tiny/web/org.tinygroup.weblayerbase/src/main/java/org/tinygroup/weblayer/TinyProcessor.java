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
package org.tinygroup.weblayer;

import org.tinygroup.commons.order.Ordered;

import javax.servlet.ServletException;
import java.io.IOException;


/**
 * WebContext处理器，用于根据WebContext进行相关处理
 *
 * @author luoguo
 */
public interface TinyProcessor extends Ordered {

    String TINY_PROCESSOR = "tiny-processor";

    /**
     * 判断url是否匹配，如果匹配，返回True
     *
     * @param urlString 请求路径
     * @return
     */
    boolean isMatch(String urlString);

    /**
     * 处理方法
     *
     * @param urlString 请求路径
     * @param context   请求上下文
     */
    void process(String urlString, WebContext context) throws ServletException, IOException;

    /**
     * 初始化方法
     *
     * @param tinyProcessorConfig 处理器配置对象
     * @throws ServletException
     */
    void init(TinyProcessorConfig tinyProcessorConfig) throws ServletException;

    /**
     * 处理器的销毁方法
     */
    void destroy();

    /**
     * 获取处理器名称
     *
     * @return
     */
    String getProcessorName();

    /**
     * 设置处理器名称
     *
     * @param processorName 处理器名称
     */
    void setProcessorName(String processorName);
}
