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
package org.tinygroup.ratelimitweblayer;

import com.google.common.util.concurrent.RateLimiter;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;

import javax.servlet.ServletException;
import java.io.IOException;

public class RateLimitFilter extends AbstractTinyFilter {
    private final static String RATE_LIMIT = "rate_limit";
    private RateLimiter rateLimiter;

    public void preProcess(WebContext context) throws ServletException,
            IOException {
        if (rateLimiter != null && !rateLimiter.tryAcquire()) {
            String characterEncoding = context.getRequest().getCharacterEncoding();
            context.getResponse().getOutputStream().write("达到流量限制".getBytes(characterEncoding));
        }

    }

    public void postProcess(WebContext context) throws ServletException,
            IOException {
        // TODO Auto-generated method stub

    }

    protected void customInit() {
        String connLimitValue = get(RATE_LIMIT);
        if (StringUtil.isBlank(connLimitValue)) {
            return;
        }
        rateLimiter = RateLimiter.create(Double.parseDouble(connLimitValue));
    }

}
