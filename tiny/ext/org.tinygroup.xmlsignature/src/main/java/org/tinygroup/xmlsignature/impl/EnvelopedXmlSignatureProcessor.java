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

import org.tinygroup.logger.LogLevel;
import org.tinygroup.xmlsignature.config.XmlSignatureConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import java.io.OutputStream;
import java.security.KeyPair;
import java.util.Collections;
import java.util.List;

/**
 * 处理Enveloped格式的XML数字签名(签名数据包含Signature元素)
 *
 * @author yancheng11334
 */
public class EnvelopedXmlSignatureProcessor extends DsigXmlSignatureProcessor {

    protected List<Reference> createReference(Document doc,
                                              XmlSignatureConfig config) throws Exception {
        Transform envelopedTransform = xmlSignatureFactory.newTransform(Transform.ENVELOPED,
                (TransformParameterSpec) null);
        DigestMethod sha1DigMethod = xmlSignatureFactory.newDigestMethod(DigestMethod.SHA1, null);
        Reference ref = xmlSignatureFactory.newReference("", sha1DigMethod, Collections.singletonList(envelopedTransform), null, null);
        return Collections.singletonList(ref);
    }

    protected void createXmlSignature(XmlSignatureConfig config, Document doc,
                                      OutputStream output) throws XMLSignatureException {
        LOGGER.logMessage(LogLevel.DEBUG, String.format("开始根据配置[%s]进行XML数字签名生成...", config.toString()));
        try {
            CanonicalizationMethod cm = createCanonicalizationMethod(config);
            LOGGER.logMessage(LogLevel.DEBUG, "创建CanonicalizationMethod完成.");
            SignatureMethod sm = createSignatureMethod(config);
            LOGGER.logMessage(LogLevel.DEBUG, "创建SignatureMethod完成.");
            List<Reference> references = createReference(doc, config);
            LOGGER.logMessage(LogLevel.DEBUG, "创建Reference完成.");
            SignedInfo si = createSignedInfo(cm, sm, references);
            LOGGER.logMessage(LogLevel.DEBUG, "创建SignedInfo完成.");
            KeyInfo ki = createKeyInfo(config);
            LOGGER.logMessage(LogLevel.DEBUG, "创建KeyInfo完成.");
            XMLSignature signature = createXMLSignature(si, ki);
            LOGGER.logMessage(LogLevel.DEBUG, "创建XMLSignature完成.");
            KeyPair keyPair = getXmlSignatureManager().getKeyPair(config.getUserId());
            LOGGER.logMessage(LogLevel.DEBUG, "创建KeyPair完成.");
            DOMSignContext signContext = new DOMSignContext(keyPair.getPrivate(), doc.getDocumentElement());
            LOGGER.logMessage(LogLevel.DEBUG, "创建DOMSignContext完成.");
            signature.sign(signContext);
            LOGGER.logMessage(LogLevel.DEBUG, "执行签名操作完成.");
            transform(doc, output);
        } catch (Exception e) {
            throw new XMLSignatureException("生成Enveloped格式的XML数字签名失败", e);
        }
        LOGGER.logMessage(LogLevel.DEBUG, String.format("根据配置[%s]进行XML数字签名生成结束", config.toString()));
    }

    protected boolean validateXmlSignature(XmlSignatureConfig config,
                                           Document doc) throws XMLSignatureException {
        LOGGER.logMessage(LogLevel.DEBUG, String.format("开始根据配置[%s]进行XML数字签名验证...", config.toString()));
        try {
            // 查找签名元素
            NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS,
                    "Signature");
            if (nl.getLength() == 0) {
                throw new Exception("没有找到<Signature>元素");
            }
            LOGGER.logMessage(LogLevel.DEBUG, "找到<Signature>元素.");
            Node signatureNode = nl.item(0);
            XMLSignature signature = xmlSignatureFactory.unmarshalXMLSignature(new DOMStructure(signatureNode));
            LOGGER.logMessage(LogLevel.DEBUG, "创建XMLSignature完成.");
            KeyPair keyPair = getXmlSignatureManager().getKeyPair(config.getUserId());
            LOGGER.logMessage(LogLevel.DEBUG, "创建KeyPair完成.");
            DOMValidateContext valCtx = new DOMValidateContext(keyPair.getPublic(),
                    signatureNode);
            LOGGER.logMessage(LogLevel.DEBUG, "创建DOMValidateContext完成.");
            boolean tag = signature.validate(valCtx);
            LOGGER.logMessage(LogLevel.DEBUG, String.format("根据配置[%s]进行XML数字签名生成结束", config.toString()));
            return tag;
        } catch (Exception e) {
            throw new XMLSignatureException("验证Enveloped格式的XML数字签名失败", e);
        }

    }

}
