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
package org.tinygroup.servicehttpchannel;

import org.apache.commons.httpclient.HttpStatus;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.impl.AbstractEventProcessor;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.Configuration;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.httpvisitor.Response;
import org.tinygroup.httpvisitor.builder.HttpFactory;
import org.tinygroup.httpvisitor.builder.PostRequestClientBuilder;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class ServiceHttpChannelEventProcessorImpl extends
        AbstractEventProcessor implements Configuration {
    private static final String TEMPLATE_ID = "template";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ServiceHttpChannelEventProcessorImpl.class);
    private static final String SERVER_PATH_CONFIG = "/application/server-path";
    private XmlNode appConfig;
    private String serverPath;
    private String templateId;
    private ServiceHttpChannelManager manager;

    public void process(Event event) {
        if (serverPath == null) {
            throw new RuntimeException("服务器地址路径未配置");
        }
        String serviceId = event.getServiceRequest().getServiceId();
        String url = serverPath + serviceId + ".mockservice";
        Event result = execute(event, url);
        Throwable throwable = result.getThrowable();
        if (throwable != null) {// 如果有异常发生，则抛出异常
            LOGGER.errorMessage("服务执行发生异常,serviceId:{},eventId:{}", throwable,
                    result.getServiceRequest().getServiceId(),
                    result.getEventId());
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else {
                throw new RuntimeException(throwable);// 此处的RuntimeException类型需要调整
            }
        }
        event.getServiceRequest()
                .getContext()
                .putSubContext(result.getEventId(),
                        result.getServiceRequest().getContext());

    }

    private Event execute(Event event, String url) {
        byte[] data;
        try {
            data = Hession.serialize(event);
        } catch (IOException e) {
            throw new RuntimeException("序列化失败", e);
        }
        PostRequestClientBuilder p = HttpFactory
                .post(url, templateId);
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        p.data(bis);
        Response r = p.execute();
        String serviceId = event.getServiceRequest().getServiceId();
        String eventId = event.getEventId();
        try {
            int iGetResultCode = r.getStatusLine().getStatusCode();
            if (iGetResultCode == HttpStatus.SC_OK) {
                LOGGER.logMessage(LogLevel.DEBUG,
                        "请求:eventId:{},serviceId:{} 成功返回。", eventId, serviceId);
                return (Event) Hession.deserialize(r.bytes());
            }
            LOGGER.logMessage(LogLevel.ERROR,
                    "请求:eventId:{},serviceId:{},返回失败，原因：{}", eventId, serviceId, r.getStatusLine().getReasonPhrase());
            throw new RuntimeException(r.getStatusLine().getReasonPhrase());
        } catch (Exception e) {
            throw new RuntimeException("执行http请求失败", e);
        } finally {
            try {
                r.close();
            } catch (IOException e) {
                LOGGER.logMessage(LogLevel.WARN,
                        "关闭响应时发生异常，请求:eventId:{},serviceId:{} ", e, eventId, serviceId);
            }
        }
    }

    public String getApplicationNodePath() {
        return SERVER_PATH_CONFIG;
    }

    public String getComponentConfigPath() {
        return null;
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.appConfig = applicationConfig;
        if (appConfig != null) {
            serverPath = appConfig.getAttribute("path");
            if (!StringUtil.isBlank(serverPath) && !serverPath.endsWith("/")) {
                serverPath = serverPath + "/";
            }
            templateId = appConfig.getAttribute(TEMPLATE_ID);
            if (StringUtil.isBlank(templateId)) {
                throw new RuntimeException("template不允许为空");
            }

        }
    }

    public XmlNode getComponentConfig() {
        return null;
    }

    public XmlNode getApplicationConfig() {
        return appConfig;
    }

    public void setCepCore(CEPCore cepCore) {

    }

    public List<ServiceInfo> getServiceInfos() {
        return manager.getInfos();
    }

    public int getWeight() {
        return 0;
    }

    public List<String> getRegex() {
        return null;
    }

    public boolean isRead() {
        return true;
    }

    public void setRead(boolean read) {
    }

    public ServiceHttpChannelManager getManager() {
        return manager;
    }

    public void setManager(ServiceHttpChannelManager manager) {
        this.manager = manager;
    }

}
