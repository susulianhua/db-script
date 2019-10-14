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
package org.tinygroup.templateweb;

import org.tinygroup.commons.tools.ObjectUtil;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.template.TemplateContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * request包装的上下文对象
 *
 * @author renhui
 */
public class RequestTemplateContext extends ContextImpl implements
        TemplateContext {

    private transient HttpServletRequest request;

    public RequestTemplateContext(HttpServletRequest request) {
        this.request = request;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        T value = super.get(name);
        if (value != null) {
            return value;
        }
        return (T) findInRequset(name);
    }

    @SuppressWarnings("unchecked")
    private <T> T findInRequset(String name) {
        if (request != null) {
            T result = (T) request.getAttribute(name);
            if (!ObjectUtil.isEmptyObject(result))
                return result;
            result = (T) request.getParameterValues(name);
            if (!ObjectUtil.isEmptyObject(result)) {
                if (result.getClass().isArray()) {// 处理字符串数组的问题
                    Object[] temp = (Object[]) result;
                    if (temp.length == 1) {
                        result = (T) temp[0];
                    }
                }
            }
            if (!ObjectUtil.isEmptyObject(result))
                return result;

            result = (T) request.getSession().getAttribute(name);
            if (!ObjectUtil.isEmptyObject(result))
                return result;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(name)) {
                        result = (T) cookie.getValue();
                        return result;
                    }
                }
            }
            result = (T) request.getHeader(name);
            if (!ObjectUtil.isEmptyObject(result)) {
                return result;
            }
        }
        return null;
    }

    public boolean exist(String name) {
        boolean exist = super.exist(name);
        if (exist) {
            return true;
        }
        return existInRequset(name);
    }

    @SuppressWarnings("unchecked")
    private boolean existInRequset(String name) {
        if (request != null) {
            Enumeration<String> enumer = request.getAttributeNames();
            while (enumer.hasMoreElements()) {
                if (enumer.nextElement().equals(name)) {
                    return true;
                }
            }
            Map parameterMap = request.getParameterMap();
            if (parameterMap.containsKey(name)) {
                return true;
            }
            enumer = request.getSession().getAttributeNames();
            while (enumer.hasMoreElements()) {
                if (enumer.nextElement().equals(name)) {
                    return true;
                }
            }
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(name)) {
                        return true;
                    }
                }
            }
            enumer = request.getHeaderNames();
            while (enumer.hasMoreElements()) {
                if (enumer.nextElement().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

}
