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

import org.apache.commons.lang.StringUtils;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.form.Form;
import org.tinygroup.weblayer.webcontext.form.FormCheckStrategy;
import org.tinygroup.weblayer.webcontext.form.FormManager;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 进行表单检查的tiny过滤器，可以防止重复提交以及防止csrf攻击
 *
 * @author renhui
 */
public class FormCheckTinyFilter extends AbstractTinyFilter {

    private FormManager formManager;

    private FormCheckStrategy formCheckStrategy;

    private List<String> checkUrls = new ArrayList<String>();

    private boolean filterFormCheck;

    public void preProcess(WebContext context) throws ServletException,
            IOException {
        if (filterFormCheck) {
            String servletPath = WebContextUtil.getServletPath(context
                    .getRequest());
            boolean isCheck = false;
            for (String checkUrl : checkUrls) {
                if (Pattern.matches(checkUrl, servletPath)) {
                    isCheck = true;
                    break;
                }
            }
            if (isCheck) {
                formCheckStrategy.apply(context.getRequest(), false);
            }
        }
    }

    public void postProcess(WebContext context) throws ServletException,
            IOException {
        // 检查通过，禁止重复提交则销毁表单.
        HttpServletRequest request = context.getRequest();
        String formToken = getFormToken(context.getRequest());
        if (formManager != null && formToken != null) {
            String forbidDupSubmit = request
                    .getParameter(Form.FORM_FORBID_DUP_SUBMIT);
            if (forbidDupSubmit == null) {
                forbidDupSubmit = (String) request
                        .getAttribute(Form.FORM_FORBID_DUP_SUBMIT);
            }
            if (!(StringUtils.equalsIgnoreCase(forbidDupSubmit, "n") || StringUtils
                    .equalsIgnoreCase(forbidDupSubmit, "no"))) {
                formManager.destroyToken(request, formToken);
            }
        }
    }

    /**
     * 获取token
     *
     * @param request
     * @return
     */
    protected String getFormToken(HttpServletRequest request) {
        String formToken = request.getParameter(Form.FORM_TOKEN_FIELD_NAME);
        if (formToken == null) {
            formToken = (String) request
                    .getAttribute(Form.FORM_TOKEN_FIELD_NAME);
        }
        return formToken;
    }

    @Override
    protected void customInit() {
        String checkUrlStr = get("check-urls");
        if (checkUrls == null) {
            checkUrls = new ArrayList<String>();
        }
        if (!StringUtil.isBlank(checkUrlStr)) {
            Collections.addAll(checkUrls, checkUrlStr.split(","));
        }
        String filterCheck = get("filterFormCheck");
        if (!StringUtil.isBlank(filterCheck)) {
            filterFormCheck = Boolean.parseBoolean(filterCheck);
        }
    }

    public void setFormManager(FormManager formManager) {
        this.formManager = formManager;
    }

    public void setFormCheckStrategy(FormCheckStrategy formCheckStrategy) {
        this.formCheckStrategy = formCheckStrategy;
    }

    public void setCheckUrls(List<String> checkUrls) {
        this.checkUrls = checkUrls;
    }

    public void setFilterFormCheck(boolean filterFormCheck) {
        this.filterFormCheck = filterFormCheck;
    }

}
