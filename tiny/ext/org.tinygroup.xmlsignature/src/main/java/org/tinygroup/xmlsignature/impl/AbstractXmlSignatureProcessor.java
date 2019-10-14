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

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlsignature.XmlSignatureManager;
import org.tinygroup.xmlsignature.XmlSignatureProcessor;
import org.tinygroup.xmlsignature.config.XmlSignatureConfig;
import org.w3c.dom.Document;

import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 抽象的XML数字签名处理器
 *
 * @author yancheng11334
 */
public abstract class AbstractXmlSignatureProcessor implements
        XmlSignatureProcessor {

    private XmlSignatureManager xmlSignatureManager;

    public XmlSignatureManager getXmlSignatureManager() {
        return xmlSignatureManager;
    }

    public void setXmlSignatureManager(XmlSignatureManager xmlSignatureManager) {
        this.xmlSignatureManager = xmlSignatureManager;
    }

    public void createXmlSignature(String userId, String sourceXmlPath, OutputStream output)
            throws XMLSignatureException {
        FileObject file = null;
        try {
            file = VFS.resolveFile(sourceXmlPath);
            createXmlSignature(userId, file.getInputStream(), output);
        } catch (XMLSignatureException e) {
            throw e;
        } catch (Exception e) {
            throw new XMLSignatureException(String.format("生成路径[%s]的XML签名发生异常",
                    sourceXmlPath), e);
        } finally {
            if (file != null) {
                file.clean();
            }
        }
    }

    public void createXmlSignature(String userId, InputStream input, OutputStream output)
            throws XMLSignatureException {
        try {
            XmlSignatureConfig config = xmlSignatureManager.getXmlSignatureConfig(userId);
            if (config == null) {
                throw new XMLSignatureException(String.format("根据使用者ID[%s]没有找到对应的配置项", userId));
            }
            Document doc = convertDocument(input);
            createXmlSignature(config, doc, output);
        } catch (XMLSignatureException e) {
            throw e;
        } catch (Exception e) {
            throw new XMLSignatureException(String.format("生成使用者ID[%s]的XML签名发生异常",
                    userId), e);
        }
    }

    public boolean validateXmlSignature(String userId, String signedXmlPath)
            throws XMLSignatureException {
        FileObject file = null;
        try {
            file = VFS.resolveFile(signedXmlPath);
            return validateXmlSignature(userId, file.getInputStream());
        } catch (XMLSignatureException e) {
            throw e;
        } catch (Exception e) {
            throw new XMLSignatureException(String.format("验证路径[%s]的XML签名发生异常",
                    signedXmlPath), e);
        } finally {
            if (file != null) {
                file.clean();
            }
        }
    }

    public boolean validateXmlSignature(String userId, InputStream input)
            throws XMLSignatureException {
        try {
            XmlSignatureConfig config = xmlSignatureManager.getXmlSignatureConfig(userId);
            if (config == null) {
                throw new XMLSignatureException(String.format("根据使用者ID[%s]没有找到对应的配置项", userId));
            }
            Document doc = convertDocument(input);
            return validateXmlSignature(config, doc);
        } catch (XMLSignatureException e) {
            throw e;
        } catch (Exception e) {
            throw new XMLSignatureException(String.format("验证使用者ID[%s]的XML签名发生异常",
                    userId), e);
        }
    }

    /**
     * 生成XML数字签名(可以有不同底层实现)
     *
     * @param config
     * @param doc
     * @param output
     * @throws XMLSignatureException
     */
    protected abstract void createXmlSignature(XmlSignatureConfig config, Document doc, OutputStream output) throws XMLSignatureException;

    /**
     * 验证XML数字签名(可以有不同底层实现)
     *
     * @param config
     * @param doc
     * @return
     * @throws XMLSignatureException
     */
    protected abstract boolean validateXmlSignature(XmlSignatureConfig config, Document doc) throws XMLSignatureException;

    /**
     * 转换流为XML对象
     *
     * @param input
     * @return
     * @throws Exception
     */
    protected Document convertDocument(InputStream input) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        return dbf.newDocumentBuilder().parse(input);
    }


}
