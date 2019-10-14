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
 * @author qiucn
 */
public class Object2FileTest extends AbstractFlowComponent {

    /**
     * 对象转文件
     */
    public void testObject2File() {
        Context context = ContextFactory.getContext();
        User u = new User();
        u.setId("123456789");
        context.put("user", u);
        context.put("key", "user");
        context.put("filePath", VFS.resolveFile("src/test/resources/user2.xml")
                .getAbsolutePath());
        Event e = Event.createEvent("Object2FileTestFlow", context);
        cepcore.process(e);

        deleteFile(VFS.resolveFile("src/test/resources/user2.xml")
                .getAbsolutePath());
    }

    /**
     * 对象的属性转文件
     */
    public void testObjectAttr2File() {
        Context context = ContextFactory.getContext();
        User u = new User();
        u.setId("123456789");
        context.put("user", u);
        context.put("el", "user.id");
        context.put("filePath", VFS.resolveFile("src/test/resources/user3.xml")
                .getAbsolutePath());
        Event e = Event.createEvent("ObjectAttr2FileTestFlow", context);
        cepcore.process(e);

        deleteFile(VFS.resolveFile("src/test/resources/user3.xml")
                .getAbsolutePath());
    }
}
