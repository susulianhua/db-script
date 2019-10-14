/**
 * Copyright (c) 2012-2016, www.tinygroup.org (luo_guo@icloud.com).
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
package org.tinygroup.flowbasiccomponent.test;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flowbasiccomponent.util.XMLOperatorUtil;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlparser.node.XmlNode;

import java.io.File;
import java.util.ArrayList;

public abstract class AbstractFlowComponent extends TestCase {

    protected FlowExecutor flowExecutor;
    protected CEPCore cepcore;
    protected FlowEventProcessorForTest eventProcessorForTest;

    void init() {
        Runner.init("application.xml", new ArrayList<String>());
    }

    public void deleteFile(String filePath) {
        File file = new File(VFS.resolveFile(filePath).getAbsolutePath());
        if (file.exists()) {
            file.delete();
        }
    }

    protected void setUp() throws Exception {
        init();
        flowExecutor = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                FlowExecutor.FLOW_BEAN);
        cepcore = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                CEPCore.CEP_CORE_BEAN);
        eventProcessorForTest = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                "flowEventProcessorForTest");
        cepcore.registerEventProcessor(eventProcessorForTest);
    }

    protected XmlNode getXml() {
        String xml = "<?xml version=\"1.0\" encoding=\"GB2312\"?><CFX><HEAD><VER>1.0</VER><SRC>100000000000</SRC><DES>114200000000</DES><APP>TIPS</APP><MsgNo>2001</MsgNo><MsgID>20050830200100087356</MsgID><MsgRef>12345678901234567890</MsgRef><WorkDate>20050824</WorkDate></HEAD><MSG><SingleReturn2001><OriTaxOrgCode>14200000000</OriTaxOrgCode><OriTraNo>00000016</OriTraNo><OriEntrustDate>20050827</OriEntrustDate><TaxVouNo>1</TaxVouNo><TaxDate>20050824</TaxDate><Result>90000</Result><AddWord>成功</AddWord></SingleReturn2001></MSG></CFX>";
        return XMLOperatorUtil.getXmlNode(xml);
    }
}
