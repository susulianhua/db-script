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
package org.tinygroup.template;

import junit.framework.TestCase;
import org.tinygroup.commons.io.ByteArrayOutputStream;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.FileObjectResourceLoader;
import org.tinygroup.template.loader.FileResourceManager;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectProcessor;
import org.tinygroup.vfs.VFS;
import org.tinygroup.vfs.impl.filter.FileNameFileObjectFilter;

/*
 * 执行/src/test/resources/templateJunitRight中的测试用例，以Junit方式。
 * 本次执行的都是正确格式输入的
 * */
public class JetTemplateAssertEqualsTest extends TestCase {

    TemplateEngine engine;//模板引擎
    FileResolver fileResolver;

    //执行每一个测试方法前都会调用的方法 -- 用于初始化
    public void setUp() {
        Runner.init("application.xml", null);

        //代码方式构造模板引擎实例
        engine = new TemplateEngineDefault();
        FileObjectResourceLoader resourceLoader = new FileObjectResourceLoader("jetx", null, null);
        FileResourceManager fileResourceManager = new FileResourceManager();
        engine.addResourceLoader(resourceLoader);
        resourceLoader.setFileResourceManager(fileResourceManager);
        fileResourceManager.addResources(engine, "src/test/resources", ".jetx", null, ".component");
    }

    //每个测试方法执行完以后主动调用的方法 -- 用于清理或释放资源
    public void tearDown() {

    }

    /**
     * 过滤Windows系统开发的编码为UTF-8(BOM)
     * @param result
     * @return
     */
    private String filterString(String result) {
        if (result != null) {
            char[] c = result.toCharArray();
            if (c.length >= 1 && (int) c[0] == 65279) {
                return new String(c, 1, c.length - 1);
            }
        }
        return result;
    }

    public void testTemplateJunit() {
        //忽略的jetx一般供别人调用，自己不做渲染
        final String[] skipJetxs = {"include-sub1.jetx", "ptest1.jetx"};

        FileObject fileObject = VFS.resolveFile("src/test/resources");
        fileObject.foreach(new FileNameFileObjectFilter(".*\\.jetx", true), new FileObjectProcessor() {
            public void process(FileObject fileObject) {
                try {
                    for (String fileName : skipJetxs) {
                        if (fileObject.getFileName().equals(fileName)) {
                            return;
                        }
                    }
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    System.out.println(String.format("正在测试模板文件：%s\n", fileObject.getAbsolutePath()));
                    engine.renderTemplateWithOutLayout(fileObject.getPath(), new TemplateContextDefault(), outputStream);
                    String result = new String(outputStream.toByteArray().toByteArray(), "UTF-8");
                    String expectResult = FileUtil.readStreamContent(VFS.resolveFile(fileObject.getAbsolutePath() + ".txt").getInputStream(), "UTF-8");

                    result = filterString(result);
                    assertEquals(result, expectResult);//断言方式比对实际输出和预期输出
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("测试模板文件fail：" + fileObject.getAbsolutePath() + " 相对路径：" + fileObject.getPath());
                }
            }
        });

    }
}
