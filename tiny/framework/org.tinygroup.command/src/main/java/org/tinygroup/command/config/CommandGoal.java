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
package org.tinygroup.command.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.tinygroup.command.CommandGoalExecutor;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.List;

/**
 * 命令的Goal
 *
 * @author luoguo
 */
@XStreamAlias("command-goal")
public class CommandGoal {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandGoal.class);
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("default-parameter")
    @XStreamAsAttribute
    private String defaultParameter;// 如果不指定参数，则认为是默认参数
    @XStreamAlias("short-description")
    private String shortDescription;
    private String description;
    private List<Parameter> parameters;
    @XStreamAsAttribute
    @XStreamAlias("class-name")
    private String className;

    // private transient CommandGoalExecutor commandGoalExecutor;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDefaultParameter() {
        return defaultParameter;
    }

    public void setDefaultParameter(String defaultParameter) {
        this.defaultParameter = defaultParameter;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public CommandGoalExecutor getCommandGoalExecutor() {
        return newInstance(className);
    }

    private CommandGoalExecutor newInstance(String className2) {
        try {
            return (CommandGoalExecutor) Class.forName(className2)
                    .newInstance();
        } catch (Exception e) {
            LOGGER.errorMessage("加载类<{0}>时出现异常!", e, className2);
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
