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
package org.tinygroup.springmvc.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.webcontext.form.FormCheckStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 表单重复提交和CSRF检查
 *
 * @author renhui
 */
public class FormRepeatAndCsrfCheckInterceptor extends
        HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FormRepeatAndCsrfCheckInterceptor.class);
    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    private List<String> checkUrls = new ArrayList<String>();
    private FormCheckStrategy formCheckStrategy;

    /**
     * This implementation always returns <code>true</code>.
     */
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String servletPath = urlPathHelper.getLookupPathForRequest(request);
        // 当前URL是否需要检查
        if (isSkipped(servletPath)) {
            LOGGER.logMessage(LogLevel.DEBUG,
                    "当前URL不需要FormRepeatAndCsrfCheckInterceptor校验，跳出。URL={0}",
                    request.getRequestURL());
        } else {
            LOGGER.logMessage(LogLevel.DEBUG,
                    "当前URL需要FormRepeatAndCsrfCheckInterceptor校验，继续校验。URL={0}",
                    request.getRequestURL());
            formCheckStrategy.apply(request, false);
        }
        return true;
    }

    /**
     * 判断给定的URL是否需要进行检查
     */
    protected boolean isSkipped(String path) {

        for (String checkUrl : checkUrls) {
            // 判断当前url是否匹配配置文件中的url
            if (Pattern.matches(checkUrl, path)) {
                return false;
            }
        }
        return true;
    }

    public List<String> getCheckUrls() {
        return checkUrls;
    }

    public void setCheckUrls(List<String> checkUrls) {
        this.checkUrls = checkUrls;
    }

    public void setFormCheckStrategy(FormCheckStrategy formCheckStrategy) {
        this.formCheckStrategy = formCheckStrategy;
    }

}