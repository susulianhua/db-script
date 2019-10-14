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
import org.tinygroup.flowbasiccomponent.test.User;
import org.tinygroup.vfs.VFS;

/**
 * 文件转对象
 *
 * @author qiucn
 */
public class File2ObjectTest extends AbstractFlowComponent {

    public void testFile2Object() {
        Context context = ContextFactory.getContext();
        context.put("filePath", VFS.resolveFile("src/test/resources/user.xml")
                .getAbsolutePath());
        context.put("classPath", "org.tinygroup.flowbasiccomponent.test.User");
        context.put("resultKey", "result");
        context.put("result", "");
        Event e = Event.createEvent("File2ObjectTestFlow", context);
        cepcore.process(e);
        User u = e.getServiceRequest().getContext().get("result");
        assertEquals("100000000", u.getId());
    }

}
