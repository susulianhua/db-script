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
package com.xquant.fileresolver.impl;

import com.xquant.common.OrderUtil;
import com.xquant.fileresolver.ChangeListener;
import com.xquant.fileresolver.FileProcessor;
import com.xquant.fileresolver.FileResolver;
import com.xquant.fileresolver.util.FileResolverUtil;
import com.xquant.vfs.FileObject;
import com.xquant.vfs.VFS;
import com.xquant.xmlparser.node.XmlNode;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.lf5.viewer.configure.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 功能说明: 文件搜索器默认实现
 * <p/>
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2013-2-21 <br>
 * <br>
 */
public class FileResolverImpl implements FileResolver {
    private static final String FILE_RESOLVER_CONFIG = "/application/file-resolver-configuration";
    private static final int DEFAULT_THREAD_NUM = Runtime.getRuntime()
            .availableProcessors();
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FileResolverImpl.class);
    private static final String[] WEB_ENVIRONMENT_CLASSES = {
            "javax.servlet.Servlet",
            "org.springframework.web.context.ConfigurableWebApplicationContext"};
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    // 是否对classPath进行处理，默认为处理
    private int fileProcessorThreadNum = DEFAULT_THREAD_NUM;
    // 文件处理器列表，由文件查找器统一管理
    private List<FileProcessor> fileProcessorList = new ArrayList<FileProcessor>();
    // 文件信息 key:path,value:modify-time
    private Map<String, Long> fileDateMap = new HashMap<String, Long>();
    // 文件信息
    private Map<String, FileObject> fileObjectCaches = new HashMap<String, FileObject>();
    private Map<String, Pattern> includePathPatternMap = new HashMap<String, Pattern>();
    private List<String> allScanningPath = new ArrayList<String>();
    private XmlNode componentConfig;
    private XmlNode applicationConfig;
    private ClassLoader classLoader;

    public FileResolverImpl() {
        String classPath = "[\\/]classes\\b";
        includePathPatternMap.put(classPath, Pattern.compile(classPath));
        classPath = "[\\/]test-classes\\b";
        includePathPatternMap.put(classPath, Pattern.compile(classPath));
    }

    public FileResolverImpl(boolean b) {

    }

    public List<ChangeListener> getChangeListeners() {
        return changeListeners;
    }

    public void setChangeListeners(List<ChangeListener> changeListeners) {
        this.changeListeners = changeListeners;
    }

    public List<String> getScanningPaths() {
        return allScanningPath;
    }

    public List<FileProcessor> getFileProcessorList() {
        return fileProcessorList;
    }

    public void setFileProcessorList(List<FileProcessor> fileProcessorList) {
        this.fileProcessorList = fileProcessorList;
    }

    public void addFileProcessor(FileProcessor fileProcessor) {
        fileProcessorList.add(fileProcessor);
        fileProcessor.setFileResolver(this);
    }

    public ClassLoader getClassLoader() {
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void resolve() {
        if (!fileProcessorList.isEmpty()) {
            for (FileProcessor fileProcessor : fileProcessorList) {
                fileProcessor.setFileResolver(this);
            }
            OrderUtil.order(fileProcessorList);
            cleanProcessor();
            // 移动日志信息，文件搜索器中存在处理器时，才会进行全路径扫描，并打印日志信息
            LOGGER.info( "正在进行全路径扫描....");
            resolverScanPath();
            for (FileProcessor fileProcessor : fileProcessorList) {
                fileProcessor.process();
            }
            LOGGER.info( "全路径扫描完成。");
        }
    }

    private void refreshScanPath() {
        List<FileObject> classPaths = new ArrayList<FileObject>();
        for (String filePath : allScanningPath) {
            FileObject fileObject = VFS.resolveFile(filePath);
            Long lastModifiedTime = fileDateMap.get(fileObject
                    .getAbsolutePath());
            long modifiedTime = fileObject.getLastModifiedTime();
            if (lastModifiedTime.longValue() != modifiedTime
                    || !fileObject.isInPackage()) {
                fileDateMap.put(fileObject.getAbsolutePath(), modifiedTime);
                if (fileObject.isExist()) {
                    classPaths.add(fileObject);
                }
            }
        }
        resolveClassPaths(classPaths);
        change();
    }

    private boolean deduceWebEnvironment() {
        for (String className : WEB_ENVIRONMENT_CLASSES) {
            if (!ClassUtils.isPresent(className, null)) {
                return false;
            }
        }
        return true;
    }

    private void resolverScanPath() {
        List<FileObject> classPaths = new ArrayList<FileObject>();
        for (String filePath : allScanningPath) {
            FileObject fileObject = VFS.resolveFile(filePath);
            classPaths.add(fileObject);
            long modifiedTime = fileObject.getLastModifiedTime();
            fileDateMap.put(fileObject.getAbsolutePath(), modifiedTime);
        }
        resolveClassPaths(classPaths);
    }

    /**
     * 文件搜索器重新搜索时，需要清空上次各个文件处理器中处理的文件列表,并清除记录的扫描路径。
     */
    private void cleanProcessor() {
        for (FileProcessor fileProcessor : fileProcessorList) {
            fileProcessor.clean();
        }
        // allScanningPath.clear();
    }

    /**
     * 文件搜索器根据路径列表进行搜索
     *
     * @param classPaths
     */
    private void resolveClassPaths(List<FileObject> classPaths) {
        List<FileObject> scanPathsList = new ArrayList<FileObject>();
        scanPathsList.addAll(classPaths);
        for(FileObject fileObject:classPaths)     {
            LOGGER.info( "正在扫描路径" +
                    fileObject.getAbsolutePath());
            resolveFileObject(fileObject);
            LOGGER.info( "路径" + fileObject.getAbsolutePath() +"扫描完成。");
        }
    }

    private void resolveFileObject(FileObject fileObject) {

        processFile(fileObject);
        if (fileObject.isFolder() && fileObject.getChildren() != null) {
            for (FileObject f : fileObject.getChildren()) {
                if (!allScanningPath.contains(f.getAbsolutePath())) {
                    if (FileResolverUtil.isInclude(f, this)) {
                        resolveFileObject(f);
                    }
                } else {
                    LOGGER.info(
                            "文件:[{}]在扫描根路径列表中存在，将作为根路径进行扫描",
                            f.getAbsolutePath());
                }
            }
            return;
        }
    }

    /**
     * 移除已删除文件
     */
    private void resolveDeletedFile() {
        // 处理删除的文件
        Map<String, FileObject> tempMap = new HashMap<String, FileObject>();
        for (String path : fileObjectCaches.keySet()) {
            FileObject fileObject = fileObjectCaches.get(path);
            if (!fileObject.isExist()) {
                // 文件已经被删除
                for (FileProcessor fileProcessor : fileProcessorList) {
                    if (fileProcessor.isMatch(fileObject)) {// 匹配后才能删除
                        fileProcessor.delete(fileObject);
                    }
                }
            } else {
                tempMap.put(path, fileObject);
            }
        }
        fileObjectCaches = tempMap;
    }

    /**
     * 文件存在并且不在忽略处理列表中，再交给各个文件处理器进行处理。
     *
     * @param fileObject
     */
    private synchronized void processFile(FileObject fileObject) {
        if (fileObject.isExist()) {
            String absolutePath = fileObject.getAbsolutePath();
            Long lastModifiedTime = fileDateMap.get(absolutePath);
            long modifiedTime = fileObject.getLastModifiedTime();
            for (FileProcessor fileProcessor : fileProcessorList) {
                if (fileProcessor.isMatch(fileObject)) {
                    if (lastModifiedTime == null) {// 说明是第一次发现
                        addFile(fileObject, fileProcessor);
                    } else if (lastModifiedTime.longValue() != modifiedTime) {
                        changeFile(absolutePath, fileObject, fileProcessor);
                    } else {
                        noChangeFile(fileObject, fileProcessor);
                    }
                }
            }
            fileDateMap.put(absolutePath, modifiedTime);
            fileObjectCaches.put(absolutePath, fileObject);
        }
    }

    private void noChangeFile(FileObject fileObject, FileProcessor fileProcessor) {
        fileProcessor.noChange(fileObject);

    }

    private void changeFile(String path, FileObject fileObject,
                            FileProcessor fileProcessor) {
        FileObject targetFileObject = fileObjectCaches.get(path);
        if (targetFileObject != null) {
            fileProcessor.delete(targetFileObject);
        } else {
            fileProcessor.delete(fileObject);
        }
        fileObjectCaches.remove(path);
        fileProcessor.modify(fileObject);// 先删除之前的，再增加新的
    }

    private void addFile(FileObject fileObject, FileProcessor fileProcessor) {
        fileProcessor.add(fileObject);
    }

    public void addIncludePathPattern(String pattern) {
        includePathPatternMap.put(pattern, Pattern.compile(pattern));
    }

    public void addResolveFileObject(FileObject fileObject) {
        String path = fileObject.getAbsolutePath();
        if (!allScanningPath.contains(path)) {
            allScanningPath.add(path);
        }
    }

    public void addResolvePath(String path) {
        addResolveFileObject(VFS.resolveFile(path));
    }

    public void addResolvePath(List<String> paths) {
        for (String path : paths) {
            addResolveFileObject(VFS.resolveFile(path));
        }
    }

    public int getFileProcessorThreadNumber() {
        return fileProcessorThreadNum;
    }

    public void setFileProcessorThreadNumber(int threadNum) {
        this.fileProcessorThreadNum = threadNum;

    }

    /**
     * 刷新工作
     */
    public void refresh() {
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        if (fileProcessorList.isEmpty()) {
            return;
        } else {
            LOGGER.info( "正在进行全路径刷新....");

            // for (FileProcessor fileProcessor : fileProcessorList) {
            // fileProcessor.clean();// 清空文件列表
            // }
            // 20150528注释以上代码，改为调用cleanProcessor()
            cleanProcessor();
            resolveDeletedFile();// 删除不存在的文件
            refreshScanPath();// 重新扫描
            for (FileProcessor fileProcessor : fileProcessorList) {
                if (fileProcessor.supportRefresh()) {
                    fileProcessor.process();
                }
            }
            LOGGER.info( "全路径刷新结束....");
        }

    }

    public String getApplicationNodePath() {
        return FILE_RESOLVER_CONFIG;
    }

    public String getComponentConfigPath() {
        return "/fileresolver.config.xml";
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.applicationConfig = applicationConfig;
        this.componentConfig = componentConfig;
        initConfig();
    }

    private void initConfig() {


    }

    public XmlNode getComponentConfig() {
        return componentConfig;
    }

    public XmlNode getApplicationConfig() {
        return applicationConfig;
    }

    private int getThreadNumber(String threadNumber) {
        int threadNum = fileProcessorThreadNum;
        if (threadNumber != null) {
            try {
                threadNum = Integer.parseInt(threadNumber);
                if (threadNum <= 0) {
                    threadNum = 1;
                }
            } catch (Exception e) {
                // 传入非int值
                threadNum = 1;
            }

        }
        return threadNum;

    }

    public Map<String, Pattern> getIncludePathPatternMap() {
        return includePathPatternMap;
    }

    @Deprecated
    public void addChangeLisenter(ChangeListener listener) {
        addChangeListener(listener);
    }

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void change() {
        for (ChangeListener changeLisenter : changeListeners) {
            changeLisenter.change(this);
        }
    }

    private void removeFile(FileObject fileObject) {
        fileObjectCaches.remove(fileObject.getPath());
        for (FileProcessor fileProcessor : fileProcessorList) {
            if (fileProcessor.isMatch(fileObject)) {// 匹配后才能删除
                fileProcessor.delete(fileObject);
            }
        }
        if (fileObject.getChildren() != null) {
            for (FileObject child : fileObject.getChildren()) {
                removeFile(child);
            }
        }
    }

    public void removeResolvePath(String path) {
        FileObject fileObject = VFS.resolveFile(path);
        String filePath = fileObject.getAbsolutePath();
        allScanningPath.remove(filePath);
        for (FileProcessor fileProcessor : fileProcessorList) {
            fileProcessor.clean();// 清空文件列表
        }
        removeFile(fileObject);
        for (FileProcessor fileProcessor : fileProcessorList) {
            if (fileProcessor.supportRefresh()) {
                fileProcessor.process();
            }
        }

        // Map<String, FileObject> tempMap = new HashMap<String, FileObject>();
        // for (String path : fileObjectCaches.keySet()) {
        // FileObject fileObject = fileObjectCaches.get(path);
        // if (!fileObject.isExist()) {
        // // 文件已经被删除
        // for (FileProcessor fileProcessor : fileProcessorList) {
        // if (fileProcessor.isMatch(fileObject)) {// 匹配后才能删除
        // fileProcessor.delete(fileObject);
        // }
        // }
        // } else {
        // tempMap.put(path, fileObject);
        // }
        // }
        // fileObjectCaches = tempMap;
    }

}
