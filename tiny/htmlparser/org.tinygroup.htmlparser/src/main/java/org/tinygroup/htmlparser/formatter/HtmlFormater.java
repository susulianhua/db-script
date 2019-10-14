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
package org.tinygroup.htmlparser.formatter;

import org.tinygroup.htmlparser.HtmlDocument;
import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.formater.NodeFormaterImpl;

import java.io.IOException;
import java.io.OutputStream;

public class HtmlFormater extends NodeFormaterImpl<HtmlDocument, HtmlNode> {

    /**
     * 格式化html文档 并以StringBuffer格式返回
     *
     * @param doc
     * @return StringBuffer
     */

    protected StringBuffer formatDocumentSelf(HtmlDocument doc) {
        StringBuffer sb = new StringBuffer();
        if (doc.getHtmlDeclaration() != null) {
            formatNode(sb, doc.getHtmlDeclaration(), 0);
        }
        if (doc.getDoctypeList() != null) {
            for (HtmlNode n : doc.getDoctypeList()) {
                formatNode(sb, n, 0);
            }
        }
        if (doc.getCommentList() != null) {
            for (HtmlNode n : doc.getCommentList()) {
                formatNode(sb, n, 0);
            }
        }
        return sb;
    }

    /**
     * 格式化html文档 并在指定的输出流中输出
     *
     * @param doc
     * @param out
     * @return void
     */

    protected void formatDocumentSelf(HtmlDocument doc, OutputStream out)
            throws IOException {
        if (doc.getHtmlDeclaration() != null) {
            formatNode(doc.getHtmlDeclaration(), out, 0);
        }
        if (doc.getDoctypeList() != null) {
            for (HtmlNode n : doc.getDoctypeList()) {
                formatNode(n, out, 0);
            }
        }
        if (doc.getCommentList() != null) {
            for (HtmlNode n : doc.getCommentList()) {
                formatNode(n, out, 0);
            }
        }
    }
}
