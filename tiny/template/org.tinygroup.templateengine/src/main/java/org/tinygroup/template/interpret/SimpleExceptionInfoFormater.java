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

import org.tinygroup.template.TemplateExceptionInfo;
import org.tinygroup.template.TemplateExceptionInfoFormater;

/**
 * 简单异常格式实现
 *
 * @author yancheng11334
 */
public abstract class SimpleExceptionInfoFormater implements TemplateExceptionInfoFormater {

    public String getMessage(TemplateExceptionInfo info) {
        StringBuilder sb = new StringBuilder();
        buildFileInfo(info, sb);
        buildReason(info, sb);
        buildPlace(info, sb);
        buildText(info, sb);
        return sb.toString();
    }

    /**
     * 实现报错文件路径输出
     *
     * @param info
     * @param sb
     */
    protected abstract void buildFileInfo(TemplateExceptionInfo info, StringBuilder sb);

    /**
     * 实现报错原因输出
     *
     * @param info
     * @param sb
     */
    protected abstract void buildReason(TemplateExceptionInfo info, StringBuilder sb);

    /**
     * 实现报错位置输出
     *
     * @param info
     * @param sb
     */
    protected abstract void buildPlace(TemplateExceptionInfo info, StringBuilder sb);

    /**
     * 实现报错内容输出
     *
     * @param info
     * @param sb
     */
    protected abstract void buildText(TemplateExceptionInfo info, StringBuilder sb);

}
