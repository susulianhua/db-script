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
package org.tinygroup.httpvisitor.client;

import java.io.IOException;

/**
 * 客户端实例管理器
 *
 * @author yancheng11334
 */
public interface ClientInstanceManager {

    public static final String DEFAULT_BEAN_NAME = "clientInstanceManager";

    void registerClient(String templateId, ClientInterface client);

    ClientInterface getClientInterface(String templateId);

    void closeClient(String templateId) throws IOException;

    void closeAllClients() throws IOException;
}
