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
package org.tinygroup.menucommand;

/**
 * 命令处理结果
 *
 * @author yancheng11334
 */
public class CommandResult {

    /**
     * 命令反馈信息
     */
    private String message;

    /**
     * 菜单Id
     */
    private String menuId;

    public CommandResult() {

    }

    public CommandResult(String message, String menuId) {
        this.message = message;
        this.menuId = menuId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "CommandResult [message=" + message + ", menuId=" + menuId + "]";
    }

}
