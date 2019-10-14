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
package org.tinygroup.vfs.impl;

import junit.framework.TestCase;
import org.tinygroup.commons.file.FileDealUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileUtils;
import org.tinygroup.vfs.VFS;

import java.io.File;
import java.io.IOException;

/**
 * fileObject的测试用例
 *
 * @author renhui
 */
public class FileObjectImplTest extends TestCase {

    public void testFileObject() throws IOException {
        String path = getClass().getResource("/test/0.html").getFile();
        FileObject fileObject = VFS.resolveFile(path);
        FileUtils.printFileObject(fileObject);
    }


    public void testGetSubFile() {

        String projectPath = System.getProperty("user.dir");
        String testResourcePath = projectPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator;
        FileObject fileObject = VFS.resolveFile(testResourcePath);
        FileObject subFile = fileObject.getFileObject("/");
        assertEquals(fileObject, subFile);
        subFile = fileObject.getFileObject("/test");
        assertTrue(subFile != null);
        subFile = fileObject.getFileObject("/test/0.html");
        assertTrue(subFile != null);

    }

    /**
     * 测试删除操作
     * @throws Exception
     */
    public void testDelete() throws Exception {
        String projectPath = System.getProperty("user.dir");
        String testResourcePath = projectPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator;

        //创建文件
        String filePath = testResourcePath + "NewFileObjectImpl.txt";
        FileObject f1 = VFS.resolveFile(filePath);
        assertEquals(false, f1.isExist());

        FileDealUtil.write(new File(filePath), "hello");
        assertEquals(true, f1.isExist());

        //删除文件
        f1.delete();
        assertEquals(false, f1.isExist());

        //创建目录
        String dirPath = testResourcePath + "hello" + File.separator;
        String dirPath2 = dirPath + "sub" + File.separator;

        FileObject d1 = VFS.resolveFile(dirPath);
        assertEquals(false, d1.isExist());

        //建立如下层次的目录结构
        //--hello
        //----hello.txt
        //----sub
        //------sub.txt
        new File(dirPath2).mkdirs();
        FileDealUtil.write(new File(dirPath2 + "sub.txt"), "sub is ok!");
        FileDealUtil.write(new File(dirPath + "hello.txt"), "hello is ok!");

        assertEquals(true, d1.isExist());
        assertEquals(true, VFS.resolveFile(dirPath2).isExist());
        assertEquals(true, VFS.resolveFile(dirPath2 + "sub.txt").isExist());
        assertEquals(true, VFS.resolveFile(dirPath + "hello.txt").isExist());

        //删除目录
        d1.delete();

        assertEquals(false, d1.isExist());
        assertEquals(false, VFS.resolveFile(dirPath2).isExist());
        assertEquals(false, VFS.resolveFile(dirPath2 + "sub.txt").isExist());
        assertEquals(false, VFS.resolveFile(dirPath + "hello.txt").isExist());

    }


}
