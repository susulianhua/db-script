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

import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.lazycommit.impl.LazyCommitWebContextImpl;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 延迟提交response，用来支持基于cookie的session。
 *
 * @author renhui
 */
public class LazyCommitTinyFilter extends AbstractTinyFilter {


    public void preProcess(WebContext context) throws ServletException, IOException {

    }


    public void postProcess(WebContext context) throws ServletException, IOException {
        if (context instanceof LazyCommitWebContextImpl) {
            LazyCommitWebContextImpl lazy = (LazyCommitWebContextImpl) context;
            lazy.commit();
        }
    }


    public WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
        return new LazyCommitWebContextImpl(wrappedContext);
    }


    public int getOrder() {
        return LAZY_COMMIT_FILTER_PRECEDENCE;
    }


    @Override
    protected void customInit() {

    }


}
