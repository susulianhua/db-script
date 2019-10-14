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
package org.tinygroup.weixin.impl;

import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinSession;

/**
 * 默认的微信上下文实现
 *
 * @author yancheng11334
 */
public class WeiXinContextDefault extends ContextImpl implements WeiXinContext {
    /**
     *
     */
    private static final long serialVersionUID = -5255507949527540557L;

    public <INPUT> INPUT getInput() {
        return get(DEFAULT_INPUT_NAME);
    }

    public <INPUT> void setInput(INPUT input) {
        put(DEFAULT_INPUT_NAME, input);
    }

    public <OUTPUT> OUTPUT getOutput() {
        return get(DEFAULT_OUTPUT_NAME);
    }

    public <OUTPUT> void setOutput(OUTPUT output) {
        put(DEFAULT_OUTPUT_NAME, output);
    }

    public WeiXinSession getWeiXinSession() {
        return get(WEIXIN_SESSION);
    }

}
