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
package org.tinygroup.pageflowbasiccomponent;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.weblayer.WebContext;


/**
 * 功能说明:  对象session移除操作组件
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-5-21 <br>
 * <br>
 */
public class SessionRemoveOperate implements ComponentInterface {

    private String sessionKey;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }


    public void execute(Context context) {
        Assert.assertNotNull(sessionKey, "sessionKey must not null");
        WebContext webContext = (WebContext) context;
        webContext.getRequest().getSession(true).removeAttribute(sessionKey);
    }

}
