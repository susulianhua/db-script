/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * <p>
 *       http://www.gnu.org/licenses/gpl.html
 * <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.xmlsignature;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.tinyrunner.Runner;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class XmlSignatureProcessorTest extends TestCase {

    private XmlSignatureProcessor envelopedProcessor = null;

    protected void setUp() throws Exception {
        Runner.initDirect("application.xml", null);
        envelopedProcessor = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean("envelopedXmlSignatureProcessor");
    }

    public void testEnvelopedProcessor() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            //生成签名接口
            envelopedProcessor.createXmlSignature("SERVER007", "src/test/resources/simple.xml", output);
            String signResult = new String(output.toByteArray(), "utf-8");
            String content = FileUtil.readFileContent(new File("src/test/resources/enveloped_ok.xml"), "utf-8");
            assertEquals(content, signResult);

            //验证签名接口
            assertEquals(true, envelopedProcessor.validateXmlSignature("SERVER007", "src/test/resources/enveloped_ok.xml"));
            assertEquals(false, envelopedProcessor.validateXmlSignature("SERVER007", "src/test/resources/enveloped_changed.xml"));
        } finally {
            output.close();
        }
    }
}
