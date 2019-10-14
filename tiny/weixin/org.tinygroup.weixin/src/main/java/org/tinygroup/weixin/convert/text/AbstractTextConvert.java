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
package org.tinygroup.weixin.convert.text;

import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinConvert;
import org.tinygroup.weixin.convert.AbstractWeiXinConvert;
import org.tinygroup.weixin.exception.WeiXinException;
import org.tinygroup.weixin.util.WeiXinParserUtil;

/**
 * 文本类对象转换器(排除json、xml)
 *
 * @author yancheng11334
 */
public abstract class AbstractTextConvert extends AbstractWeiXinConvert {

    public AbstractTextConvert(Class<?> clazz) {
        super(clazz);
    }

    /**
     * 初始化方法
     */
    protected void init() {
        WeiXinParserUtil.addTextConvert((WeiXinConvert) this);
    }

    public <INPUT> boolean isMatch(INPUT input, WeiXinContext context) {
        if (input instanceof String) {
            if (checkResultType(input, context)) {
                return checkMatch((String) input, context);
            }
        }
        return false;
    }

    /**
     * 根据报文内容进行判断
     *
     * @param input
     * @param context
     * @return
     */
    protected abstract boolean checkMatch(String input, WeiXinContext context);

    public <OUTPUT, INPUT> OUTPUT convert(INPUT input, WeiXinContext context) {
        try {
            return convertString((String) input, context);
        } catch (Exception e) {
            throw new WeiXinException(String.format("%s convert to class:%s is failed!", input, clazz.getName()), e);
        }
    }

    /**
     * 转换String报文
     *
     * @param <OUTPUT>
     * @param input
     * @param context
     * @return
     */
    protected abstract <OUTPUT> OUTPUT convertString(String input, WeiXinContext context);
}
