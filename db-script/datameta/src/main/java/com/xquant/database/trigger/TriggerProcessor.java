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
package com.xquant.database.trigger;

import com.xquant.database.ProcessorManager;
import com.xquant.database.config.trigger.Trigger;
import com.xquant.database.config.trigger.Triggers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 触发器处理
 *
 * @author renhui
 */
public interface TriggerProcessor {

    void addTriggers(Triggers triggers);

    void removeTriggers(Triggers triggers);

    Trigger getTrigger(String triggerName);

    String getCreateSql(String triggerName, String language);

    String getDropSql(String triggerName, String language);

    List<String> getCreateSql(String language);

    List<String> getDropSql(String language);

    List<Trigger> getTriggers(String language);

    boolean checkTriggerExist(String language, Trigger trigger, Connection connection) throws SQLException;

    ProcessorManager getProcessorManager();

    void setProcessorManager(ProcessorManager processorManager);
}
