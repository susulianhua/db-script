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
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.xmlsignature.config.XmlSignatureConfig;

import java.security.KeyPair;

/**
 * 测试XML数字签名的加载
 *
 * @author yancheng11334
 */
public class XmlSignatureManagerTest extends TestCase {

    private XmlSignatureManager manager;

    protected void setUp() throws Exception {
        Runner.initDirect("application.xml", null);
        manager = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean(XmlSignatureManager.DEFAULT_BAEN_NAME);
    }

    public void testBase() {
        XmlSignatureConfig config = null;
        config = manager.getXmlSignatureConfig("GSYH001");
        assertEquals("/opt/public.jks", config.getPublicKeyPath());
        assertEquals("/opt/private.jks", config.getPrivateKeyPath());

        config = manager.getXmlSignatureConfig("HZGF");
        assertEquals("/opt/1.keystore", config.getPublicKeyPath());
        assertEquals("/opt/2.keystore", config.getPrivateKeyPath());
    }

    public void testKeyPair() {
        KeyPair keyPair = manager.getKeyPair("SERVER007");
        assertNotNull(keyPair);
        assertEquals("RSA", keyPair.getPublic().getAlgorithm());
        assertEquals("RSA", keyPair.getPrivate().getAlgorithm());
    }
}
