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
package org.tinygroup.weixinmeterial.convert.json;

import com.alibaba.fastjson.JSONObject;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinConvertMode;
import org.tinygroup.weixin.convert.json.AbstractJSONObjectConvert;
import org.tinygroup.weixinmeterial.result.NewsListResult;

public class NewsListResultConvert extends AbstractJSONObjectConvert {

    //获取素材列表结果比较特殊：图文和其他素材外部结构一致，只是内部列表元素结构不同。
//	public boolean isMatch(JSONObject input) {
//		try{
//			if(input.containsKey("item")){
//				JSONArray array = input.getJSONArray("item");
//				return array.getJSONObject(0).containsKey("content");
//			}
//		}catch(Exception e){
//			//忽略异常
//			return false;
//		}
//		
//		return false;
//	}

    public NewsListResultConvert() {
        super(NewsListResult.class);
    }

    public WeiXinConvertMode getWeiXinConvertMode() {
        return WeiXinConvertMode.SEND;
    }

    protected boolean checkMatch(JSONObject input, WeiXinContext context) {
        return input.containsKey("item");
    }

}
