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
package org.tinygroup.fileresolver;

import junit.framework.TestCase;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.fileresolver.impl.ExcludeContextFileFinder;
import org.tinygroup.fileresolver.impl.FileResolverImpl;
import org.tinygroup.fileresolver.impl.FullContextFileFinder;
import org.tinygroup.fileresolver.impl.FullContextFileRepositoryImpl;
import org.tinygroup.parser.Document;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlParser;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.io.File;
import java.io.IOException;

/**
 * 功能说明: FullContentFileResposity接口测试用例
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-2-22 <br>
 * <br>
 */
public class FullContentFileResposityTest extends TestCase {

    private static final String FILE_PATH = "/org/tinygroup/fileresolver/FileResolver.class";

    private static FullContextFileRepositoryImpl repository;

    private static boolean hasInited;

    void init() {
        repository = new FullContextFileRepositoryImpl();
        FileResolver fileResolver = new FileResolverImpl();
        FullContextFileFinder finder = new FullContextFileFinder();
        ExcludeContextFileFinder exFinder = new ExcludeContextFileFinder();
        XmlParser<String> parse = new XmlStringParser();
        Document<XmlNode> document = parse
                .parse("<full-context-file-finder>"
                        + "<file ext-name=\"class\" content-type=\"application/test\" />"
                        + "</full-context-file-finder>");
        Document<XmlNode> exDocument = parse
                .parse("<exclude-full-context-file-finder>"
                        + "<file ext-name=\"xml\" content-type=\"application/test\" />"
                        + "</exclude-full-context-file-finder>");
        exFinder.config(exDocument.getRoot(), null);
        repository.setExcludeContextFileFinder(exFinder);
        finder.setExcludeContextFileFinder(exFinder);
        finder.config(document.getRoot(), null);
        finder.setFullContextFileRepository(repository);
        fileResolver.addFileProcessor(finder);
        fileResolver.addResolvePath(FileResolverUtil.getClassPath(fileResolver));
        fileResolver.addIncludePathPattern("org\\.tinygroup\\.(.)*\\.jar");
        fileResolver.resolve();
    }

    protected void setUp() throws Exception {
        if (!hasInited) {
            init();
            hasInited = true;
        }

    }

    protected void tearDown() throws Exception {

    }

    public void testAddSearchPath() throws IOException {
        File director = new File("./test");
        if (!director.exists()) {
            director.mkdirs();
        }
        repository.addSearchPath("./test");

        File testFile = new File("./test/test.xml1");
        if (!testFile.exists()) {
            testFile.createNewFile();
        }

        FileObject fileObject = repository.getFileObject("/test.xml1");
        assertTrue(fileObject != null);


        testFile = new File("./test/test.xml");
        if (!testFile.exists()) {
            testFile.createNewFile();
        }

        fileObject = repository.getFileObject("/test.xml");
        assertTrue(fileObject == null);

        FileUtil.delete(director);

    }

    public void testGetFileObject() {
        FileObject fileObject = repository.getFileObject(FILE_PATH);
        assertEquals("FileResolver.class", fileObject.getFileName());
    }

    public void testRemoveFileObject() {
        FileObject beforeRemove = repository.getFileObject(FILE_PATH);
        assertTrue(beforeRemove != null);
        repository.removeFileObject(FILE_PATH);
        repository.addFileObject(FILE_PATH, beforeRemove);
    }

    public void testGetFileContentType() {
        assertEquals("application/test", repository.getFileContentType("class"));
    }

    public void testGetRootFileObject() {
        FileObject fileObject = repository.getRootFileObject(FILE_PATH);
        System.out.println(fileObject.getAbsolutePath());
        assertTrue(fileObject.getAbsolutePath().endsWith("classes"));
    }

}
