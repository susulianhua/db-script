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
package org.tinygroup.database.sequence;

import org.tinygroup.database.ProcessorManager;
import org.tinygroup.database.config.sequence.Sequence;
import org.tinygroup.database.config.sequence.Sequences;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * sequence处理器
 *
 * @author renhui
 */
public interface SequenceProcessor {

    void addSequences(Sequences sequences);

    void removeSequences(Sequences sequences);

    Sequence getSequence(String sequenceName);

    String getCreateSql(String sequenceName, String language);

    String getDropSql(String sequenceName, String language);

    List<String> getCreateSql(String language);

    List<String> getDropSql(String language);

    List<Sequence> getSequences(String language);

    boolean checkSequenceExist(String language, Sequence sequence, Connection connection) throws SQLException;

    ProcessorManager getProcessorManager();

    void setProcessorManager(ProcessorManager processorManager);
}
