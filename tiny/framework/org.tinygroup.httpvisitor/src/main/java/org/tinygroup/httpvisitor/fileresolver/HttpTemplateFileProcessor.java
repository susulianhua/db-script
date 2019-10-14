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
package org.tinygroup.httpvisitor.fileresolver;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.commons.tools.ValueUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.httpvisitor.client.ClientConstants;
import org.tinygroup.httpvisitor.config.HttpConfigTemplate;
import org.tinygroup.httpvisitor.manager.HttpTemplateManager;
import org.tinygroup.httpvisitor.struct.KeyCert;
import org.tinygroup.httpvisitor.struct.PasswordCert;
import org.tinygroup.httpvisitor.struct.Proxy;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.io.InputStream;
import java.util.List;


/**
 * 扫描HTTP通讯配置模板
 *
 * @author yancheng11334
 */
public class HttpTemplateFileProcessor extends AbstractFileProcessor {

    private XmlStringParser parser = new XmlStringParser();

    private HttpTemplateManager httpTemplateManager;

    public HttpTemplateManager getHttpTemplateManager() {
        return httpTemplateManager;
    }

    public void setHttpTemplateManager(HttpTemplateManager manager) {
        this.httpTemplateManager = manager;
    }

    public void process() {
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除HTTP通讯配置模板文件[{0}]",
                    fileObject.getAbsolutePath());
            XmlNode xmlNode = (XmlNode) caches.get(fileObject.getAbsolutePath());
            if (xmlNode != null) {
                removeHttpTemplate(xmlNode);
                caches.remove(fileObject.getAbsolutePath());
            }

            LOGGER.logMessage(LogLevel.INFO, "移除HTTP通讯配置模板文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载HTTP通讯配置模板文件[{0}]",
                    fileObject.getAbsolutePath());
            XmlNode oldXmlNode = (XmlNode) caches.get(fileObject.getAbsolutePath());
            if (oldXmlNode != null) {
                removeHttpTemplate(oldXmlNode);
            }
            XmlNode xmlNode = convertXml(fileObject);
            if (xmlNode != null) {
                addHttpTemplate(xmlNode);
                caches.put(fileObject.getAbsolutePath(), xmlNode);
            }
            LOGGER.logMessage(LogLevel.INFO, "加载HTTP通讯配置模板文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(".httptemplate.xml");
    }

    private XmlNode convertXml(FileObject fileObject) {
        InputStream inputStream = null;
        try {
            inputStream = fileObject.getInputStream();
            return parser.parse(IOUtils.readFromInputStream(inputStream, "utf-8")).getRoot();
        } catch (Exception e) {
            LOGGER.errorMessage("解析HTTP通讯配置模板文件[{0}]发生异常", e, fileObject.getAbsolutePath());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    LOGGER.errorMessage("关闭文件流时出错,文件路径:{0}", e, fileObject.getAbsolutePath());
                }
            }
        }
        return null;
    }

    private void addHttpTemplate(XmlNode xmlNode) {

        List<XmlNode> templateConfigs = xmlNode.getSubNodes("http-template");
        if (templateConfigs != null) {
            for (XmlNode node : templateConfigs) {
                String id = node.getAttribute("id");
                LOGGER.logMessage(LogLevel.INFO, "正在加载HTTP通讯配置模板[{0}]", id);
                HttpConfigTemplate template = new HttpConfigTemplate(id);
                addClientParamter(node.getSubNodes("client-paramter"), template);
                addHeaderParamter(node.getSubNodes("header-paramter"), template);
                addRequestInterceptor(node.getSubNodes("request-interceptor"), template);
                httpTemplateManager.addHttpConfigTemplate(template);
                LOGGER.logMessage(LogLevel.INFO, "加载HTTP通讯配置模板[{0}]结束", template.getTemplateId());
            }
        }
    }

    private void addClientParamter(List<XmlNode> clientNodes, HttpConfigTemplate template) {
        if (clientNodes != null) {
            for (XmlNode node : clientNodes) {
                String name = node.getAttribute("name");
                try {
                    if (ClientConstants.CLIENT_CERT.equals(name)) {
                        addCert(node, template);
                    } else if (ClientConstants.CLIENT_PROXY.equals(name)) {
                        addProxy(node, template);
                    } else {
                        template.setClientParamter(name, ValueUtil.getValue(node.getContent(), node.getAttribute("type")));
                    }
                } catch (Exception e) {
                    LOGGER.errorMessage("解析HTTP通讯配置模板[{0}]的客户端参数[{1}]失败", e, template.getTemplateId(), name);
                }

            }
        }
    }

    private void addCert(XmlNode node, HttpConfigTemplate template) throws Exception {
        String userName = node.getAttribute("userName");
        String password = node.getAttribute("password");
        String certPath = node.getAttribute("certPath");
        String certType = node.getAttribute("certType");
        if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(password)) {
            template.setClientParamter(node.getAttribute("name"), new PasswordCert(userName, password));
        } else if (!StringUtil.isEmpty(certPath) && !StringUtil.isEmpty(password) && !StringUtil.isEmpty(certType)) {
            template.setClientParamter(node.getAttribute("name"), new KeyCert(certPath, password, certType));
        } else {
            LOGGER.logMessage(LogLevel.WARN, "解析HTTP通讯配置模板[{0}]的认证节点[{1}]失败", template.getTemplateId(), node.toString());
        }
    }

    private void addProxy(XmlNode node, HttpConfigTemplate template) throws Exception {
        String host = node.getAttribute("host");
        Integer port = Integer.parseInt(node.getAttribute("port"));
        String proxyName = node.getAttribute("proxyName");
        String password = node.getAttribute("password");
        if (StringUtil.isEmpty(proxyName) || StringUtil.isEmpty(password)) {
            template.setClientParamter(node.getAttribute("name"), new Proxy(host, port));
        } else {
            template.setClientParamter(node.getAttribute("name"), new Proxy(host, port, proxyName, password));
        }
    }

    //header节点不做类型转换
    private void addHeaderParamter(List<XmlNode> headerNodes, HttpConfigTemplate template) {
        if (headerNodes != null) {
            for (XmlNode node : headerNodes) {
                template.setHeaderParamter(node.getAttribute("name"), node.getContent());
            }
        }
    }

    private void addRequestInterceptor(List<XmlNode> interceptorNodes, HttpConfigTemplate template) {
        template.resetInterceptorList();
        if (interceptorNodes != null) {
            for (XmlNode node : interceptorNodes) {
                template.addInterceptorName(node.getAttribute("bean"));
            }
        }
    }

    private void removeHttpTemplate(XmlNode xmlNode) {
        List<XmlNode> templateConfigs = xmlNode.getSubNodes("http-template");
        if (templateConfigs != null) {
            for (XmlNode node : templateConfigs) {
                String id = node.getAttribute("id");
                LOGGER.logMessage(LogLevel.INFO, "正在移除HTTP通讯配置模板[{0}]", id);
                httpTemplateManager.removeHttpConfigTemplate(id);
                LOGGER.logMessage(LogLevel.INFO, "移除HTTP通讯配置模板[{0}]结束", id);
            }
        }
    }

}
