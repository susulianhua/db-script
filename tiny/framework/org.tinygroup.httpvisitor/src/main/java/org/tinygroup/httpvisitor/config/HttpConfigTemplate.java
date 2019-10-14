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
package org.tinygroup.httpvisitor.config;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.httpvisitor.Header;
import org.tinygroup.httpvisitor.RequestInterceptor;
import org.tinygroup.httpvisitor.struct.SimpleHeader;
import org.tinygroup.xmlsignature.impl.StringXmlSignatureHelper;

import java.util.*;
import java.util.Map.Entry;

/**
 * HTTP通讯的参数配置模板
 *
 * @author yancheng11334
 */
public class HttpConfigTemplate {

    private static final String DYNMIC_SIGNATURE_NAME = "dynmicSignatureHeader";

    private Map<String, Object> clientConfigMaps = new HashMap<String, Object>();
    private Map<String, String> headerConfigMaps = new HashMap<String, String>();

    private List<RequestInterceptor> interceptorList = new ArrayList<RequestInterceptor>();
    private List<String> interceptorNameList = new ArrayList<String>();
    private StringXmlSignatureHelper xmlSignatureHelper = new StringXmlSignatureHelper();

    private String templateId;

    public HttpConfigTemplate(String id) {
        this.templateId = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public Object getClientParamter(String name) {
        return clientConfigMaps.get(name);
    }

    public Object getClientParamter(String name, Object defaultValue) {
        return clientConfigMaps.containsKey(name) ? clientConfigMaps.get(name) : defaultValue;
    }

    public Set<String> getClientParamters() {
        return clientConfigMaps.keySet();
    }

    public void setClientParamter(String name, Object value) {
        clientConfigMaps.put(name, value);
    }

    public String getHeaderParamter(String name) {
        return headerConfigMaps.get(name);
    }

    public String getHeaderParamter(String name, String defaultValue) {
        return headerConfigMaps.containsKey(name) ? headerConfigMaps.get(name) : defaultValue;
    }

    public List<RequestInterceptor> getInterceptorList() {
        if (interceptorList.isEmpty() && interceptorNameList.size() > 0) {
            //调用时执行初始化
            for (String name : interceptorNameList) {
                interceptorList.add((RequestInterceptor) BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean(name));
            }
        }
        return interceptorList;
    }

    public void addInterceptorName(String bean) {
        interceptorNameList.add(bean);
    }

    public void resetInterceptorList() {
        interceptorNameList.clear();
        interceptorList.clear();
    }

    public List<Header> getHeaderParamters() {
        List<Header> headers = new ArrayList<Header>();
        for (Entry<String, String> entry : headerConfigMaps.entrySet()) {
            headers.add(new SimpleHeader(entry.getKey(), entry.getValue()));
        }
        //动态生成XML签名
        if (allowDynmicSignatureHeader()) {
            String name = (String) clientConfigMaps.get(DYNMIC_SIGNATURE_NAME);
            String xmlsignature = xmlSignatureHelper.getEscapeXml(null);
            headers.add(new SimpleHeader(name, xmlsignature));
        }
        return headers;
    }

    public void setHeaderParamter(String name, String value) {
        headerConfigMaps.put(name, value);
    }

    private boolean allowDynmicSignatureHeader() {
        return clientConfigMaps.containsKey(DYNMIC_SIGNATURE_NAME);
    }

    public String toString() {
        return "HttpConfigTemplate [clientConfigMaps=" + clientConfigMaps
                + ", headerConfigMaps=" + headerConfigMaps + ", templateId="
                + templateId + "]";
    }

}
