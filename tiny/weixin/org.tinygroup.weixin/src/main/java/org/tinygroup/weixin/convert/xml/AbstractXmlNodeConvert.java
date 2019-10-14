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
package org.tinygroup.weixin.convert.xml;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinConvert;
import org.tinygroup.weixin.convert.AbstractWeiXinConvert;
import org.tinygroup.weixin.exception.WeiXinException;
import org.tinygroup.weixin.util.WeiXinParserUtil;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 微信XML转换基类
 *
 * @author yancheng11334
 */
public abstract class AbstractXmlNodeConvert extends AbstractWeiXinConvert {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractXmlNodeConvert.class);
    protected XStream xstream;

    public AbstractXmlNodeConvert(Class<?> clazz) {
        super(clazz);
        xstream = new XStream();
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.processAnnotations(clazz);
    }

    public <INPUT> boolean isMatch(INPUT input, WeiXinContext context) {
        if (input instanceof XmlNode) {
            if (checkResultType(input, context)) {
                return checkMatch((XmlNode) input, context);
            }
        }
        return false;
    }

    public <OUTPUT, INPUT> OUTPUT convert(INPUT input, WeiXinContext context) {
        return convertXmlNode((XmlNode) input, context);
    }

    /**
     * 根据报文内容进行判断
     *
     * @param input
     * @param context
     * @return
     */
    protected abstract boolean checkMatch(XmlNode input, WeiXinContext context);

    /**
     * 转换XmlNode为业务对象
     *
     * @param <OUTPUT>
     * @param input
     * @param context
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <OUTPUT> OUTPUT convertXmlNode(XmlNode input, WeiXinContext context) {
        try {
            return (OUTPUT) xstream.fromXML(input.toString());
        } catch (Exception e) {
            throw new WeiXinException(String.format("%s convert to class:%s is failed!", input.toString(), clazz.getName()), e);
        }
    }

    protected String getMsgType(XmlNode input) {
        return get(input, "MsgType");
    }

    protected String getEncrypt(XmlNode input) {
        return get(input, "Encrypt");
    }

    protected String getEvent(XmlNode input) {
        return get(input, "Event");
    }

    protected String get(XmlNode input, String nodename) {
        XmlNode node = input.getSubNode(nodename);
        return node == null ? null : node.getContent();
    }

    protected boolean contains(XmlNode input, String nodename) {
        if (input == null) {
            return false;
        }
        XmlNode node = input.getSubNode(nodename);
        return node == null ? false : true;
    }

    /**
     * 初始化方法
     */
    protected void init() {
        WeiXinParserUtil.addXmlConvert((WeiXinConvert) this);
    }
}
