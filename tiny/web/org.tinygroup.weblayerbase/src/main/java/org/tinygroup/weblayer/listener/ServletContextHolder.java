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
package org.tinygroup.weblayer.listener;

import javax.servlet.ServletContext;

public class ServletContextHolder {

    private static ServletContext servletContext;

    public static ServletContext getServletContext() {
        ServletContext context = ServletContextHolder.servletContext;
        return context;
    }

    public static void setServletContext(ServletContext servletContext) {
        ServletContextHolder.servletContext = servletContext;
    }

    public static void clear() {
        if (servletContext != null && servletContext instanceof TinyServletContext) {
            ((TinyServletContext) servletContext).clear();
        }
        servletContext = null;
    }
}
