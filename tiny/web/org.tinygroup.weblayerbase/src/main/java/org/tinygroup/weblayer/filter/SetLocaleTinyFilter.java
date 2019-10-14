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
package org.tinygroup.weblayer.filter;

import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.setlocacle.impl.SetLocaleOverrider;
import org.tinygroup.weblayer.webcontext.setlocacle.impl.SetLocaleWebContextImpl;
import org.tinygroup.xmlparser.node.XmlNode;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static org.tinygroup.commons.tools.StringUtil.defaultIfEmpty;
import static org.tinygroup.weblayer.webcontext.setlocacle.SetLocaleWebContext.*;

/**
 * 设置当前请求的区域（locale）、编码字符集（charset）。
 *
 * @author renhui
 */
public class SetLocaleTinyFilter extends AbstractTinyFilter {

    private static final String DEFAULT_LOCALE = "defaultLocale";
    private static final String DEFAULT_CHARSET = "defaultCharset";
    private static final String INPUT_CHARSET_PARAM = "inputCharsetParam";
    private static final String OUTPUT_CHARSET_PARAM = "outputCharsetParam";
    private static final String PARAM_KEY = "paramKey";
    private static final String SESSION_KEY = "sessionKey";
    private static final String REQUEST_URI_KEY = "url-pattern";
    private static final String INPUT_CHARSET = "input-charset";
    private static final String OUTPUT_CHARSET = "output-charset";
    private static final String SET_LOCALE_CONFIG = "set-locale";
    private static final String OVERRIDER = "overrider";
    private Pattern inputCharsetPattern;
    private Pattern outputCharsetPattern;
    private SetLocaleOverrider[] overriders;
    private Locale defaultLocale;
    private String defaultCharset;
    private String sessionKey;
    private String paramKey;

    @Override
    protected void customInit() {
        initFilter();
    }

    private void initFilter() {
        logger.logMessage(LogLevel.INFO, "tiny-filter:<{}>初始化开始", getClass()
                .getSimpleName());
        if (defaultLocale == null) {
            setDefaultLocale(LocaleUtil.parseLocale(defaultIfEmpty(
                    get(DEFAULT_LOCALE), LOCALE_DEFAULT)));
        }
        if (defaultCharset == null) {
            setDefaultCharset(defaultIfEmpty(get(DEFAULT_CHARSET),
                    CHARSET_DEFAULT));
        }
        if (inputCharsetPattern == null) {
            String inputCharsetParam = defaultIfEmpty(get(INPUT_CHARSET_PARAM),
                    INPUT_CHARSET_PARAM_DEFAULT);
            setInputCharsetPattern(Pattern.compile("(" + inputCharsetParam
                    + ")=([\\w-]+)"));
        }
        if (outputCharsetPattern == null) {
            String outputCharsetParam = defaultIfEmpty(
                    get(OUTPUT_CHARSET_PARAM), OUTPUT_CHARSET_PARAM_DEFAULT);
            setOutputCharsetPattern(Pattern.compile("(" + outputCharsetParam
                    + ")=([\\w-]+)"));
        }
        if (paramKey == null) {
            setParamKey(defaultIfEmpty(get(PARAM_KEY), PARAMETER_KEY_DEFAULT));
        }
        if (sessionKey == null) {
            setSessionKey(defaultIfEmpty(get(SESSION_KEY), SESSION_KEY_DEFAULT));
        }
        if (overriders == null) {
            ConfigurationManager appConfigManager = ConfigurationUtil
                    .getConfigurationManager();
            XmlNode setLocale = appConfigManager.getApplicationConfiguration()
                    .getSubNode(SET_LOCALE_CONFIG);
            parserExtraConfig(setLocale);
        }
        logger.logMessage(LogLevel.INFO, "tiny-filter:<{}>初始化结束", getClass()
                .getSimpleName());
    }

    protected void parserExtraConfig(XmlNode setLocale) {
        if (setLocale != null) {
            NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(setLocale);
            List<XmlNode> overriderConfigs = nameFilter.findNodeList(OVERRIDER);
            this.overriders = new SetLocaleOverrider[overriderConfigs.size()];
            for (int i = 0; i < overriderConfigs.size(); i++) {
                XmlNode node = overriderConfigs.get(i);
                overriders[i] = new SetLocaleOverrider(
                        node.getAttribute(REQUEST_URI_KEY),
                        node.getAttribute(INPUT_CHARSET),
                        node.getAttribute(OUTPUT_CHARSET));
            }

        }

    }

    public void setInputCharsetPattern(Pattern inputCharsetPattern) {
        this.inputCharsetPattern = inputCharsetPattern;
    }

    public void setOutputCharsetPattern(Pattern outputCharsetPattern) {
        this.outputCharsetPattern = outputCharsetPattern;
    }

    public void setOverriders(SetLocaleOverrider[] overriders) {
        this.overriders = overriders;
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = defaultCharset;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public void preProcess(WebContext context) throws ServletException,
            IOException {
        SetLocaleWebContextImpl setLocale = (SetLocaleWebContextImpl) context;
        setLocale.prepare();
    }

    public void postProcess(WebContext context) throws ServletException,
            IOException {
        LocaleUtil.resetContext();//去除线程绑定的LocaleInfo
    }

    protected WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
        SetLocaleWebContextImpl setLocale = new SetLocaleWebContextImpl(
                wrappedContext);
        setLocale.setDefaultCharset(defaultCharset);
        setLocale.setDefaultLocale(defaultLocale);
        setLocale.setInputCharsetPattern(inputCharsetPattern);
        setLocale.setOutputCharsetPattern(outputCharsetPattern);
        setLocale.setOverriders(overriders);
        setLocale.setParamKey(paramKey);
        setLocale.setSessionKey(sessionKey);
        return setLocale;
    }

    public int getOrder() {
        return SETLOCALE_FILTER_PRECEDENCE;
    }

}
