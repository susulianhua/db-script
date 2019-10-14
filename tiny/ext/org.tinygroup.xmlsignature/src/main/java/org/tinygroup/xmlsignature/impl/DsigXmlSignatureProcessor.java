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

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlsignature.config.XmlSignatureConfig;
import org.w3c.dom.Document;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.security.KeyPair;
import java.util.Collections;
import java.util.List;

/**
 * 基于javax.xml.dsig包的XML数字签名的实现
 *
 * @author yancheng11334
 */
public abstract class DsigXmlSignatureProcessor extends
        AbstractXmlSignatureProcessor {

    protected XMLSignatureFactory xmlSignatureFactory;

    protected Logger LOGGER = LoggerFactory.getLogger(DsigXmlSignatureProcessor.class);

    public DsigXmlSignatureProcessor() {
        this(null);
    }

    public DsigXmlSignatureProcessor(String mechanismType) {
        xmlSignatureFactory = mechanismType == null ? XMLSignatureFactory
                .getInstance() : XMLSignatureFactory.getInstance(mechanismType);
    }

    /**
     * 创建References
     *
     * @return
     */
    protected abstract List<Reference> createReference(Document doc, XmlSignatureConfig config) throws Exception;

    /**
     * 创建CanonicalizationMethod
     *
     * @param config
     * @return
     * @throws Exception
     */
    protected CanonicalizationMethod createCanonicalizationMethod(
            XmlSignatureConfig config) throws Exception {
        CanonicalizationMethod cmethod = xmlSignatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null);
        return cmethod;
    }

    /**
     * 创建SignatureMethod
     *
     * @param config
     * @return
     * @throws Exception
     */
    protected SignatureMethod createSignatureMethod(XmlSignatureConfig config)
            throws Exception {
        SignatureMethod smethod = xmlSignatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
        return smethod;
    }

    /**
     * 创建KeyInfo
     *
     * @param config
     * @return
     * @throws Exception
     */
    protected KeyInfo createKeyInfo(XmlSignatureConfig config) throws Exception {
        KeyPair keyPair = getXmlSignatureManager().getKeyPair(config.getUserId());
        KeyInfoFactory keyInfoFac = xmlSignatureFactory.getKeyInfoFactory();
        KeyValue keyValue = keyInfoFac.newKeyValue(keyPair.getPublic());
        KeyInfo keyInfo = keyInfoFac.newKeyInfo(Collections
                .singletonList(keyValue));
        return keyInfo;
    }

    /**
     * 创建SignedInfo
     *
     * @return
     */
    protected SignedInfo createSignedInfo(CanonicalizationMethod cm, SignatureMethod sm, List<Reference> references) {
        return xmlSignatureFactory.newSignedInfo(cm, sm, references);
    }

    /**
     * 创建XMLSignature
     *
     * @param si
     * @param ki
     * @return
     */
    protected XMLSignature createXMLSignature(SignedInfo si, KeyInfo ki) {
        return xmlSignatureFactory.newXMLSignature(si, ki);
    }

    /**
     * 输出签名完毕的XML
     *
     * @param doc
     * @param output
     * @throws Exception
     */
    protected void transform(Document doc, OutputStream output) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(output));
    }

}
