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

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.WebContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能说明: 请求转发组件<br>
 * 开发时间: 2013-5-2 <br>
 * 功能描述: 用于Forward到指定的URL<br>
 */
public class Forward implements ComponentInterface {
    private static final Logger logger = LoggerFactory.getLogger(Forward.class);
    String path;// 要转到的页面

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void execute(Context context) {
        try {
            WebContext webContext = (WebContext) context;
            HttpServletRequest request = webContext.getRequest();
            request.getRequestDispatcher(path).forward(request,
                    webContext.getResponse());
        } catch (Exception e) {
            logger.errorMessage("Forward到地址[{}]出错，错误原因：{}", e, path,
                    e.getMessage());
        }
    }

}
