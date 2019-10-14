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
package org.tinygroup.fulltext;

/**
 * 全文检索静态配置的管理器接口
 *
 * @param <T>
 * @author yancheng11334
 */
public interface FullTextConfigManager<T> {

    /**
     * application.xml的全局配置参数名:<br>
     * 参数值:全文检索的配置管理器bean名称
     */
    public static final String FULLTEXT_CONFIG_MANAGER = "FULLTEXT_CONFIG_MANAGER";

    /**
     * application.xml的全局配置参数名:<br>
     * 参数值:全文检索的配置唯一ID
     */
    public static final String FULLTEXT_CONFIG_ID = "FULLTEXT_CONFIG_ID";

    /**
     * 添加单条配置
     *
     * @param config
     */
    public void addFullTextConfig(T config);

    /**
     * 删除单条配置
     *
     * @param config
     */
    public void removeFullTextConfig(T config);

    /**
     * 得到配置信息
     *
     * @param configId
     * @return
     */
    public T getFullTextConfig(String configId);

    /**
     * 得到默认的配置信息
     *
     * @return
     */
    public T getFullTextConfig();
}
