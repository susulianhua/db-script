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
package org.tinygroup.xmlsignature;

import javax.xml.crypto.dsig.XMLSignatureException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * XML数字签名处理器
 *
 * @author yancheng11334
 */
public interface XmlSignatureProcessor {

    /**
     * 生成XML数字签名
     *
     * @param userId
     * @param input
     * @param output
     * @throws XMLSignatureException
     */
    void createXmlSignature(String userId, InputStream input, OutputStream output) throws XMLSignatureException;

    /**
     * 生成XML数字签名
     *
     * @param userId
     * @param sourceXmlPath
     * @param output
     * @throws XMLSignatureException
     */
    void createXmlSignature(String userId, String sourceXmlPath, OutputStream output) throws XMLSignatureException;

    /**
     * 验证XML数字签名
     *
     * @param userId
     * @param input
     * @return
     * @throws XMLSignatureException
     */
    boolean validateXmlSignature(String userId, InputStream input) throws XMLSignatureException;

    /**
     * 验证XML数字签名
     *
     * @param userId
     * @param signedXmlPath
     * @return
     * @throws XMLSignatureException
     */
    boolean validateXmlSignature(String userId, String signedXmlPath) throws XMLSignatureException;
}
