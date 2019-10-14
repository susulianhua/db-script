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
package org.tinygroup.weixin.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinConvert;
import org.tinygroup.weixin.WeiXinConvertMode;

/**
 * json报文转换器
 *
 * @author yancheng11334
 */
public class JsonParser extends AbstractParser {


    public <T> T parse(String result, WeiXinContext context, WeiXinConvertMode mode) {
        return parse(JSON.parseObject(result), context, mode);
    }

    @SuppressWarnings("unchecked")
    private <T> T parse(JSONObject result, WeiXinContext context, WeiXinConvertMode mode) {
        for (WeiXinConvert convert : convertList) {
            if (checkConvertMode(convert, mode) && convert.isMatch(result, context)) {
                return (T) convert.convert(result, context);
            }
        }
        return null;
    }

}
