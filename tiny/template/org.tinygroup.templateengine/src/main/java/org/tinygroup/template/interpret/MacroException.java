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
package org.tinygroup.template.interpret;

import org.antlr.v4.runtime.ParserRuleContext;
import org.tinygroup.template.Macro;
import org.tinygroup.template.TemplateException;

/**
 * 宏异常，解决宏和页面报错的提示路径问题
 *
 * @author yancheng11334
 */
public class MacroException extends TemplateException {

    /**
     *
     */
    private static final long serialVersionUID = -6726495316708790421L;
    private transient Macro macro;


    public MacroException(Macro macro, Exception e) {
        super(e);
        this.macro = macro;
    }

    /**
     * 更新异常信息块
     *
     * @param context
     * @param fileInfo
     */
    public void updateExceptionInfo(ParserRuleContext context, String fileInfo) {
        if (macro != null) {
            if (exceptionInfos.isEmpty()) {
                exceptionInfos.add(new MacroExceptionInfo(context, detailMessage, fileInfo, macro));
            } else {
                //得到最近插入的异常信息块
                AbstractTemplateExceptionInfo info = (AbstractTemplateExceptionInfo) exceptionInfos.get(exceptionInfos.size() - 1);
                if (info.isMacroException() && info.getMacroInfo().equals(macro)) {
                    //更新ParserRuleContext
                    //info.updateParserRuleContext(context);
                } else {
                    //插入记录
                    exceptionInfos.add(new MacroExceptionInfo(context, detailMessage, fileInfo, macro));
                }
            }
        }
    }

    /**
     * 得到宏
     *
     * @return
     */
    public Macro getMacro() {
        return macro;
    }

}
