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
package org.tinygroup.httpvisitor.struct;

import org.tinygroup.httpvisitor.Cookie;

import java.util.Date;

/**
 * 简易cookie值对象
 *
 * @author yancheng11334
 */
public class SimpleCookie implements Cookie {

    private String name;

    private String value;

    private Date expiryDate;

    private String path;

    private String domain;

    private boolean secure;

    public SimpleCookie() {
        super();
    }

    public SimpleCookie(String name, String value, Date expiryDate,
                        String path, String domain, boolean secure) {
        super();
        this.name = name;
        this.value = value;
        this.expiryDate = expiryDate;
        this.path = path;
        this.domain = domain;
        this.secure = secure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

}
