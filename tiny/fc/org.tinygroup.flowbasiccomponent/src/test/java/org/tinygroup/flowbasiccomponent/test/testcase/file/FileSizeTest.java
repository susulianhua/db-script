/**
 * Copyright (c) 2012-2016, www.tinygroup.org (luo_guo@icloud.com).
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
package org.tinygroup.flowbasiccomponent.test.testcase.file;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.flowbasiccomponent.test.AbstractFlowComponent;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.VFS;

/**
 * @author qiucn
 */
public class FileSizeTest extends AbstractFlowComponent {

    private static Logger LOGGER = LoggerFactory.getLogger(FileSizeTest.class);

    /**
     *
     */
    public void testGetFileSize() {
        Context context = ContextFactory.getContext();
        context.put("filePath", VFS.resolveFile("src/test/resources/user.xml")
                .getAbsolutePath());
        context.put("resultKey", "result");
        Event e = Event.createEvent("FileSizeFlow", context);
        cepcore.process(e);
        LOGGER.logMessage(LogLevel.INFO, "文件大小:{}", e.getServiceRequest().getContext().get("result"));
//        assertEquals(192, Integer.valueOf(e.getServiceRequest().getContext().get("result").toString()).intValue());
    }

}
