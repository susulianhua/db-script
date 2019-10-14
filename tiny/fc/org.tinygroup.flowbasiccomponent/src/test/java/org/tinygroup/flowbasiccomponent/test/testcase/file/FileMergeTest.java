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
import org.tinygroup.vfs.VFS;

/**
 * @author qiucn
 */
public class FileMergeTest extends AbstractFlowComponent {

    /**
     * filePath3不为空
     */
    public void testFileMergeTo3() {
        Context context = ContextFactory.getContext();
        context.put("filePath1", VFS
                .resolveFile("src/test/resources/file1.xml").getAbsolutePath());
        context.put("filePath2", VFS
                .resolveFile("src/test/resources/file2.xml").getAbsolutePath());
        context.put("filePath3", VFS
                .resolveFile("src/test/resources/file3.xml").getAbsolutePath());
        Event e = Event.createEvent("fileMergeTestFlow", context);
        cepcore.process(e);
        deleteFile(VFS.resolveFile("src/test/resources/file3.xml")
                .getAbsolutePath());
    }

    /**
     * filePath3为空
     */
    public void testFileMergeTo1() {
        Context context = ContextFactory.getContext();
        context.put("filePath1", VFS
                .resolveFile("src/test/resources/file1.xml").getAbsolutePath());
        context.put("filePath2", VFS
                .resolveFile("src/test/resources/file2.xml").getAbsolutePath());
        context.put("filePath3", "");
        Event e = Event.createEvent("fileMergeTestFlow", context);
        cepcore.process(e);
    }
}
