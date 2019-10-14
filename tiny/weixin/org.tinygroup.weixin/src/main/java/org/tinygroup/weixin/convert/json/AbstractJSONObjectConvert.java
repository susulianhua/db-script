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
package org.tinygroup.weixin.convert.json;

import com.alibaba.fastjson.JSONObject;
import org.tinygroup.convert.objectjson.fastjson.JsonToObject;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinConvert;
import org.tinygroup.weixin.convert.AbstractWeiXinConvert;
import org.tinygroup.weixin.exception.WeiXinException;
import org.tinygroup.weixin.util.WeiXinParserUtil;

/**
 * 微信json转换基类
 *
 * @author yancheng11334
 */
public abstract class AbstractJSONObjectConvert extends AbstractWeiXinConvert {

    public AbstractJSONObjectConvert(Class<?> clazz) {
        super(clazz);
    }

    /**
     * 初始化方法
     */
    protected void init() {
        WeiXinParserUtil.addJsonConvert((WeiXinConvert) this);
    }

    public <INPUT> boolean isMatch(INPUT input, WeiXinContext context) {
        if (input instanceof JSONObject) {
            if (checkResultType(input, context)) {
                return checkMatch((JSONObject) input, context);
            }
        }
        return false;
    }

    public <OUTPUT, INPUT> OUTPUT convert(INPUT input, WeiXinContext context) {
        return convertJSON((JSONObject) input, context);
    }

    /**
     * 根据报文内容进行判断
     *
     * @param input
     * @param context
     * @return
     */
    protected abstract boolean checkMatch(JSONObject input, WeiXinContext context);

    /**
     * 转换JSONObject为业务对象
     *
     * @param <OUTPUT>
     * @param input
     * @param context
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected <OUTPUT> OUTPUT convertJSON(JSONObject input, WeiXinContext context) {
        try {
            JsonToObject jsonToken = new JsonToObject(clazz);
            return (OUTPUT) jsonToken.convert(input.toJSONString());
        } catch (Exception e) {
            throw new WeiXinException(String.format("%s convert to class:%s is failed!", input.toJSONString(), clazz.getName()), e);
        }
    }
}
