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

import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinConvert;
import org.tinygroup.weixin.WeiXinConvertMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的解析集合类
 *
 * @author yancheng11334
 */
public abstract class AbstractParser {

    protected List<WeiXinConvert> convertList = new ArrayList<WeiXinConvert>();

    public void addWeiXinConvert(WeiXinConvert convert) {
        if (!convertList.contains(convert)) {
            convertList.add(convert);
            java.util.Collections.sort(convertList);
        }
    }

    public void removeWeiXinConvert(WeiXinConvert convert) {
        if (convertList.contains(convert)) {
            convertList.remove(convert);
            java.util.Collections.sort(convertList);
        }
    }

    /**
     * 判断转换器的类型是否匹配
     *
     * @param convert
     * @param mode
     * @return
     */
    public boolean checkConvertMode(WeiXinConvert convert, WeiXinConvertMode mode) {
        if (mode == null || mode == WeiXinConvertMode.ALL || convert.getWeiXinConvertMode() == WeiXinConvertMode.ALL) {
            return true;
        }
        return convert.getWeiXinConvertMode() == mode;
    }

    public abstract <T> T parse(String result, WeiXinContext context, WeiXinConvertMode mode);
}
