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
package org.tinygroup.threadgroup;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 抽象处理器
 *
 * @author luoguo
 */
public abstract class AbstractProcessor implements Processor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProcessor.class);
    private String name;
    private MultiThreadProcessor multiThreadProcess;
    private ExceptionCallBack exceptionCallBack = null;

    public AbstractProcessor(String name) {
        this.name = name;
    }

    protected abstract void action() throws Exception;

    public void setExceptionCallBack(ExceptionCallBack exceptionCallBack) {
        this.exceptionCallBack = exceptionCallBack;
    }

    public String getName() {
        return name;
    }

    public void run() {
        try {
            LOGGER.logMessage(LogLevel.DEBUG, "线程<{}-{}>运行开始...", multiThreadProcess.getName(), name);
            action();
            LOGGER.logMessage(LogLevel.DEBUG, "线程<{}-{}>运行结束", multiThreadProcess.getName(), name);
        } catch (Exception e) {
            LOGGER.errorMessage(e.getMessage(), e);
            if (exceptionCallBack != null) {
                exceptionCallBack.callBack(this, e);
            }
        } finally {
            multiThreadProcess.threadDone();
        }
    }

    public void setMultiThreadProcess(MultiThreadProcessor multiThreadProcess) {
        this.multiThreadProcess = multiThreadProcess;
    }
}
