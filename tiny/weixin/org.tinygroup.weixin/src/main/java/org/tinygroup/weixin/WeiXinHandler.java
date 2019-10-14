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
package org.tinygroup.weixin;


/**
 * 微信业务处理器
 *
 * @author yancheng11334
 */
public interface WeiXinHandler extends Comparable<WeiXinHandler> {

    int getPriority();

    void setPriority(int priority);

    WeiXinHandlerMode getWeiXinHandlerMode();

    /**
     * 是否匹配对象和上下文
     *
     * @param <T>
     * @param message
     * @return
     */
    <T> boolean isMatch(T message, WeiXinContext context);


    /**
     * 处理对象
     *
     * @param <T>
     * @param message
     * @param context
     */
    <T> void process(T message, WeiXinContext context);
}
