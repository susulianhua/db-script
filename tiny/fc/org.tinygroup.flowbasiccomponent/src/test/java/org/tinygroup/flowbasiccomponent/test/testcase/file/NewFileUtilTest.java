package org.tinygroup.flowbasiccomponent.test.testcase.file;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.flowbasiccomponent.test.AbstractFlowComponent;
import org.tinygroup.flowbasiccomponent.test.Org;
import org.tinygroup.flowbasiccomponent.test.User;
import org.tinygroup.vfs.VFS;

import java.util.ArrayList;
import java.util.List;

public class NewFileUtilTest extends AbstractFlowComponent {

    /**
     * 对象转文件
     */
    public void testObject2File() {
        Context context = ContextFactory.getContext();
        User u = new User();
        u.setId("123456789");
        context.put("user", u);
        context.put("key", "user");
        context.put("filePath", VFS.resolveFile("src/test/resources/user4.xml")
                .getAbsolutePath());
        Event e = Event.createEvent("Object2FileTestFlow", context);
        cepcore.process(e);

        deleteFile(VFS.resolveFile("src/test/resources/user4.xml")
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
        context.put("filePath", VFS.resolveFile("src/test/resources/user5.xml")
                .getAbsolutePath());
        Event e = Event.createEvent("ObjectAttr2FileTestFlow", context);
        cepcore.process(e);

        deleteFile(VFS.resolveFile("src/test/resources/user5.xml")
                .getAbsolutePath());
    }

    /**
     * 对象的属性转文件
     */
    public void testObjectAttrList2File() {
        Context context = ContextFactory.getContext();
        Org o = new Org();

        List<User> userList = new ArrayList<User>();
        User u = new User();
        u.setId("123456789");
        userList.add(u);
        User u1 = new User();
        u1.setId("987654321");
        userList.add(u1);
        o.setUserList(userList);
        o.setUser(u);

        context.put("user", u);
        context.put("org", o);
        context.put("el", "org.userList");
        context.put("filePath", VFS.resolveFile("src/test/resources/user6.xml")
                .getAbsolutePath());
        Event e = Event.createEvent("ObjectAttr2FileTestFlow", context);
        cepcore.process(e);

        deleteFile(VFS.resolveFile("src/test/resources/user6.xml")
                .getAbsolutePath());
    }

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
