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
package org.tinygroup.docgen;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.docgen.function.StaticClassFunction;
import org.tinygroup.docgen.util.ImageUtil;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.loader.FileResourceManager;
import org.tinygroup.tinytestutil.AbstractTestUtil;

import java.io.File;
import java.io.FileOutputStream;

public class DocGenTest extends TestCase {

    private static DocumentGeneraterManager manager = null;
    private static FileResourceManager fileResourceManager;

    protected void setUp() throws Exception {
        super.setUp();
        AbstractTestUtil.init(null, true);
        if (manager == null) {
            manager = BeanContainerFactory.getBeanContainer(
                    this.getClass().getClassLoader()).getBean(
                    DocumentGeneraterManager.MANAGER_BEAN_NAME);
        }
        if (fileResourceManager == null) {
            fileResourceManager = BeanContainerFactory.getBeanContainer(
                    this.getClass().getClassLoader()).getBean(
                    "fileResourceManager");
        }
        TemplateEngine templateEngine = (TemplateEngine) manager.getFileGenerater("doc").getTemplateGenerater();
        templateEngine.addTemplateFunction(new StaticClassFunction("imageUtil", ImageUtil.class));
        fileResourceManager.addResources(templateEngine, "src/test/resources/", "docpage", null, "doctemplate");

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * 整体测试
     *
     * @throws Exception
     */
    public void testDocGenerate() throws Exception {
        File file = new File("test.xml");
        FileOutputStream outputStream = new FileOutputStream(file);
        Context context = new ContextImpl();
        String picData = ImageUtil.fileToBase64("src/test/resources/pic.jpg");
        context.put("picData", picData);
        manager.getFileGenerater("doc").generate("src/test/resources/test.docpage", context,
                outputStream);
        outputStream.close();
        file.delete();
    }

    /**
     * 书签，链接
     *
     * @throws Exception
     */
    public void testCommon() throws Exception {
        File file = new File("常用.xml");
        FileOutputStream outputStream = new FileOutputStream(file);
        manager.getFileGenerater("doc").generate("src/test/resources/common.docpage",
                new ContextImpl(), outputStream);
        outputStream.close();
        file.delete();
    }

    /**
     * 段落
     *
     * @throws Exception
     */
    public void testParagraph() throws Exception {
        File file = new File("段落.xml");
        FileOutputStream outputStream = new FileOutputStream(file);
        manager.getFileGenerater("doc").generate("src/test/resources/paragraph.docpage",
                new ContextImpl(), outputStream);
        outputStream.close();
        file.delete();
    }

    /**
     * 目录
     *
     * @throws Exception
     */
    public void testCatalogue() throws Exception {
        File file = new File("目录.xml");
        FileOutputStream outputStream = new FileOutputStream(file);
        manager.getFileGenerater("doc").generate("src/test/resources/catalogue.docpage",
                new ContextImpl(), outputStream);
        outputStream.close();
        file.delete();
    }

    /**
     * 图片
     *
     * @throws Exception
     */
    public void testPicture() throws Exception {
        File file = new File("图片.xml");
        FileOutputStream outputStream = new FileOutputStream(file);
        Context context = new ContextImpl();
        //context.put("imageUtil", ImageUtil.class);
        manager.getFileGenerater("doc").generate("src/test/resources/picture.docpage", context,
                outputStream);
        outputStream.close();
        file.delete();
    }

    /**
     * 表格
     *
     * @throws Exception
     */
    public void testTable() throws Exception {
        File file = new File("表格.xml");
        FileOutputStream outputStream = new FileOutputStream(file);
        manager.getFileGenerater("doc").generate("src/test/resources/table.docpage",
                new ContextImpl(), outputStream);
        outputStream.close();
        file.delete();
    }

    /**
     * 项目标号
     *
     * @throws Exception
     */
    public void testBullets() throws Exception {
        File file = new File("项目标号.xml");
        FileOutputStream outputStream = new FileOutputStream(file);
        manager.getFileGenerater("doc").generate("src/test/resources/bullets.docpage",
                new ContextImpl(), outputStream);
        outputStream.close();
        file.delete();
    }

    /**
     * 页眉，页脚，页码
     *
     * @throws Exception
     */
    public void testPageHeader() throws Exception {
        File file = new File("页眉页脚.xml");
        FileOutputStream outputStream = new FileOutputStream(file);
        manager.getFileGenerater("doc").generate("src/test/resources/pageHeaderTail.docpage",
                new ContextImpl(), outputStream);
        outputStream.close();
        file.delete();
    }

}
