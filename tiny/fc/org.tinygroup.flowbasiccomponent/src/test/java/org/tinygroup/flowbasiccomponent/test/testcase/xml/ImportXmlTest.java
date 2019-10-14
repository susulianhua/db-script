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
package org.tinygroup.flowbasiccomponent.test.testcase.xml;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.flowbasiccomponent.test.AbstractFlowComponent;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * @author qiucn
 */
public class ImportXmlTest extends AbstractFlowComponent {

    public void testImportXml() {
        Context context = ContextFactory.getContext();
        context.put("xml", getXml());
        context.put("importFilePath", VFS.resolveFile("src/test/resources/import.xml").getAbsolutePath());
        context.put("nodePath", "");
        context.put("encoding", "GB2312");
        Event e = Event.createEvent("importNodeFlow", context);
        cepcore.process(e);
        XmlNode xn = e.getServiceRequest().getContext().get("xml");
        assertEquals("<CFX><HEAD><VER>1.0</VER><SRC>100000000000</SRC><DES>114200000000</DES><APP>TIPS</APP><MsgNo>2001</MsgNo><MsgID>20050830200100087356</MsgID><MsgRef>12345678901234567890</MsgRef><WorkDate>20050824</WorkDate></HEAD><MSG><SingleReturn2001><OriTaxOrgCode>14200000000</OriTaxOrgCode><OriTraNo>00000016</OriTraNo><OriEntrustDate>20050827</OriEntrustDate><TaxVouNo>1</TaxVouNo><TaxDate>20050824</TaxDate><Result>90000</Result><AddWord>成功</AddWord></SingleReturn2001></MSG><MSG><SingleReturn2001><OriTaxOrgCode>14200000000</OriTaxOrgCode><OriTraNo>00000016</OriTraNo><OriEntrustDate>20050827</OriEntrustDate><TaxVouNo>1</TaxVouNo><TaxDate>20050824</TaxDate><Result>90000</Result><AddWord>成功</AddWord></SingleReturn2001></MSG></CFX>", xn.toString());
    }

    public void testImportXml1() {
        Context context = ContextFactory.getContext();
        context.put("xml", getXml());
        context.put("importFilePath", VFS.resolveFile("src/test/resources/import.xml").getAbsolutePath());
        context.put("nodePath", "/CFX/MSG/SingleReturn2001");
        context.put("encoding", "GB2312");
        Event e = Event.createEvent("importNodeFlow", context);
        cepcore.process(e);
        XmlNode xn = e.getServiceRequest().getContext().get("xml");
        assertEquals("<CFX><HEAD><VER>1.0</VER><SRC>100000000000</SRC><DES>114200000000</DES><APP>TIPS</APP><MsgNo>2001</MsgNo><MsgID>20050830200100087356</MsgID><MsgRef>12345678901234567890</MsgRef><WorkDate>20050824</WorkDate></HEAD><MSG><SingleReturn2001><OriTaxOrgCode>14200000000</OriTaxOrgCode><OriTraNo>00000016</OriTraNo><OriEntrustDate>20050827</OriEntrustDate><TaxVouNo>1</TaxVouNo><TaxDate>20050824</TaxDate><Result>90000</Result><AddWord>成功</AddWord><MSG><SingleReturn2001><OriTaxOrgCode>14200000000</OriTaxOrgCode><OriTraNo>00000016</OriTraNo><OriEntrustDate>20050827</OriEntrustDate><TaxVouNo>1</TaxVouNo><TaxDate>20050824</TaxDate><Result>90000</Result><AddWord>成功</AddWord></SingleReturn2001></MSG></SingleReturn2001></MSG><MSG><SingleReturn2001><OriTaxOrgCode>14200000000</OriTaxOrgCode><OriTraNo>00000016</OriTraNo><OriEntrustDate>20050827</OriEntrustDate><TaxVouNo>1</TaxVouNo><TaxDate>20050824</TaxDate><Result>90000</Result><AddWord>成功</AddWord></SingleReturn2001></MSG></CFX>", xn.toString());
    }
}
