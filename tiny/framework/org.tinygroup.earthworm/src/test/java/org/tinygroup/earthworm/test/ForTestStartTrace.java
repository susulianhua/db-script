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
import org.tinygroup.earthworm.impl.OpenSampleTrier;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class ForTestStartTrace {
    private static Logger LOGGER = LoggerFactory.getLogger(ForTestStartTrace.class);

    public static void main(String[] args) {
        EarthWorm.setSampleTrier(OpenSampleTrier.class.getName());
        EarthWorm.startTrace("", "default1");
        EarthWorm.endTrace();
        LOGGER.infoMessage("=============");
        LOGGER.infoMessage("=============");
        EarthWorm.startTrace("", "default2");
        LOGGER.infoMessage("=============");
        EarthWorm.startTrace("", "default3");
        LOGGER.infoMessage("=============");
        EarthWorm.endTrace();
        LOGGER.infoMessage("=============");
        EarthWorm.startTrace("", "default4");
        LOGGER.infoMessage("=============");
        EarthWorm.endTrace();
        LOGGER.infoMessage("=============");
        EarthWorm.endTrace();
    }
}
