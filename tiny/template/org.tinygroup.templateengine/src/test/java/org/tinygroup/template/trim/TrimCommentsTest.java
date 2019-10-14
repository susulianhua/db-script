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
package org.tinygroup.template.trim;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.io.ByteArrayOutputStream;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectProcessor;
import org.tinygroup.vfs.VFS;
import org.tinygroup.vfs.impl.filter.FileNameFileObjectFilter;

/**
 * Created by BYSocket on 2015/12/1.
 */
public class TrimCommentsTest extends TestCase {

    TemplateEngine engine;//模板引擎
    FileResolver fileResolver;

    FileObject fileObject = VFS.resolveFile("src/test/resources/trim");

    public void setUp() {
        Runner.init("application.xml", null);
        engine = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean("templateEngine");
        fileResolver = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean("fileResolver");
        engine.setCompactMode(true);
        fileResolver.getScanningPaths().add("src/test/resources/trim");
    }

    /**
     * Trim 注释、Set指令和以下指令生成的多余\r\n:
     * #if #else #elseif #end #eol ${}
     * #for #foreach #while #break #continue #stop
     * #include #@call
     */
    public void testCommentAll() {
        fileObject.foreach(
                new FileNameFileObjectFilter("allnewline\\.page", true), new FileObjectProcessor() {
                    public void process(FileObject fileObject) {
                        try {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            engine.renderTemplateWithOutLayout(fileObject.getPath(), new TemplateContextDefault(), outputStream);
                            String result = new String(outputStream.toByteArray().toByteArray(), "UTF-8");
                            System.out.println(result);
                            String expectResult = FileUtil.readStreamContent(VFS.resolveFile(fileObject.getAbsolutePath() + ".txt").getInputStream(), "UTF-8");
                            assertEquals(result, expectResult);
                        } catch (Exception e) {
                            // e.printStackTrace();
                        }
                    }
                });
    }
}
