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
package org.tinygroup.vfs.impl.sftp;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;
import org.tinygroup.vfs.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SftpFileObject implements IFileObject {
    private static final long serialVersionUID = -79355183041937821L;

    private SchemaProvider schemaProvider;
    private FileObject parent;
    private URL url;

    private SftpClient sftpClient;// sftp客户端 直接操作sftp文件系统
    private String absolutePath;// 绝对路径（结尾去除/）
    private String absoluteDir;// 绝对目录（包含文件或最末级文件夹的文件夹绝对路径，去除/）
    private String path; // 相对于初始化资源的相对路径
    private String fileName; // 文件名
    private String extName; // 扩展名
    private LsEntry entry; // 文件或文件夹信息对象

    public SftpFileObject(SchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    public SftpFileObject(SchemaProvider schemaProvider, String resource) {
        try {
            this.schemaProvider = schemaProvider;
            this.url = new URL(null, resource, new URLStreamHandler() {

                protected URLConnection openConnection(URL u)
                        throws IOException {
                    return null;
                }
            });// 给sftp提供一个空的streamhandler 不然会报错
            connectSftpServer();
            initRoot();
        } catch (MalformedURLException e) {
            throw new VFSRuntimeException("不能定位到资源:" + resource, e);
        }
    }


    public SchemaProvider getSchemaProvider() {
        return schemaProvider;
    }


    public boolean isModified() {
        return false;
    }


    public void resetModified() {

    }


    public URL getURL() {
        return url;
    }


    public String getAbsolutePath() {
        return absolutePath;
    }


    public String getPath() {
        return path;
    }


    public String getFileName() {
        return fileName;
    }


    public String getExtName() {
        if (extName == null) {
            int lastIndex = fileName.lastIndexOf('.');
            if (lastIndex != -1) {
                extName = fileName.substring(lastIndex + 1);
            } else {
                extName = "";
            }
        }
        return extName;
    }


    public boolean isFolder() {
        if (isExist()) {
            return entry.getAttrs().isDir();
        }
        throw new VFSRuntimeException("资源不存在，无法判断是否是文件夹！");
    }


    public boolean isInPackage() {
        return false;
    }


    public boolean isExist() {
        return entry != null;
    }


    public long getLastModifiedTime() {
        if (isExist()) {
            return 1000L * entry.getAttrs().getMTime();
        }
        throw new VFSRuntimeException("资源不存在，无法获取最后修改时间！");
    }


    public long getSize() {
        if (isExist()) {
            return entry.getAttrs().getSize();
        }
        throw new VFSRuntimeException("资源不存在，无法获取资源大小！");
    }


    public InputStream getInputStream() {
        if (!isExist() || isFolder()) {
            throw new VFSRuntimeException("该资源不存在或是文件夹");
        }

        InputStream is = null;
        try {
            sftpClient.changeDir(absoluteDir);
            is = sftpClient.getInputStream(fileName);
        } catch (SftpException e) {
            throw new VFSRuntimeException("获取输入流异常", e);
        }

        return is;
    }


    public OutputStream getOutputStream() {
        makePathDirs();
        OutputStream os = null;
        try {
            sftpClient.changeDir(absoluteDir);
            os = sftpClient.getOutputStream(fileName);
        } catch (SftpException e) {
            throw new VFSRuntimeException("获取输入流异常", e);
        }

        return os;
    }


    public FileObject getParent() {
        if (parent == null && !"/".equals(absoluteDir)) { // parent为空或者目录不是根目录是进入获取父层级
            SftpFileObject sfo = new SftpFileObject(schemaProvider);
            sfo.sftpClient = sftpClient;
            sfo.absolutePath = absoluteDir;
            sfo.absoluteDir = getDirPath(sfo.absolutePath);
            sfo.fileName = getFilePath(sfo.absolutePath);
            sfo.entry = sftpClient.getEntry(sfo.absoluteDir, sfo.fileName);
            if (!isExist() || isFolder()) {
                sfo.path = path + "../";
            } else {
                sfo.path = getDirPath(path) + "/";
            }
            setParent(sfo);
        }
        return parent;
    }


    public void setParent(FileObject fileObject) {
        this.parent = fileObject;
    }


    public List<FileObject> getChildren() {
        if (!isExist()) {
            return null;
        }
        if (!isFolder()) {
            return null;
        }
        sftpClient.changeDir(absolutePath);
        LsEntry[] files = sftpClient.ls();
        List<FileObject> sfos = new ArrayList<FileObject>();
        for (LsEntry file : files) {
            SftpFileObject sfo = new SftpFileObject(getSchemaProvider());
            sfo.setParent(this);
            sfo.sftpClient = sftpClient;
            sfo.absoluteDir = absolutePath;
            if ("/".equals(absolutePath)) {
                sfo.absolutePath = absolutePath + file.getFilename();
            } else {
                sfo.absolutePath = absolutePath + "/" + file.getFilename();
            }
            sfo.fileName = file.getFilename();
            sfo.entry = file;
            if (path.endsWith("/")) {
                sfo.path = path + file.getFilename();
            } else {
                sfo.path = path + "/" + file.getFilename();
            }
            sfos.add(sfo);
        }
        return sfos;
    }


    public FileObject getChild(String fileName) {
        List<FileObject> child = getChildren();
        if (child == null || child.size() == 0) {
            return null;
        }
        for (FileObject fo : getChildren()) {
            if (fo.getFileName().equals(fileName)) {
                return fo;
            }
        }
        return null;
    }

    /**
     * 对文件对象及其所有子对象都通过文件对象过滤器进行过滤，如果匹配，则执行文件对外处理器
     *
     * @param fileObjectFilter
     * @param fileObjectProcessor
     * @param parentFirst         true:如果父亲和儿子都命中，则先处理父亲；false:如果父亲和儿子都命中，则先处理儿子
     */

    public void foreach(FileObjectFilter fileObjectFilter,
                        FileObjectProcessor fileObjectProcessor, boolean parentFirst) {
        // 先处理父对象，后处理子对象
        if (parentFirst && fileObjectFilter.accept(this)) {
            fileObjectProcessor.process(this);
        }
        // 如果是目录，递归调用子对象的查询
        if (isFolder()) {
            // 遍历当前文件对象的子文件列表
            for (FileObject subFileObject : getChildren()) {
                subFileObject.foreach(fileObjectFilter, fileObjectProcessor,
                        parentFirst);
            }
        }
        // 先处理子对象，后处理父对象
        if (!parentFirst && fileObjectFilter.accept(this)) {
            fileObjectProcessor.process(this);
        }
    }

    /**
     * 对文件对象及其所有子对象都通过文件对象过滤器进行过滤，如果匹配，则执行文件对外处理器，父文件先处理
     *
     * @param fileObjectFilter
     * @param fileObjectProcessor
     */

    public void foreach(FileObjectFilter fileObjectFilter,
                        FileObjectProcessor fileObjectProcessor) {
        foreach(fileObjectFilter, fileObjectProcessor, true);
    }


    public void clean() {
        sftpClient.logout();
    }


    public FileObject getFileObject(String path) {
        // 此处不用File.separator
        // @luoguo path必须是linux规范
        if (path.equals("/")) {
            return this;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] pathLayers = path.split("/");
        if (isFolder()) {
            List<FileObject> fileObjects = getChildren();
            for (FileObject subFileObject : fileObjects) {
                if (subFileObject.getFileName().equals(pathLayers[0])) {
                    if (pathLayers.length > 1) {
                        return subFileObject.getFileObject(substringAfter(path,
                                "/"));
                    } else {
                        return subFileObject;
                    }
                }
            }

        }

        return null;
    }


    public void upload(InputStream in) {
        if (isExist() && isFolder()) {
            throw new VFSRuntimeException("该资源是文件夹，无法上传内容！");
        }
        makePathDirs();
        sftpClient.changeDir(absoluteDir);
        sftpClient.upload(fileName, in);
    }


    public void upload(String sourceFile) {
        try {
            upload(new FileInputStream(sourceFile));
        } catch (FileNotFoundException e) {
            throw new VFSRuntimeException("获取上传文件流失败", e);
        }
    }


    public void upload(File sourceFile) {
        try {
            upload(new FileInputStream(sourceFile));
        } catch (FileNotFoundException e) {
            throw new VFSRuntimeException("获取上传文件流失败", e);
        }
    }


    public void download(OutputStream out) {
        if (!isExist() || isFolder()) {
            throw new VFSRuntimeException("该资源不存在或是文件夹");
        }
        sftpClient.changeDir(absoluteDir);
        sftpClient.download(fileName, out);
    }


    public void download(String dstFile) {
        download(new File(dstFile));
    }


    public void download(File dstFile) {
        try {
            if (!dstFile.exists()) {
                dstFile.getParentFile().mkdirs();
                dstFile.createNewFile();
            }
            download(new FileOutputStream(dstFile));
        } catch (FileNotFoundException e) {
            throw new VFSRuntimeException("获取下载保存文件流失败", e);
        } catch (IOException e) {
            throw new VFSRuntimeException("创建文件流失败", e);
        }
    }

    public String getAbsoluteDir() {
        return absoluteDir;
    }

    /*-------private method-----------*/
    private void connectSftpServer() {
        URL url = getURL();
        sftpClient = new SftpClient(url);
        sftpClient.login();
    }

    private void initRoot() {
        try {
            String p = getURL().getPath();
            // 如果且以"/"结尾，去掉"/"
            if (p.endsWith("/")) {
                p = p.substring(0, p.lastIndexOf('/'));
            }
            // 资源在服务器中所属目录
            String dir = getDirPath(p);

            absolutePath = recode(p);
            absoluteDir = recode(dir);

            fileName = getFilePath(p);
            fileName = recode(fileName);

            sftpClient.changeDir(absoluteDir);
            entry = sftpClient.getEntry(absoluteDir, fileName);
            path = "./";
            if (entry != null) { // 文件存在
                if (!isFolder()) {
                    path = path + fileName;
                }
            }
        } catch (Exception e) {
            throw new VFSRuntimeException(e);
        }
    }

    private String recode(String str) {
        String recode = str;
        try {
            recode = new String(str.getBytes("UTF-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            // 转码失败,忽略之
        }
        return recode;
    }

    private String getDirPath(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.lastIndexOf('/'));
        }
        // 资源在服务器中所属目录
        String dir = path.substring(0, path.lastIndexOf('/'));
        // 如果所属目录为根目录
        if (dir.length() == 0) {
            dir = "/";
        }
        return dir;
    }

    private String getFilePath(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.lastIndexOf('/'));
        }
        String file = path.substring(path.lastIndexOf('/'));
        file = file.substring(file.lastIndexOf('/') + 1);
        return file;
    }

    private String substringAfter(String str, String separator) {
        if (str == null || str.length() == 0) {
            return str;
        }

        int pos = str.indexOf(separator);

        if (pos >= 0) {
            return str.substring(pos + separator.length());
        }
        return "";
    }

    private void makePathDirs() {
        Stack<SftpFileObject> stack = new Stack<SftpFileObject>();
        SftpFileObject fo = this;
        while (fo != null && !fo.isExist()) {
            fo = (SftpFileObject) fo.getParent();
            stack.push(fo);
        }
        while (!stack.isEmpty()) {
            fo = stack.pop();
            sftpClient.changeDir(fo.getAbsoluteDir());
            sftpClient.makeDir(fo.getFileName());
        }
    }

    public void delete() {
        throw new RuntimeException("本FileObject实现不支持delete操作!");
    }
}
