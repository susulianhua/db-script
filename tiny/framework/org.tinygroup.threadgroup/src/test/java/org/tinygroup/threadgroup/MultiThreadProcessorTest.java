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

import junit.framework.TestCase;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class MultiThreadProcessorTest extends TestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadProcessorTest.class);

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testStart() {
        MultiThreadProcessor processors = new MultiThreadProcessor("number add");
        for (int i = 0; i < 10; i++) {
            processors.addProcessor(new NumberAdd("add" + i));
        }
        long startTime = System.currentTimeMillis();
        processors.start();
        long endTime = System.currentTimeMillis();
        LOGGER.logMessage(LogLevel.INFO, "执行时间：{}", endTime - startTime);
        assertEquals(10000, NumberAdd.sumValue);
    }

}
