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
package org.tinygroup.weblayer.webcontext.form;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 表单类
 *
 * @author renhui
 */
public class Form implements Serializable {

    /**
     * 多值表单数据合并时使用的分隔符
     */
    public static final String FORM_VALUE_SEPARATOR = "|";
    public static final String FORM_TOKEN_FIELD_NAME = "_form_token";
    public static final String FORM_UNIQ_ID_FIELD_NAME = "_form_uniq_id";
    public static final String FORM_FORBID_DUP_SUBMIT = "_form_forbid_dup";
    public static final int FORM_STATUS_FIRST = 0; // 此状态为token未使用时的状态
    public static final int FORM_STATUS_USED = 1; // 此状态为防重复提交检查后token的状态
    /**
     *
     */
    private static final long serialVersionUID = 1578752024001891549L;
    /**
     * 表单标识
     */
    private String token;

    private String url;

    /**
     * 表单状态标识
     */
    private int status = FORM_STATUS_FIRST;

    /**
     * 表单字段
     */
    private Map<String, String> fields = new HashMap<String, String>();

    /**
     * 表单创建时间
     */
    private Date createTime;


    public Form() {
        super();
        this.createTime = new Date();
    }

    /**
     * 构造函数
     *
     * @param token
     */
    public Form(String token) {
        this.token = token;
        this.createTime = new Date();
    }

    public static String getFormUniqIdFieldName() {
        return FORM_UNIQ_ID_FIELD_NAME;
    }

    /**
     * 向表单中添加数据，value会被合并成一个字符串，以FORM_VALUE_SEPARATOR分割。
     *
     * @see #setField(String, String)
     */
    public String setField(String name, String[] value) {
        return setField(name, StringUtils.join(value, FORM_VALUE_SEPARATOR));
    }

    /**
     * 向表单中添加数据
     */
    public String setField(String name, String value) {
        if (name != null && value != null) {
            if (fields == null) {
                fields = new HashMap<String, String>();
            }

            fields.put(name, value);
        }

        return value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public Date getCreateTime() {
        return createTime;
    }

    // 用于VM中获取field名称的方法

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTokenFieldName() {
        return FORM_TOKEN_FIELD_NAME;
    }

    public String getForbidDupSubmitName() {
        return FORM_FORBID_DUP_SUBMIT;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}