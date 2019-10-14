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
package org.tinygroup.flow.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.tinygroup.commons.tools.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 下一步执行的节点
 *
 * @author luoguo
 */
@XStreamAlias("next-node")
public class NextNode implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1443588374203359350L;
    /**
     * @XStreamAsAttribute
     * @XStreamAlias("el-condition")
     */
    private String el;// 结果节点标识，可以是正则表达式
    @XStreamAsAttribute
    @XStreamAlias("exception-type")
    private String exceptionType;// 异常类型
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String title;
    private String description;

    @XStreamAsAttribute
    @XStreamAlias("next-node-id")
    private String nextNodeId;// 下一步要执行的节点标识，可以用a:b的形式引用调用外部的流程,a为flowId,b为NodeId

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEl() {
        return el;
    }

    public void setEl(String el) {
        this.el = el;
    }

    public String getNextNodeId() {
        return nextNodeId;
    }

    public void setNextNodeId(String nextNodeId) {
        this.nextNodeId = nextNodeId;
    }

    public List<String> getExceptionTypes() {
        List<String> exceptionTypes = new ArrayList<String>();
        if (!StringUtil.isBlank(exceptionType)) {
            String[] exceptionTypeArray = exceptionType.split(",");
            for (String str : exceptionTypeArray) {
                exceptionTypes.add(str);
            }
        }

        return exceptionTypes;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

}
