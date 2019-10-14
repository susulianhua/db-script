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
package org.tinygroup.springmvc.theme;

import org.springframework.web.servlet.ThemeResolver;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springmvc.extension.RequestInstanceHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TinyThemeResolver implements ThemeResolver {
    protected static final Logger logger = LoggerFactory
            .getLogger(TinyThemeResolver.class);

    public String resolveThemeName(HttpServletRequest request) {
        ThemeResolver themeResolver = RequestInstanceHolder
                .getMappingInstance().getCommonThemeResolver();
        if (themeResolver != null) {
            logger.logMessage(LogLevel.DEBUG,
                    " invoke themeResolver.resolveThemeName() method that will proxy ["
                            + themeResolver + "]");
            return themeResolver.resolveThemeName(request);
        }
        return null;
    }

    public void setThemeName(HttpServletRequest request,
                             HttpServletResponse response, String themeName) {
        ThemeResolver themeResolver = RequestInstanceHolder
                .getMappingInstance().getCommonThemeResolver();
        if (themeResolver != null) {
            logger.logMessage(LogLevel.DEBUG,
                    " invoke themeResolver.setThemeName() method that will proxy ["
                            + themeResolver + "]");
            themeResolver.setThemeName(request, response, themeName);
        }
    }

}
