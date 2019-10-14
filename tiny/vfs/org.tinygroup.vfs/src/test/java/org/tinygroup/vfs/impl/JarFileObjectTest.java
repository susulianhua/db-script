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
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * jar fileobject 测试用例
 *
 * @author renhui
 */
public class JarFileObjectTest extends TestCase {
    private static void fileCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                in.close();
                outStream.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void testJarZipFileObject() throws Exception {
        fileCopy(new File("src/test/resources/vfs-0.0.1-SNAPSHOT.jar"),
                new File("src/test/resources/test.jar"));
        FileObject fileObject = VFS.resolveFile("src/test/resources/test.jar");
        FileObject fo = findFileObject(fileObject, "META-INF");
        try {
            // jar内部的文件夹虽然也被识别为jar，但不支持删除
            fo.delete();
        } catch (Exception e) {
        }
        assertTrue(fo.isExist());
        FileObject fo1 = findFileObject(fileObject, "VFS.class");
        try {
            // jar内部的文件虽然也被识别为jar，但不支持删除
            fo1.delete();
        } catch (Exception e) {
        }
        assertTrue(fo1.isExist());
        // 删除jar包，支持
        fileObject.delete();
        assertFalse(fileObject.isExist());
    }

    public void testJarFileObject() throws Exception {

        String path = getClass().getResource("/vfs-0.0.1-SNAPSHOT.jar")
                .getFile();
        FileObject fileObject = VFS.resolveFile(path);
        FileObject fo = findFileObject(fileObject, "VFS.class");
        if (fo != null) {
            InputStream inputStream = fo.getInputStream();
            byte[] buf = new byte[(int) fo.getSize()];
            inputStream.close();
            assertTrue(buf.length > 0);
        }
    }

    private FileObject findFileObject(FileObject fileObject, String name) {

        if (fileObject.getFileName().equals(name)) {
            return fileObject;
        } else {
            if (fileObject.isFolder() && fileObject.getChildren() != null) {
                for (FileObject fo : fileObject.getChildren()) {
                    FileObject f = findFileObject(fo, name);
                    if (f != null) {
                        return f;
                    }
                }
            }
        }
        return null;
    }

    public void testSubFile() {
        String path = getClass().getResource("/vfs-0.0.1-SNAPSHOT.jar")
                .getFile();
        FileObject fileObject = VFS.resolveFile(path);
        FileObject subFile = fileObject.getFileObject("/");
        assertEquals(fileObject, subFile);
        subFile = fileObject.getFileObject("/org");
        assertTrue(subFile != null);
        subFile = fileObject.getFileObject("/org/hundsun");
        assertTrue(subFile != null);
        subFile = fileObject
                .getFileObject("/org/hundsun/opensource/vfs/VFS.class");
        assertTrue(subFile != null);
        assertEquals("VFS.class", subFile.getFileName());
    }

    public void testWasJar() throws IOException {
        String path = "wsjar:file:"
                + getClass().getResource("/vfs-0.0.1-SNAPSHOT.jar").getFile();
        FileObject fileObject = VFS.resolveFile(path);
        FileObject fo = findFileObject(fileObject, "VFS.class");
        if (fo != null) {
            InputStream inputStream = fo.getInputStream();
            byte[] buf = new byte[(int) fo.getSize()];
            inputStream.close();
            assertTrue(buf != null);
        }
    }

}
