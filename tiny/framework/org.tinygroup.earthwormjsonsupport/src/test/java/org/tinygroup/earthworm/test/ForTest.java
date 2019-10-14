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
package org.tinygroup.earthworm.test;

import org.tinygroup.earthworm.EarthWorm;
import org.tinygroup.earthworm.EarthWormJsonSupport;
import org.tinygroup.earthworm.impl.OpenSampleTrier;
import org.tinygroup.earthworm.util.EarthWormConfig;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class ForTest {
    private static Logger LOGGER = LoggerFactory.getLogger(ForTest.class);

    public static void main(String[] args) {
        EarthWormConfig.setLogSupportClass(EarthWormJsonSupport.class.getName());
        EarthWormConfig.setSampleTrierClass(OpenSampleTrier.class.getName());
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.startTrace("", "default");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.startLocal("hhhh");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.endLocal();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.startRpc("hello");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.rpcClientSend();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.rpcClientRecv();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.startLocal("hhhh");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.endLocal();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.startLocal("hhhh1");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.endLocal();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.startRpc("hello");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.rpcClientSend();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.rpcClientRecv();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.startLocal("hhhh");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.endLocal();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.startLocal("hhhh1");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.endLocal();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.startLocal("hhhh");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.startLocal("hhhh1");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.endLocal();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.endLocal();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        EarthWorm.endTrace();
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
        LOGGER.logMessage(LogLevel.INFO, "====================");
    }
}
