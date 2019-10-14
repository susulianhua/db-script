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
package org.tinygroup.template.executor;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.FileObjectResourceLoader;
import org.tinygroup.template.loader.FileResourceManager;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 用于直接对当前环境下的VM进行执行，并输出结果到控制台
 * Created by luoguo on 2014/6/7.
 */
public class TinyTemplateExecutor {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(TinyTemplateExecutor.class);
    private static final String DEFAULT_TEMPLATE_EXT_NAME = "page";
    private static final String DEFAULT_LAYOUT_EXT_NAME = "layout";
    private static final String DEFAULT_COMPONENT_EXT_NAME = "component";
    private static final String SPLIT_TAG = ";";

    public static void main(String[] args) throws TemplateException {
        if (args.length == 0) {
            System.out.println("Usage:\n\tTinyTemplateExecutor templateFile [relativePath] [absolutePath] [resources] [urlParameters]");
            return;
        }
        String relativePath = null;
        String absolutePath = null;
        String resources = null;
        String urlParameters = null;

        //解析参数
        if (args.length >= 1) {
            relativePath = args[0].replaceAll("\\\\", "/");
        }
        if (args.length >= 2) {
            absolutePath = args[1].replaceAll("\\\\", "/");
        }
        if (args.length >= 3) {
            resources = args[2].replaceAll("\\\\", "/");
        }
        if (args.length >= 4) {
            urlParameters = args[3];
        }
        Map<String, String> maps = parserStringParameter(urlParameters);

        //模板文件扩展名不能写死，需要根据模板文件动态获取
        final String templateExtFileName = StringUtil.defaultIfEmpty(getExtFileName(relativePath), DEFAULT_TEMPLATE_EXT_NAME);
        final String layoutExtFileName = DEFAULT_LAYOUT_EXT_NAME;
        final String componentExtFileName = DEFAULT_COMPONENT_EXT_NAME;


        //初始化模板引擎
        final TemplateEngine engine = new TemplateEngineDefault();


        //配置文件目录资源加载器
        FileObjectResourceLoader resourceLoader = new FileObjectResourceLoader(templateExtFileName, layoutExtFileName, componentExtFileName);
        resourceLoader.setFileResourceManager(new FileResourceManager());
        engine.addResourceLoader(resourceLoader);


        if (resources != null) {
            String[] ss = resources.split(SPLIT_TAG);
            //遍历resource目录的资源并注册
            for (String resource : ss) {
                FileObject fileObject = VFS.resolveFile(resource);
                //控制台不输出日志
                resourceLoader.getFileResourceManager().addResources(engine, fileObject, "." + templateExtFileName, "." + layoutExtFileName, "." + componentExtFileName, false);
            }
        }

        TemplateContext context = new TemplateContextDefault();
        //如果有用户自定义参数，放入模板上下文
        if (!CollectionUtil.isEmpty(maps)) {
            for (Entry<String, String> entry : maps.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
        }


        //渲染模板
        if (relativePath != null) {
            //如果只有一个，则只执行一个
            engine.renderTemplate(relativePath, context, System.out);
        }
    }


    //解析简单的String参数
    protected static Map<String, String> parserStringParameter(String urlParams) {
        Map<String, String> maps = new HashMap<String, String>();
        if (!StringUtil.isBlank(urlParams)) {
            String[] ss = urlParams.split("&");
            for (String pair : ss) {
                int n = pair.indexOf("=");
                if (n != -1) {
                    maps.put(pair.substring(0, n), pair.substring(n + 1, pair.length()));
                }
            }
        }
        return maps;
    }

    protected static String getJarFileName(FileObject fileObject) {
        if (fileObject != null) {
            String path = fileObject.getAbsolutePath();
            int n = path.lastIndexOf("!");
            if (n != -1) {
                return path.substring(0, n);
            } else {
                return path;
            }
        }
        return null;
    }

    protected static String getDir(String relativePath, String absolutePath) {
        if (relativePath == null || absolutePath == null) {
            return null;
        }
        return absolutePath.substring(0, absolutePath.length() - relativePath.length());
    }

    protected static String getProjectRoot(String dir) {
        if (dir == null) {
            return null;
        }
        int n = dir.indexOf("src");
        if (n != -1) {
            return dir.substring(0, n);
        } else {
            return dir;
        }
    }

    protected static String getExtFileName(String path) {
        if (path == null) {
            return null;
        }
        int lastIndexOfDot = path.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return null;
        }
        return path.substring(lastIndexOfDot + 1);
    }

}
