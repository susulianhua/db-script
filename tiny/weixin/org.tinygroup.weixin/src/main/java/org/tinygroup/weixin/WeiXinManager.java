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

import org.tinygroup.weixin.common.UrlConfig;
import org.tinygroup.weixin.common.UrlConfigs;

/**
 * 微信配置管理
 *
 * @author yancheng11334
 */
public interface WeiXinManager {

    /**
     * 默认的bean配置名称
     */
    public static final String DEFAULT_BEAN_NAME = "weiXinManager";

    /**
     * 返回指定key对应的URL
     *
     * @param key
     * @return
     */
    UrlConfig getUrl(String key);

    /**
     * 添加一条配置
     *
     * @param urlConfig
     */
    void addUrlConfig(UrlConfig urlConfig);

    /**
     * 添加一组配置
     *
     * @param urlConfigs
     */
    void addUrlConfigs(UrlConfigs urlConfigs);

    /**
     * 移除一条配置
     *
     * @param urlConfig
     */
    void removeUrlConfig(UrlConfig urlConfig);

    /**
     * 移除一组配置
     *
     * @param urlConfigs
     */
    void removeUrlConfigs(UrlConfigs urlConfigs);

    /**
     * 渲染URL资源
     *
     * @param key
     * @param context
     * @return
     */
    String renderUrl(String key, WeiXinContext context);
}
