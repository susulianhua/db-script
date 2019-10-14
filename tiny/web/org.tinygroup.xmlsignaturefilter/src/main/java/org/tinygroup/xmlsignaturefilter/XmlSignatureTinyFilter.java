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
package org.tinygroup.xmlsignaturefilter;

import org.tinygroup.commons.tools.StringEscapeUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.parser.XmlStringParser;
import org.tinygroup.xmlsignature.impl.StringXmlSignatureHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 验证XML数字签名的过滤器
 *
 * @author yancheng11334
 */
public class XmlSignatureTinyFilter extends AbstractTinyFilter {

    private String headerName;
    private StringXmlSignatureHelper helper = new StringXmlSignatureHelper();

    public void preProcess(WebContext context) throws ServletException,
            IOException {
    }

    public void postProcess(WebContext context) throws ServletException,
            IOException {
    }

    public WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
        HttpServletRequest request = wrappedContext.getRequest();
        try {
            boolean tag = checkRequestHeader(request);
            return new XmlSignatureWebContextImpl(wrappedContext, tag);
        } catch (Exception e) {
            throw new RuntimeException("验证XML数字签名发生异常", e);
        }
    }

    private boolean checkRequestHeader(HttpServletRequest request) throws Exception {
        String xml = request.getHeader(headerName);
        if (xml != null) {
            xml = StringEscapeUtil.unescapeURL(xml, "UTF-8");
            XmlDocument doc = new XmlStringParser().parse(xml);
            String userId = doc.getRoot().getSubNode("UserId").getContent();
            ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            //需要取反
            return !helper.getXmlSignatureProcessor().validateXmlSignature(userId, input);
        }
        return false;
    }

    protected void customInit() {
        headerName = StringUtil.defaultIfEmpty(get("headerName"), "tiny-xmlsignature");
    }

}
