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
package org.tinygroup.templatespringext.impl;

import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.loader.FileObjectResourceLoader;
import org.tinygroup.template.loader.FileResourceManager;
import org.tinygroup.templatespringext.AbstractFileScanner;
import org.tinygroup.templatespringext.processor.TinyJarFileProcessor;
import org.tinygroup.templatespringext.processor.TinyLocalPathProcessor;
import org.tinygroup.templatespringext.processor.TinyMacroProcessor;
import org.tinygroup.vfs.VFS;

/**
 * Created by wangll13383 on 2015/9/11.
 */
public class FileScannerImpl extends AbstractFileScanner {

    private TinyJarFileProcessor jarFileProcessor;

    private TinyMacroProcessor macroFileProcessor;

    private TinyLocalPathProcessor localPathProcessor;

    private TemplateEngine engine;

    private FileResourceManager fileResourceManager = new FileResourceManager();

    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    public TinyJarFileProcessor getJarFileProcessor() {
        return jarFileProcessor;
    }

    public void setJarFileProcessor(TinyJarFileProcessor jarFileProcessor) {
        this.jarFileProcessor = jarFileProcessor;
    }

    public void init() {
        jarFileProcessor = jarFileProcessor == null ? new TinyJarFileProcessor() : jarFileProcessor;
        jarFileProcessor.setFileResourceManager(fileResourceManager);
        macroFileProcessor = new TinyMacroProcessor();
        macroFileProcessor.setFileResourceManager(fileResourceManager);
        localPathProcessor = new TinyLocalPathProcessor();
        localPathProcessor.setFileResourceManager(fileResourceManager);

        FileObjectResourceLoader fileObjectResourceLoader = new FileObjectResourceLoader("page", "layout", "component");
        fileObjectResourceLoader.setFileResourceManager(fileResourceManager);
        engine.addResourceLoader(fileObjectResourceLoader);

        this.getFileProcessors().add(jarFileProcessor);
        this.getFileProcessors().add(macroFileProcessor);
        this.getFileProcessors().add(localPathProcessor);

        jarFileProcessor.setEngine(engine);
        macroFileProcessor.setEngine(engine);
        localPathProcessor.setEngine(engine);

    }

    public void scanFile() {
        resolverFolder(VFS.resolveFile("./"));
    }

    public void fileProcess() {
        localPathProcessor.process();
        classPathProcess();
        jarFileProcessor.process();
        macroFileProcessor.process();
    }

    public void classPathProcess() {
        for (String classPath : getClassPathList()) {
            fileResourceManager.addResources(engine, classPath, ".page", ".layout", ".component");
        }
    }
}
