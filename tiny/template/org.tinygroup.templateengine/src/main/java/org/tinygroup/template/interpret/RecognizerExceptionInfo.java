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

import org.tinygroup.template.Macro;
import org.tinygroup.template.TemplateExceptionInfo;
import org.tinygroup.template.listener.Point;

/**
 * 处理Recognizer的异常情况
 *
 * @author yancheng11334
 */
public class RecognizerExceptionInfo implements TemplateExceptionInfo {

    private String reason;
    private String fileInfo;
    private Point start;

    public RecognizerExceptionInfo(String reason, String fileInfo, int line, int charPositionInLine) {
        super();
        this.reason = reason;
        this.fileInfo = fileInfo;
        this.start = new Point(line, charPositionInLine);
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public String getReason() {
        return reason;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return null;
    }

    public String getText() {
        return null;
    }

    public boolean isMacroException() {
        return false;
    }

    public Macro getMacroInfo() {
        return null;
    }

}
