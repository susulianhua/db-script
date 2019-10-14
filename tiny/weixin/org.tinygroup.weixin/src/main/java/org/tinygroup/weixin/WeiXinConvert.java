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
 * 微信消息/结果转换统一接口
 *
 * @author yancheng11334
 */
public interface WeiXinConvert extends Comparable<WeiXinConvert> {

    /**
     * 获得优先级
     *
     * @return
     */
    int getPriority();

    /**
     * 设置优先级
     *
     * @param priority
     */
    void setPriority(int priority);

    /**
     * 获得报文的状态
     *
     * @return
     */
    WeiXinConvertMode getWeiXinConvertMode();

    /**
     * 获得结果类型
     *
     * @return
     */
    Class<?> getCalssType();

    /**
     * 判断转换接口能否处理输入信息（微信报文会出现不同类型报文字段一致的情况，需要根据上下文判断）
     *
     * @param <INPUT>
     * @param input
     * @param context
     * @return
     */
    <INPUT> boolean isMatch(INPUT input, WeiXinContext context);

    /**
     * 转换消息（微信报文会出现不同类型报文字段一致的情况，需要根据上下文判断）
     *
     * @param input
     * @return
     */
    <OUTPUT, INPUT> OUTPUT convert(INPUT input, WeiXinContext context);

}
