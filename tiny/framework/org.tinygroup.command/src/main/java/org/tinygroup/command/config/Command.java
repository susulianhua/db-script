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

import java.util.List;

/**
 * 命令，包含了多个Goal
 *
 * @author luoguo
 */
@XStreamAlias("command")
public class Command {
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("short-description")
    private String shortDescription;
    private String description;

    @XStreamAlias("command-goals")
    private List<CommandGoal> commandGoals;

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CommandGoal> getCommandGoals() {
        return commandGoals;
    }

    public void setCommandGoals(List<CommandGoal> commandGoals) {
        this.commandGoals = commandGoals;
    }
}
