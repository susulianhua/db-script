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
package org.tinygroup.logger.impl;

import java.util.ArrayList;
import java.util.List;

public class LogBuffer {
    /**
     * 日志事务深度
     */
    private int transactionDepth = 0;
    private List<Message> logMessages = new ArrayList<Message>();

    public List<Message> getLogMessages() {
        return logMessages;
    }

    public void increaseTransactionDepth() {
        transactionDepth++;
    }

    public void decreaseTransactionDepth() {
        transactionDepth--;
    }

    public int getTimes() {
        return transactionDepth;
    }

    public void addMessage(Message message) {
        logMessages.add(message);
    }

    public void reset() {
        transactionDepth = 0;
        logMessages.clear();
    }
}
