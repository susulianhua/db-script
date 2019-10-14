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

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.session.SessionConfiguration;
import org.tinygroup.weblayer.webcontext.session.SessionConfiguration.ConfigImpl;
import org.tinygroup.weblayer.webcontext.session.SessionWebContext;
import org.tinygroup.weblayer.webcontext.session.impl.SessionWebContextImpl;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;

import javax.servlet.ServletException;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 增强的Session框架，可将session中的对象保存到cookie、数据库或其它存储中。
 *
 * @author renhui
 */
public class SessionTinyFilter extends AbstractTinyFilter {

    private static final String SESSION_CONFIGURATION_BEAN_NAME = "sessionConfiguration";

    private ConfigImpl config;


    @Override
    protected void customInit() {
        SessionConfiguration sessionConfiguration = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean(
                SESSION_CONFIGURATION_BEAN_NAME);
        config = sessionConfiguration.getSessionConfig();
    }


    public void preProcess(WebContext context) throws ServletException, IOException {

    }


    public void postProcess(WebContext context) throws ServletException, IOException {
        SessionWebContextImpl session = (SessionWebContextImpl) context;
        session.commit();
    }

    public WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
    	SessionWebContext sessionWebContext= new SessionWebContextImpl(wrappedContext, config);
    	String pattern = config.ignoreSessionCheckPattern();
    	if(!StringUtil.isBlank(pattern)){
    		String servletPath = WebContextUtil.getServletPath(wrappedContext.getRequest());
    		if(Pattern.matches(pattern, servletPath)){
    			sessionWebContext.setIgnoreSessionTouch(true);
    		}
    	}
    	return sessionWebContext;
    }

    public int getOrder() {
        return SESSION_FILTER_PRECEDENCE;
    }

}
