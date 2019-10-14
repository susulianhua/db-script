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

import org.apache.commons.lang.StringUtils;
import org.tinygroup.template.TemplateExceptionInfo;

/**
 * 简单格式错误的默认实现输出
 *
 * @author yancheng11334
 */
public class DefaultExceptionInfoFormater extends SimpleExceptionInfoFormater {

    protected void buildFileInfo(TemplateExceptionInfo info, StringBuilder sb) {
        if (info.getFileInfo() != null) {
            if (info.isMacroException()) {
                sb.append("报错宏文件路径:").append(info.getFileInfo()).append(" 报错宏名称:").append(info.getMacroInfo().getName());
            } else {
                sb.append("报错文件路径:").append(info.getFileInfo());
            }
            sb.append("\n");
        }

    }

    protected void buildReason(TemplateExceptionInfo info, StringBuilder sb) {
        if (info.getReason() != null) {
            sb.append("报错原因:").append(info.getReason()).append("\n");
        }

    }

    protected void buildPlace(TemplateExceptionInfo info, StringBuilder sb) {
        if (info.getStart() != null) {
            sb.append("报错位置:[").append(info.getStart().getY()).append(",").append(info.getStart().getX()).append("]");
            if (info.getEnd() != null) {
                sb.append("-[").append(info.getEnd().getY()).append(",").append(info.getEnd().getX()).append("]");
            }
            sb.append("\n");
        }


    }

    protected void buildText(TemplateExceptionInfo info, StringBuilder sb) {
        if (info.getText() != null) {
            sb.append(StringUtils.leftPad("", 20, "=")).append("\n");  //分隔线
            sb.append(info.getText()).append("\n");
            sb.append(StringUtils.leftPad("", 20, "=")).append("\n\n");  //分隔
        }
    }

}
