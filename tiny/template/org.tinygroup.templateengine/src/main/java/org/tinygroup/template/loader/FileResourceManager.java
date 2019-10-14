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
package org.tinygroup.template.loader;

import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.template.Macro;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectFilter;
import org.tinygroup.vfs.FileObjectProcessor;
import org.tinygroup.vfs.VFS;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模板文件资源管理器(页面文件、布局文件和宏文件)
 *
 * @author yancheng11334
 */
public class FileResourceManager {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(FileResourceManager.class);

    /**
     * 文件资源,第一级key选相对路径，第二级key选绝对路径
     */
    private Map<String, Map<String, FileObject>> resources = new ConcurrentHashMap<String, Map<String, FileObject>>();

    /**
     * 文件扫描器
     */
    private FileResolver fileResolver;

    private List<FileObject> scanFileList = new ArrayList<FileObject>();

    private Comparator<FileObject> comparator = new FileObjectComparator();

    /**
     * 文本资源是否按最近修改时间排序
     */
    private boolean textOrderByTime = false;

    public boolean isTextOrderByTime() {
        return textOrderByTime;
    }

    public void setTextOrderByTime(boolean textOrderByTime) {
        this.textOrderByTime = textOrderByTime;
    }

    public FileResolver getFileResolver() {
        return fileResolver;
    }

    public void setFileResolver(FileResolver fileResolver) {
        this.fileResolver = fileResolver;
    }

    public void addScanFile(FileObject file) {
        if (!scanFileList.contains(file)) {
            scanFileList.add(file);
        }
    }

    /**
     * 批量增加某个VFS路径下的模板引擎相关资源
     *
     * @param engine
     * @param root
     * @param templateExtName
     * @param layoutExtName
     * @param componentExtName
     */
    public void addResources(final TemplateEngine engine,
                             final FileObject root, final String templateExtName,
                             final String layoutExtName, final String componentExtName) {
        addResources(engine, root, templateExtName, layoutExtName,
                componentExtName, true);
    }

    /**
     * 批量增加某个VFS路径下的模板引擎相关资源
     *
     * @param engine
     * @param root
     * @param templateExtName
     * @param layoutExtName
     * @param componentExtName
     * @param logTag
     */
    public void addResources(final TemplateEngine engine,
                             final FileObject root, final String templateExtName,
                             final String layoutExtName, final String componentExtName, final boolean logTag) {
        addScanFile(root);
        root.foreach(new FileObjectFilter() {
            public boolean accept(FileObject fileObject) {
                String name = fileObject.getFileName();
                return (templateExtName != null && name
                        .endsWith(templateExtName))
                        || (layoutExtName != null && name
                        .endsWith(layoutExtName))
                        || (componentExtName != null && name
                        .endsWith(componentExtName));
            }
        }, new FileObjectProcessor() {
            public void process(FileObject fileObject) {
                if (logTag) {
                    LOGGER.logMessage(LogLevel.INFO, "模板资源文件[{0}]开始加载",
                            fileObject.getAbsolutePath());
                }

                try {
                    addResource(fileObject.getPath(), fileObject);
                    Template template = engine.findTemplateCache(fileObject.getPath(), fileObject.getAbsolutePath());
                    if (template != null) {
                        if (componentExtName != null
                                && fileObject.getFileName().endsWith(
                                componentExtName)) {
                            for (Macro macro : template.getMacroMap().values()) {
                                engine.removeMacroCache(macro.getName(), macro.getAbsolutePath());
                            }
                        }
                        engine.removeTemplateCache(fileObject.getPath(), fileObject.getAbsolutePath());
                    }
                    template = TemplateLoadUtil.loadComponent(
                            (TemplateEngineDefault) engine, fileObject);
                    template.setTemplateEngine(engine);
                    engine.addTemplateCache(template.getPath(), template);
                    if (componentExtName != null
                            && fileObject.getFileName().endsWith(
                            componentExtName)) {
                        engine.registerMacroLibrary(template);
                    }

                } catch (Exception e) {
                    if (logTag) {
                        LOGGER.errorMessage("加载模板资源文件[{0}]出错,注册主键[{1}]", e,
                                fileObject.getAbsolutePath(), fileObject.getPath());
                    }

                }
                if (logTag) {
                    LOGGER.logMessage(LogLevel.INFO, "模板资源文件[{0}]加载完毕,注册主键[{1}]",
                            fileObject.getAbsolutePath(), fileObject.getPath());
                }


            }
        });
    }

    /**
     * 批量增加某个VFS路径下的模板引擎相关资源
     *
     * @param engine
     * @param resource
     * @param templateExtName
     * @param layoutExtName
     * @param componentExtName
     */
    public void addResources(final TemplateEngine engine,
                             final String resource, final String templateExtName,
                             final String layoutExtName, final String componentExtName) {
        FileObject root = VFS.resolveFile(resource);
        addResources(engine, root, templateExtName, layoutExtName,
                componentExtName);
    }

    /**
     * 添加文件资源
     * @param path
     * @param file
     * @return
     */
    public boolean addResource(String path, FileObject file) {

        //处理文件资源
        Map<String, FileObject> fileMap = resources.get(path);
        if (fileMap == null) {
            fileMap = new ConcurrentHashMap<String, FileObject>();
            resources.put(path, fileMap);
        }
        fileMap.put(file.getAbsolutePath(), file);

        return true;
    }

    public void removeScanFile(FileObject file) {
        scanFileList.remove(file);
    }

    /**
     * 批量删除某个VFS路径下的模板引擎相关资源
     *
     * @param engine
     * @param root
     * @param templateExtName
     * @param layoutExtName
     * @param componentExtName
     */
    public void removeResources(final TemplateEngine engine,
                                final FileObject root, final String templateExtName,
                                final String layoutExtName, final String componentExtName) {
        removeScanFile(root);
        root.foreach(new FileObjectFilter() {
            public boolean accept(FileObject fileObject) {
                String name = fileObject.getFileName();
                return (templateExtName != null && name
                        .endsWith(templateExtName))
                        || (layoutExtName != null && name
                        .endsWith(layoutExtName))
                        || (componentExtName != null && name
                        .endsWith(componentExtName));
            }
        }, new FileObjectProcessor() {
            public void process(FileObject fileObject) {
                LOGGER.logMessage(LogLevel.INFO, "模板资源文件[{0}]开始移除",
                        fileObject.getAbsolutePath());
                try {
                    removeResource(fileObject);
                    Template template = engine.findTemplateCache(fileObject.getPath(), fileObject.getAbsolutePath());
                    if (template != null) {
                        engine.removeTemplateCache(template.getPath(), template.getAbsolutePath());
                        if (componentExtName != null
                                && fileObject.getFileName().endsWith(
                                componentExtName)) {
                            for (Macro macro : template.getMacroMap().values()) {
                                engine.removeMacroCache(macro.getName(), macro.getAbsolutePath());
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.errorMessage("移除模板资源文件[{0}]出错,卸载主键[{1}]", e,
                            fileObject.getAbsolutePath(), fileObject.getPath());
                }
                LOGGER.logMessage(LogLevel.INFO, "模板资源文件[{0}]移除完毕,卸载主键[{1}]",
                        fileObject.getAbsolutePath(), fileObject.getPath());
            }
        });
    }

    /**
     * 批量删除某个VFS路径下的模板引擎相关资源
     *
     * @param engine
     * @param resource
     * @param templateExtName
     * @param layoutExtName
     * @param componentExtName
     */
    public void removeResources(final TemplateEngine engine,
                                final String resource, final String templateExtName,
                                final String layoutExtName, final String componentExtName) {
        FileObject root = VFS.resolveFile(resource);
        removeResources(engine, root, templateExtName, layoutExtName,
                componentExtName);
    }

    /**
     * 删除文件资源
     * @param file
     */
    public void removeResource(FileObject file) {
        // 处理文件资源
        String path = file.getPath();

        Map<String, FileObject> fileMap = resources.get(path);
        if (fileMap != null) {
            fileMap.remove(file.getAbsolutePath());
            if (fileMap.isEmpty()) {
                resources.remove(path);
            }
        }

    }

    /**
     * 查询已经注册文件资源
     *
     * @param path
     * @param tag  是否精确匹配
     * @return
     */
    public FileObject getFileObject(String path, boolean tag) {
        //地址的过滤转换
        path = filterPath(path);

        // 先直接地址匹配
        if (resources.containsKey(path)) {
            return findFileObject(path);
        }

        return null;
    }


    private FileObject findFileObject(String path) {
        Map<String, FileObject> fileMap = resources.get(path);
        if (fileMap != null && !fileMap.isEmpty()) {
            return Collections.min(fileMap.values(), comparator);
        }
        return null;
    }

    /**
     * 查询已经注册文件资源
     *
     * @param path
     * @return
     */
    public FileObject getFileObject(String path) {
        return getFileObject(path, false);
    }

    /**
     * 查询注册模板资源以外的其他文件
     *
     * @param path
     * @return
     */
    public FileObject getOtherFileObject(String path) {

        if (textOrderByTime) {
            //遍历全部文件目录,如果有多个相同路径的文本资源按最新修改时间排序,返回最新的
            Set<FileObject> sets = new HashSet<FileObject>();
            if (fileResolver != null) {
                for (String rootPath : fileResolver.getScanningPaths()) {
                    FileObject root = VFS.resolveFile(rootPath);
                    FileObject file = root.getFileObject(path);
                    if (file != null) {
                        sets.add(file);
                    }
                }
            }
            for (FileObject root : scanFileList) {
                FileObject file = root.getFileObject(path);
                if (file != null) {
                    sets.add(file);
                }
            }
            return Collections.min(sets, comparator);
        } else {
            // 逐个遍历文件目录，发现符合条件的文本资源即返回
            // 通过文件扫描器查询
            if (fileResolver != null) {
                for (String rootPath : fileResolver.getScanningPaths()) {
                    FileObject root = VFS.resolveFile(rootPath);
                    FileObject file = root.getFileObject(path);
                    if (file != null) {
                        return file;
                    }
                }
            }
            // 非文件扫描器方式查询
            for (FileObject root : scanFileList) {
                FileObject file = root.getFileObject(path);
                if (file != null) {
                    return file;
                }
            }
            return null;
        }

    }

    /**
     * 合并路径
     *
     * @param dirs
     * @param n
     * @return
     */
    private String mergePath(String[] dirs, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = n; i < dirs.length; i++) {
            sb.append("/").append(dirs[i]);
        }
        return sb.toString();
    }

    private String filterPath(String path) {
        if (!path.startsWith("/")) {
            //如果不是绝对路径
//    	   try {
//			  URL newUrl = new URL(path);
//			  return newUrl.getPath();
//		   } catch (MalformedURLException e) {
//			  //如果转换异常直接返回原路径
//			  return path;
//		   }
            //URL获取新的绝对路径需要path和参照文件，本接口无法提供参照文件，所以采用简易方式
            path = "/" + path;
        }
        return path;
    }

    /**
     * 拆分路径
     *
     * @param path
     * @return
     */
    private String[] splitPath(String path) {
        path = path.startsWith("/") ? path.substring(1) : path;
        return path.split("/");
    }

    /**
     * 文件是否被修改
     *
     * @param path
     * @return
     */
    public boolean isModified(String path) {
        return isModified(path, false);
    }

    /**
     * 文件是否被修改
     *
     * @param path
     * @return
     */
    public boolean isModified(String path, boolean tag) {
        FileObject fileObject = getFileObject(path, tag);
        if (fileObject == null) {
            return true;
        }
        return fileObject.isModified();
    }


    /**
     * 重置文件修改标识
     *
     * @param path
     */
    public void resetModified(String path) {
        resetModified(path, false);
    }

    /**
     * 重置文件修改标识
     *
     * @param path
     */
    public void resetModified(String path, boolean tag) {
        FileObject fileObject = getFileObject(path, tag);
        if (fileObject != null) {
            fileObject.resetModified();
        }
    }

    /**
     * 文件比较器
     * @author yancheng11334
     *
     */
    static class FileObjectComparator implements Comparator<FileObject> {
        //按最近修改时间排序
        public int compare(FileObject o1, FileObject o2) {
            if (o1.getLastModifiedTime() > o2.getLastModifiedTime()) {
                return -1;
            } else if (o1.getLastModifiedTime() < o2.getLastModifiedTime()) {
                return 1;
            }
            return 0;
        }

    }

//    public static void main(String[] args){
//    	FileObject f1 = VFS.resolveFile("d:/yc.txt");
//    	FileObject f2 = VFS.resolveFile("d:/apiclient_cert.p12");
//    	FileObject f3 = VFS.resolveFile("d:/new.txt");
//    	List<FileObject> list = new ArrayList<FileObject>();
//    	
//    	
//    	list.add(f3);
//    	list.add(f1);
//    	list.add(f2);
//    	System.out.println("f1="+f1.getLastModifiedTime());
//    	System.out.println("f2="+f2.getLastModifiedTime());
//    	System.out.println("f3="+f3.getLastModifiedTime());
//    	FileObjectComparator c = new FileObjectComparator();
//    	//Collections.sort(list, c);
//    	//System.out.println(list.get(2).getAbsolutePath());
//    	FileObject result = Collections.min(list,c);
//    	System.out.println(result.getAbsolutePath());
//    }
}
