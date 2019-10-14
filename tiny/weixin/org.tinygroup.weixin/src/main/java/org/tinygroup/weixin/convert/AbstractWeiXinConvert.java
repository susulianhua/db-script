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

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinConvert;
import org.tinygroup.weixin.WeiXinConvertMode;
import org.tinygroup.weixin.common.UrlConfig;

public abstract class AbstractWeiXinConvert implements WeiXinConvert {

    protected Class<?> clazz;
    private int priority;

    public AbstractWeiXinConvert(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getCalssType() {
        return clazz;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int compareTo(WeiXinConvert o) {
        if (o.getPriority() == getPriority()) {
            return 0;
        } else if (o.getPriority() < getPriority()) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * 检查返回结果是否和当前处理器匹配
     *
     * @param <INPUT>
     * @param input
     * @param context
     * @return
     */
    protected <INPUT> boolean checkResultType(INPUT input, WeiXinContext context) {
        if (getWeiXinConvertMode() != WeiXinConvertMode.SEND || context == null) {
            return true;
        }
        UrlConfig config = context.get(UrlConfig.DEFAULT_CONTEXT_NAME);
        if (config != null && !StringUtil.isEmpty(config.getResultTypes())) {
            String[] types = config.getResultTypes().split(",");
            String name = getCalssType().getName();
            for (String type : types) {
                if (type.equals(name)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

}
