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
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteConfiguration;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteRule;
import org.tinygroup.weblayer.webcontext.rewrite.impl.RewriteWebContextImpl;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 改写URL及参数，类似于Apache HTTPD Server中的rewrite模块。
 *
 * @author renhui
 */
public class RewriteTinyFilter extends AbstractTinyFilter {

    private RewriteRule[] rules;

    public void setRules(RewriteRule[] rules) {
        this.rules = rules;
    }

    protected void parserExtraConfig() {
        if (rules == null) {
            RewriteConfiguration rewriteConfiguration = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean("rewriteConfiguration");
            rules = rewriteConfiguration.getRules();
        }

    }

    public void preProcess(WebContext context) throws ServletException, IOException {
        RewriteWebContextImpl rewrite = (RewriteWebContextImpl) context;
        rewrite.prepare();
    }


    public void postProcess(WebContext context) throws ServletException, IOException {

    }

    public WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
        return new RewriteWebContextImpl(wrappedContext, rules);
    }


    public int getOrder() {
        return REWRITE_FILTER_PRECEDENCE;
    }

    @Override
    protected void customInit() {
        parserExtraConfig();
    }


}
