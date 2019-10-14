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
package org.tinygroup.convert;

import junit.framework.TestCase;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.convert.xsdjava.ClassToSchema;
import org.tinygroup.convert.xsdjava.SchemaToClass;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TestXsdJava extends TestCase {

    public void testClass2Schema() throws ConvertException {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(Classes.class);
        list.add(Student.class);
        list.add(Birthday.class);

        File src = new File("test");
        if (!src.exists()) {
            src.mkdir();
        }
        ClassToSchema classToSchema = new ClassToSchema(src);
        List<File> files = classToSchema.convert(list);
        assertTrue(files.size() > 0);
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
        src.delete();
    }

    public void tearDown() {
        File srcTest = new File("test");
        FileUtil.delete(srcTest);
    }

    public void testSchema2Class() throws ConvertException {
        File srcTest = new File("test");
        if (!srcTest.exists()) {
            srcTest.mkdir();
        }
        SchemaToClass schemaToClass = new SchemaToClass(
                "test",
                "src/test/resources/xjb",
                "org.tinygroup.convert");
        List<String> xsdFiles = new ArrayList<String>();
        try {
            File src = new File(getClass().getResource("/xsd").toURI());
            File[] subFiles = src.listFiles();
            if (subFiles != null) {
                for (File subFile : subFiles) {
                    xsdFiles.add(subFile.getAbsolutePath());
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        schemaToClass.convert(xsdFiles);
        srcTest.delete();
    }

    public void testSchema2ClassMuti() throws ConvertException {
        File srcTest = new File("test");
        if (!srcTest.exists()) {
            srcTest.mkdir();
        }
        try {
            File src = new File(getClass().getResource("/xsd").toURI());
            File[] subFiles = src.listFiles();
            if (subFiles != null) {
                for (File subFile : subFiles) {
                    List<String> xsdFiles = new ArrayList<String>();
                    SchemaToClass schemaToClass = new SchemaToClass(
                            "test",
                            "src/test/resources/xjb",
                            "com.abc."
                                    + subFile.getName().replaceAll("[.]", "_"));
                    xsdFiles.add(subFile.getAbsolutePath());
                    schemaToClass.convert(xsdFiles);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
