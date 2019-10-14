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
package org.tinygroup.weblayer.webcontext.session.store;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.commons.tools.ToStringBuilder;
import org.tinygroup.commons.tools.ToStringBuilder.MapBuilder;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.webcontext.session.SessionConfig;
import org.tinygroup.weblayer.webcontext.session.SessionConfig.CookieConfig;
import org.tinygroup.weblayer.webcontext.util.CookieSupport;

import javax.servlet.http.HttpServletResponse;

import static org.tinygroup.commons.tools.Assert.assertNotNull;
import static org.tinygroup.commons.tools.ObjectUtil.defaultIfNull;
import static org.tinygroup.commons.tools.StringUtil.defaultIfEmpty;
import static org.tinygroup.commons.tools.StringUtil.isEmpty;

/**
 * 抽象的cookie store实现。
 *
 * @author Michael Zhou
 */
public abstract class AbstractCookieStore implements CookieStore {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private String storeName;
    private CookieConfig idCookieConfig;
    private String name;
    private String domain;
    private String path;
    private Integer maxAge;
    private Boolean httpOnly;
    private Boolean secure;
    private Boolean survivesInInvalidating;

    public String getStoreName() {
        return storeName;
    }

    public CookieConfig getIdCookieConfig() {
        return idCookieConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public boolean getSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isSurvivesInInvalidating() {
        return survivesInInvalidating;
    }

    public void setSurvivesInInvalidating(boolean survivesInInvalidating) {
        this.survivesInInvalidating = survivesInInvalidating;
    }

    /**
     * 初始化cookie store。
     */
    public final void init(String storeName, SessionConfig sessionConfig) throws Exception {
        this.idCookieConfig = assertNotNull(sessionConfig, "sessionConfig").getId().getCookie();
        this.storeName = storeName;
        name = defaultIfEmpty(name, idCookieConfig.getName());
        if (isEmpty(name)) {
            throw new IllegalArgumentException("missing cookie name for store: " + storeName);
        }

        domain = defaultIfEmpty(domain, idCookieConfig.getDomain());
        path = defaultIfEmpty(path, idCookieConfig.getPath());
        maxAge = defaultIfNull(maxAge, idCookieConfig.getMaxAge());
        httpOnly = defaultIfNull(httpOnly, idCookieConfig.isHttpOnly());
        secure = defaultIfNull(secure, idCookieConfig.isSecure());
        survivesInInvalidating = defaultIfNull(survivesInInvalidating, SURVIVES_IN_INVALIDATING_DEFAULT);

        if (survivesInInvalidating && maxAge <= 0) {
            throw new IllegalArgumentException(
                    "Cookie store which Survives In Invalidating must specify MaxAge of cookie");
        }

        init();
    }

    /**
     * 初始化cookie store。
     */
    protected void init() throws Exception {
    }

    /**
     * 向response中写入cookie。
     */
    protected void writeCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        CookieSupport cookie = new CookieSupport(cookieName, cookieValue);

        if (!StringUtil.isEmpty(domain)) {
            cookie.setDomain(domain);
        }

        if (!StringUtil.isEmpty(path)) {
            cookie.setPath(path);
        }

        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }

        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);

        log.logMessage(LogLevel.DEBUG, "{}", cookie);

        cookie.addCookie(response);
    }


    public final String toString() {
        MapBuilder mb = new MapBuilder();

        toString(mb);

        return new ToStringBuilder().append("CookieStore").append(mb).toString();
    }

    protected void toString(MapBuilder mb) {
        mb.append("name", name);
        mb.append("domain", domain);
        mb.append("path", path);
        mb.append("maxAge", String.format("%,d seconds", maxAge));
        mb.append("httpOnly", httpOnly);
        mb.append("secure", secure);
        mb.append("survivesInInvalidating", survivesInInvalidating);
    }
}
