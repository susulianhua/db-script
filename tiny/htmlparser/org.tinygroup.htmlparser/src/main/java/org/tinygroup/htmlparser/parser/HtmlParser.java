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
package org.tinygroup.htmlparser.parser;

import org.tinygroup.htmlparser.HtmlDocument;
import org.tinygroup.htmlparser.HtmlNodeType;
import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.NodeType;
import org.tinygroup.parser.Parser;

public abstract class HtmlParser<Source> implements
        Parser<HtmlNode, HtmlDocument, Source> {

    private static String startPattern = null;

    /**
     * 获取指定节点类型开始标签的结束标识符的Pattern（正则表达式）
     *
     * @param nt
     * @return
     */
    static String getHeadEndPattern(NodeType nt) {
        if (nt.getHead() != null && nt.getHead().getEnd() != null) {
            return replaceSpecialChar(nt.getHead().getEnd());
        }
        return null;
    }

    /**
     * 获取指定节点类型结尾标签（NodeSign）的开始标识符的Pattern(正则表达式)
     *
     * @param nt
     * @return
     */
    static String getTailStartPattern(NodeType nt) {
        if (nt.getTail() != null && nt.getTail().getStart() != null) {
            return replaceSpecialChar(nt.getTail().getStart());
        }
        return null;
    }

    /**
     * 获取指定节点类型结尾标签的结束标识符的Pattern（正则表达式）
     *
     * @param nt
     * @return
     */
    static String getTailEndPattern(NodeType nt) {
        if (nt.getTail() != null && nt.getTail().getEnd() != null) {
            return replaceSpecialChar(nt.getTail().getEnd());
        }
        return null;
    }

    /**
     * 获取所有可用标记的Pattern(正则表达式)
     *
     * @return
     */
    static String getHeadStartPattern() {
        if (startPattern != null) {
            return startPattern;
        }
        StringBuffer pattern = new StringBuffer("<[/]|[/]>");// 正则表达式
        for (HtmlNodeType nt : HtmlNodeType.values()) {
            if (nt.getHead() != null) {
                String sn = nt.getHead().getStart();
                if (sn != null) {
                    if (pattern.length() > 0) {
                        pattern.append("|");
                    }
                    pattern.append(replaceSpecialChar(sn));
                }
            }
        }

        startPattern = pattern.toString();
        return startPattern;
    }

    private static String replaceSpecialChar(String sn) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sn.length(); i++) {
            char c = sn.charAt(i);
            switch (c) {
                case '[':
                    sb.append("\\[");
                    break;
                case ']':
                    sb.append("\\]");
                    break;
                case '?':
                    sb.append("\\?");
                    break;
                case '!':
                    sb.append("\\!");
                    break;
                case '-':
                    sb.append("\\-");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
}
