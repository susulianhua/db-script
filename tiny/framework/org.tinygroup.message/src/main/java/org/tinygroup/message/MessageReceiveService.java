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
package org.tinygroup.message;

import java.util.Collection;

/**
 * 信息服务，用于定义信息的发送与接收接口
 * Created by luoguo on 2014/4/17.
 */
public interface MessageReceiveService<ACCOUNT extends MessageAccount, MSG extends Message> {

    /**
     * 返回接收的消息
     *
     * @return
     * @throws MessageException
     */
    Collection<MSG> getMessages(ACCOUNT messageAccount) throws MessageException;


}
