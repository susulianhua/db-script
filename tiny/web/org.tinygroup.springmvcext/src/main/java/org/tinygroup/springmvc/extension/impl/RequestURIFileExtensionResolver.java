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
package org.tinygroup.springmvc.extension.impl;

import org.springframework.web.util.UrlPathHelper;
import org.tinygroup.springmvc.extension.FileExtensionResolver;
import org.tinygroup.springmvc.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * @author renhui
 */
public class RequestURIFileExtensionResolver implements
        FileExtensionResolver<HttpServletRequest> {

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    public UrlPathHelper getUrlPathHelper() {
        return urlPathHelper;
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    public List<String> resolveFileExtensions(HttpServletRequest request) {
        UrlPathHelper urlPathHelper = getUrlPathHelper();
        String path = urlPathHelper.getLookupPathForRequest(request);
        String targetExt = WebUtil.getExtension(path);
        if (null == targetExt) {
            return null;
        }
        return Collections.singletonList(targetExt);
    }

    public boolean isSupport(HttpServletRequest t) {
        return t instanceof HttpServletRequest;
    }
}
