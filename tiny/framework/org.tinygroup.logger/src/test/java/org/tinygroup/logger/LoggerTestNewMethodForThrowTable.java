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
package org.tinygroup.logger;

import junit.framework.TestCase;

public class LoggerTestNewMethodForThrowTable extends TestCase {

    static Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    public void testExceptionINFO() {
        try {
            generateException();
        } catch (Exception e) {
            logger.logMessage(LogLevel.INFO, "-------INFO-------", e);
        }

    }

    public void testExceptionDEBUG() {
        try {
            generateException();
        } catch (Exception e) {
            logger.logMessage(LogLevel.DEBUG, "-------DEBUG-------", e);
        }

    }

    public void testExceptionWARN() {
        try {
            generateException();
        } catch (Exception e) {
            logger.logMessage(LogLevel.WARN, "-------WARN-------", e);
        }

    }

    public void testExceptionERROR() {
        try {
            generateException();
        } catch (Exception e) {
            logger.logMessage(LogLevel.ERROR, "-------ERROR-------", e);
        }

    }


    private void generateException() {
        String a = null;
        a.indexOf(5);
    }
}
