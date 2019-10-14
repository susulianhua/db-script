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
package org.tinygroup.codegen;

import com.thoughtworks.xstream.XStream;
import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.codegen.config.CodeGenMetaData;
import org.tinygroup.codegen.util.CodeGenUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.xstream.XStreamFactory;

import java.io.File;

public class CodeGenTest extends TestCase {
    String testJavaPath;
    String testResourcePath;

    private CodeGenerator generator;

    protected void setUp() throws Exception {
        super.setUp();
        AbstractTestUtil.init(null, true);
        generator = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean("codeGenerator");
    }

    protected void tearDown() throws Exception {
        File javaFile = new File(testJavaPath + CodeGenUtil.packageToPath("org.tinygroup.codegen") + "HelloWorld.java");
        assertTrue(javaFile.exists());
        System.gc();
        javaFile.deleteOnExit();
        File xmlFile = new File(testResourcePath + "test" + File.separator + "helloworld.xml");
        assertTrue(xmlFile.exists());
        xmlFile.deleteOnExit();
    }


    public void testCodeGen() throws Exception {

        XStream xStream = XStreamFactory.getXStream();
        xStream.setClassLoader(getClass().getClassLoader());
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(CodeGenMetaData.class);
        CodeGenMetaData metaData = (CodeGenMetaData) xStream.fromXML(getClass().getResourceAsStream("/test.codegen.xml"));
        Context context = ContextFactory.getContext();
        String projectPath = System.getProperty("user.dir");
        testJavaPath = projectPath + File.separator + "src" + File.separator + "test" + File.separator + "java" + File.separator;
        testResourcePath = projectPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator;
        context.put(CodeGenerator.JAVA_ROOT, projectPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator);
        context.put(CodeGenerator.JAVA_RES_ROOT, projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator);
        context.put(CodeGenerator.JAVA_TEST_ROOT, testJavaPath);
        context.put(CodeGenerator.JAVA_TEST_RES_ROOT, testResourcePath);
        context.put(CodeGenerator.CODE_META_DATA, metaData);
        context.put("beanPackageName", "org.tinygroup.codegen");
        context.put("className", "HelloWorld");
        context.put(CodeGenerator.ABSOLUTE_PATH, testResourcePath);
        generator.generate(context);
    }

}
