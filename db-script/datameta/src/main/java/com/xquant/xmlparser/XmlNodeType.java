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
package com.xquant.xmlparser;

import com.xquant.parser.NodeType;
import com.xquant.parser.nodetype.NodeSign;
import com.xquant.parser.nodetype.NodeTypeImpl;

public enum XmlNodeType implements NodeType {
    DOCTYPE(new NodeSign("<!DOCTYPE ", null), new NodeSign(null, ">"), false,
            true, false), // DTD验证器标签
    CDATA(new NodeSign("<![CDATA[", null), new NodeSign(null, "]]>"), false,
            true, false), // CDATA内容标签，只有文本内空
    XML_DECLARATION(new NodeSign("<?xml", null), new NodeSign(null, "?>"),
            true, false, false), // XML声明标签部分
    PROCESSING_INSTRUCTION(new NodeSign("<?", null), new NodeSign(null, "?>"),
            false, true, false), // XML处理指令标签
    COMMENT(new NodeSign("<!--", null), new NodeSign(null, "-->"), false, true,
            false), // 注释标签部分
    ELEMENT(new NodeSign("<", ">"),// 元素标签部分
            new NodeSign("</", ">"), true, false, true),
    TEXT(null, null, // 文本内容标签部分
            false, true, false);

    private NodeType nt = null;

    /**
     * 构造方法
     *
     * @param head       头标签
     * @param tail       结尾标签
     * @param hasHeader  是否有头部
     * @param hasContent 是否有文本内容
     * @param hasBody    是否有子结点
     */
    XmlNodeType(NodeSign head, NodeSign tail, boolean hasHeader,
                boolean hasContent, boolean hasBody) {
        nt = new NodeTypeImpl(head, tail, hasHeader, hasContent, hasBody);
    }

    public boolean isHasHeader() {
        return nt.isHasHeader();
    }

    public boolean isHasBody() {
        return nt.isHasBody();
    }

    public boolean isHasContent() {
        return nt.isHasContent();
    }

    /**
     * 获取头标签
     *
     * @return NodeSign
     */
    public NodeSign getHead() {
        return nt.getHead();
    }

    /**
     * 获取结尾标签
     *
     * @return NodeSign
     */
    public NodeSign getTail() {
        return nt.getTail();
    }

    /**
     * 获取头标签的标识 符 为输入参数添加头标签
     *
     * @param str
     * @return StringBuffer
     */
    public void getHeader(StringBuffer sb, String str) {
        if (this.equals(ELEMENT) && str.length() == 0) {
            return;
        }
        if (getHead() == null) {
            sb.append(str);
        } else {
            if (getHead().getStart() != null) {
                sb.append(getHead().getStart());
            }
            sb.append(str);
            if (getHead().getEnd() != null) {
                sb.append(getHead().getEnd());
            }
        }
    }

    /**
     * 获取结尾标签的标识符 为输入参数添加结束标签
     *
     * @param str
     * @return StringBuffer
     */
    public void getTail(StringBuffer sb, String str) {
        if (this.equals(ELEMENT) && str.length() == 0) {
            return;
        }
        if (getTail() == null) {
            sb.append(str);
        } else {
            if (getTail().getStart() != null) {
                sb.append(getTail().getStart());
            }
            sb.append(str);
            if (getTail().getEnd() != null) {
                sb.append(getTail().getEnd());
            }
        }
    }

    public boolean isText() {
        return this == TEXT;
    }
}
