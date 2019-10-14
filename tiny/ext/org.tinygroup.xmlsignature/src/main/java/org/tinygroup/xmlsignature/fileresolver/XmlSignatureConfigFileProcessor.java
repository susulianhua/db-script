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
package org.tinygroup.xmlsignature.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlsignature.XmlSignatureManager;
import org.tinygroup.xmlsignature.config.XmlSignatureConfigs;
import org.tinygroup.xstream.XStreamFactory;

public class XmlSignatureConfigFileProcessor extends AbstractFileProcessor {

    private static final String XSTREAM_NAME = "xmlsignature";

    private XmlSignatureManager xmlSignatureManager;

    public XmlSignatureManager getXmlSignatureManager() {
        return xmlSignatureManager;
    }

    public void setXmlSignatureManager(XmlSignatureManager xmlSignatureManager) {
        this.xmlSignatureManager = xmlSignatureManager;
    }

    public void process() {
        XStream stream = XStreamFactory.getXStream(XSTREAM_NAME);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除XML数字签名配置文件[{0}]",
                    fileObject.getAbsolutePath());
            XmlSignatureConfigs xmlSignatureConfigs = (XmlSignatureConfigs) caches.get(fileObject.getAbsolutePath());
            if (xmlSignatureConfigs != null) {
                xmlSignatureManager.removeXmlSignatureConfigs(xmlSignatureConfigs);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除XML数字签名配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载XML数字签名配置文件[{0}]",
                    fileObject.getAbsolutePath());
            XmlSignatureConfigs oldXmlSignatureConfigs = (XmlSignatureConfigs) caches.get(fileObject.getAbsolutePath());
            if (oldXmlSignatureConfigs != null) {
                xmlSignatureManager.removeXmlSignatureConfigs(oldXmlSignatureConfigs);
            }
            XmlSignatureConfigs xmlSignatureConfigs = convertFromXml(stream, fileObject);
            xmlSignatureManager.addXmlSignatureConfigs(xmlSignatureConfigs);
            caches.put(fileObject.getAbsolutePath(), xmlSignatureConfigs);
            LOGGER.logMessage(LogLevel.INFO, "加载XML数字签名配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(".xmlsignature.xml");
    }

}
