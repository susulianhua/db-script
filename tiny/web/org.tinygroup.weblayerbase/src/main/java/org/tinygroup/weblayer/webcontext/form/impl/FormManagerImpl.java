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
package org.tinygroup.weblayer.webcontext.form.impl;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.tinygroup.weblayer.webcontext.form.Form;
import org.tinygroup.weblayer.webcontext.form.FormManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表单管理器实现类
 *
 * @author renhui
 */
public class FormManagerImpl implements FormManager {
    private static final String SESSION_KEY_OF_FROMS = "_forms_in_session_token";

    /**
     * 表单最大个数
     */
    private int maxFormNum = 30;

    public void destroyToken(HttpServletRequest request, String token) {
        getForms(request).remove(token);
    }

    public String dumpForm(HttpServletRequest request, String token) {
        Form form = getForms(request).get(token);
        if (form == null) {
            return "null";
        }

        return form.toString();
    }

    /**
     * 判断表单是否存在。如果token为null，直接返回false。
     */
    public boolean hasForm(HttpServletRequest request, String token) {
        if (token == null) {
            return false;
        }
        return getForms(request).containsKey(token);
    }

    public boolean hasFormToken(HttpServletRequest request) {
        String formToken = request.getParameter(Form.FORM_TOKEN_FIELD_NAME);
        return StringUtils.isNotBlank(formToken);
    }

    /**
     * 比较表单数据是否有变化
     */
    public boolean isModified(HttpServletRequest request, String token) {
        boolean flag = false;
        Form form = getForm(request, token);
        if (form != null) {
            Map<String, String> oldParams = form.getFields();

            Map<String, String> newParams = getNamedParameters(request,
                    oldParams.keySet().toArray(new String[oldParams.size()]));
            for (String key : oldParams.keySet()) {
                // 忽略null和空串的情况
                String newValue = StringUtils.defaultString(newParams.get(key));
                String oldValue = StringUtils.defaultString(oldParams.get(key));
                if (!StringUtils.equals(newValue, oldValue)) {
                    flag = true;
                    break;
                }
            }
        } else {
            flag = true;
        }

        return flag;
    }

    /**
     * 生成一个新的表单，如果目前表单个数大于设定的最大表单数则先删除最早的一个表单。<br>
     * 新表单用RandomStringUtils.randomAlphanumeric(32)生成Token。
     *
     * @return 创建的新表单
     */
    public Form newForm(HttpServletRequest request) {
        Form form = new Form(RandomStringUtils.randomAlphanumeric(32));
        Map<String, Form> forms = getForms(request);
        synchronized (forms) {
            // 如果目前表单个数大于等于最大表单数，那么删除最老的表单，添加新表单。
            if (forms.size() >= maxFormNum) {
                removeOldestForm(request);
            }

            forms.put(form.getToken(), form);
        }
        return form;
    }

    /**
     * 生成一个新的表单，如果目前表单个数大于设定的最大表单数则先删除最早的一个表单。<br>
     * 新表单用RandomStringUtils.randomAlphanumeric(32)生成Token。
     *
     * @return 创建的新表单
     */
    public Form newForm(HttpServletRequest request, String url) {
        Form form = newForm(request);
        form.setUrl(url);
        return form;
    }

    public Form getForm(HttpServletRequest request, String formToken) {
        Map<String, Form> formsInSession = getForms(request);
        return formsInSession.get(formToken);

    }

    /**
     * 获得目前session中的表单列表。
     *
     * @return 返回的Map中以表单的token为键，Form对象为值
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Form> getForms(HttpServletRequest request) {
        Map<String, Form> formsInSession = null;
        HttpSession session = request.getSession();
        synchronized (session) {
            formsInSession = (Map<String, Form>) session
                    .getAttribute(SESSION_KEY_OF_FROMS);

            if (formsInSession == null) {
                formsInSession = new HashMap<String, Form>();
                session.setAttribute(SESSION_KEY_OF_FROMS, formsInSession);
            }
        }

        return formsInSession;
    }

    /**
     * 从Request中获得指定名称的参数。如果表单项是多选，有多个数据，则进行合并，以“|”分割。
     *
     * @return 一个Map，以参数名为键，参数值为值。
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> getNamedParameters(
            HttpServletRequest request, String[] names) {
        Map<String, String[]> parameters = request.getParameterMap();
        Map<String, String> params = new HashMap<String, String>();

        for (String name : names) {
            String[] value = parameters.get(name);
            params.put(name, StringUtils.join(value, Form.FORM_VALUE_SEPARATOR));
        }

        return params;
    }

    /**
     * 删除最老的Form
     *
     * @see #destroyToken(HttpServletRequest, String)
     */
    protected void removeOldestForm(HttpServletRequest request) {
        List<Form> forms = new ArrayList<Form>(getForms(request).values());
        if (forms.isEmpty()) {
            return;
        }

        Form oldestForm = forms.get(0);
        for (Form form : forms) {
            if (form.getCreateTime().before(oldestForm.getCreateTime())) {
                oldestForm = form;
            }
        }

        destroyToken(request, oldestForm.getToken());
    }

    public void setMaxFormNum(int maxFormNum) {
        this.maxFormNum = maxFormNum;
    }

}