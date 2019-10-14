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
package org.tinygroup.xmlsignature.impl;

import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringEscapeUtil;
import org.tinygroup.xmlsignature.XmlSignatureManager;
import org.tinygroup.xmlsignature.XmlSignatureProcessor;
import org.tinygroup.xmlsignature.config.XmlSignatureConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * XmlSignatureProcessor的辅助类
 *
 * @author yancheng11334
 */
public class StringXmlSignatureHelper {

    private static final String SIMPLE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Access><UserId>%s</UserId></Access>";
    private BeanContainer<?> beanContainer;
    private XmlSignatureProcessor xmlSignatureProcessor;
    private XmlSignatureManager xmlSignatureManager;

    public StringXmlSignatureHelper() {
        beanContainer = BeanContainerFactory.getBeanContainer(getClass().getClassLoader());
    }

    public XmlSignatureProcessor getXmlSignatureProcessor() {
        if (xmlSignatureProcessor == null) {
            return beanContainer.getBean("envelopedXmlSignatureProcessor");
        }
        return xmlSignatureProcessor;
    }

    public void setXmlSignatureProcessor(XmlSignatureProcessor xmlSignatureProcessor) {
        this.xmlSignatureProcessor = xmlSignatureProcessor;
    }

    public XmlSignatureManager getXmlSignatureManager() {
        if (xmlSignatureManager == null) {
            return beanContainer.getBean(XmlSignatureManager.DEFAULT_BAEN_NAME);
        }
        return xmlSignatureManager;
    }

    public void setXmlSignatureManager(XmlSignatureManager xmlSignatureManager) {
        this.xmlSignatureManager = xmlSignatureManager;
    }

    /**
     * 读取数字签名使用的XML内容,userId为null的话，会随机读取一个配置节点做userId
     *
     * @param userId
     * @return
     */
    public String getTemplateXml(String userId) {
        XmlSignatureConfig config = findXmlSignatureConfig(userId);
        return String.format(SIMPLE_XML, config.getUserId());
    }

    /**
     * 生成签名的XML文件
     *
     * @param userId
     * @return
     */
    public String getSignatureXml(String userId) {
        XmlSignatureConfig config = findXmlSignatureConfig(userId);
        String template = getTemplateXml(config.getUserId());
        ByteArrayInputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            input = new ByteArrayInputStream(template.getBytes("UTF-8"));
            output = new ByteArrayOutputStream();
            getXmlSignatureProcessor().createXmlSignature(config.getUserId(), input, output);
            return new String(output.toByteArray(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("生成XML签名发生异常", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    //ignore
                }
            }

            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }

    protected XmlSignatureConfig findXmlSignatureConfig(String userId) {
        XmlSignatureConfig config;
        if (userId == null) {
            List<XmlSignatureConfig> list = getXmlSignatureManager().getXmlSignatureConfigList();
            if (list == null || list.size() == 0) {
                throw new RuntimeException("本节点没有找到默认的数字签名配置项,请检查配置文件!");
            }
            config = list.get(0);
        } else {
            config = getXmlSignatureManager().getXmlSignatureConfig(userId);
        }
        return config;
    }

    /**
     * 读取转义后的数字签名使用的XML内容,userId为null的话，会随机读取一个配置节点做userId
     *
     * @param userId
     * @return
     */
    public String getEscapeXml(String userId) {
        try {
            return StringEscapeUtil.escapeURL(getSignatureXml(userId), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
