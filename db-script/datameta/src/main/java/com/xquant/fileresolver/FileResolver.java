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
package com.xquant.fileresolver;

import com.xquant.file.FileObject;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 文件查找器
 *
 * @author luoguo
 */
public interface FileResolver   {

    String BEAN_NAME = "fileResolver";

    /**
     * 返回所有的文件处理器
     *
     * @return 文件处理器列表
     */
    List<FileProcessor> getFileProcessorList();

    /**
     * 返回当前FileResolver要扫描的根路径列表
     *
     * @return
     */
    List<String> getScanningPaths();

    /**
     * 手工添加扫描的匹配列表，如果有包含列表，则按包含列表
     *
     * @param pattern 扫描路径的正则
     */
    void addIncludePathPattern(String pattern);

    /**
     * 返回所有的扫描路径正则
     *
     * @return
     */
    Map<String, Pattern> getIncludePathPatternMap();

    /**
     * 添加扫描的路径
     *
     * @param fileObject 文件对象
     */
    void addResolveFileObject(FileObject fileObject);

    /**
     * 添加扫描路径
     *
     * @param path 扫描路径
     */
    void addResolvePath(String path);

    /**
     * 添加扫描路径
     *
     * @param paths 扫描路径列表
     */
    void addResolvePath(List<String> paths);

    /**
     * 移除扫描路径
     *
     * @param path 扫描路径
     */
    void removeResolvePath(String path);

    /**
     * 增加文件处理器
     *
     * @param fileProcessor 文件处理器
     */
    void addFileProcessor(FileProcessor fileProcessor);

    /**
     * 返回类加载器
     *
     * @return
     */
    ClassLoader getClassLoader();

    /**
     * 设置类加载器
     *
     * @param classLoader 类加载器
     */
    void setClassLoader(ClassLoader classLoader);

    /**
     * 文件处理方法
     */
    void resolve();

    /**
     * 文件处理刷新方法
     */
    void refresh();

    /**
     * 获取文件处理的线程数目
     *
     * @return
     */
    int getFileProcessorThreadNumber();

    /**
     * 设置文件处理的线程数目
     *
     * @param threadNum
     */
    void setFileProcessorThreadNumber(int threadNum);

    @Deprecated
    void addChangeLisenter(ChangeListener listener);

    /**
     * 添加文件变化监听器
     *
     * @param listener
     */
    void addChangeListener(ChangeListener listener);

    /**
     * 返回所有的文件监听器列表
     *
     * @return
     */
    List<ChangeListener> getChangeListeners();

    /**
     * 文件变化触发的方法,会触发所有 ChangeListener
     */
    void change();
}
