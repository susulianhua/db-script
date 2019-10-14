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

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tinygroup.vfs.VFSRuntimeException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class SftpClient {

    /**
     * 日志记录器
     */
    private Log logger = LogFactory.getLog(SftpClient.class);
    /**
     * 资源路径
     */
    private URL url;
    /**
     * Session
     */
    private Session session = null;
    /**
     * Channel
     */
    private ChannelSftp channel = null;
    /**
     * SFTP服务器IP地址
     */
    private String host;
    /**
     * SFTP服务器端口
     */
    private int port;
    /**
     * 连接超时时间，单位毫秒
     */
    private int timeout = 30000;
    /**
     * 私钥
     */
    private String privKey = "";

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * SFTP 安全文件传送协议
     *
     * @param host     SFTP服务器IP地址
     * @param port     SFTP服务器端口
     * @param timeout  连接超时时间，单位毫秒
     * @param username 用户名
     * @param password 密码
     */
    public SftpClient(URL url) {
        this.url = url;
        this.host = url.getHost();
        this.port = url.getPort();
        if (url.getUserInfo() != null) {
            String userInfo[] = url.getUserInfo().split(":");
            if (userInfo.length >= 1) {
                this.username = userInfo[0];
            }
            if (userInfo.length >= 2) {
                this.password = userInfo[1];
            }
        }
    }

    /**
     * 登陆SFTP服务器
     */
    public void login() {

        try {
            JSch jsch = new JSch();
            if (StringUtils.isNotBlank(privKey)) {
                jsch.addIdentity(privKey);
            }

            if (port > 0) {
                session = jsch.getSession(username, host, port); // 连接服务器，采用指定端口
            } else {
                session = jsch.getSession(username, host); // 连接服务器，采用默认端口
            }

            if (password != null) {
                session.setPassword(password);
            }

            // 如果服务器连接不上，则抛出异常
            if (session == null) {
                throw new VFSRuntimeException("服务器连接失败：" + url.toString());
            }

            Properties config = new Properties();
            // 设置不用检查hostKey
            // 如果设置成“yes”，ssh就不会自动把计算机的密匙加入“$HOME/.ssh/known_hosts”文件，
            // 并且一旦计算机的密匙发生了变化，就拒绝连接。
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(timeout);
            session.connect();
            logger.debug("sftp session connected");

            logger.debug("opening channel");
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            logger.debug("connected successfully");
        } catch (JSchException e) {
            logger.error("sftp login failed", e);
            throw new VFSRuntimeException("建立SFTP通道失败：" + url.toString());
        }
    }

    /**
     * 获取当前工作目录下子文件的InputStream
     *
     * @param fileName 当前工作目录下子文件名
     * @return InputStream
     * @throws SftpException
     */
    public InputStream getInputStream(String fileName) throws SftpException {
        return channel.get(fileName);
    }

    /**
     * 获取当前工作目录下子文件的OutputStream
     *
     * @param fileName 当前工作目录下子文件名
     * @return InputStream
     * @throws SftpException
     */
    public OutputStream getOutputStream(String fileName) throws SftpException {
        return channel.put(fileName, ChannelSftp.OVERWRITE);
    }

    /**
     * 切换工作目录
     * <p>
     * 使用示例，SFTP服务器上的目录结构如下：/testA/testA_B/
     * <table border="1">
     * <tr>
     * <td>当前目录</td>
     * <td>方法</td>
     * <td>参数(绝对路径/相对路径)</td>
     * <td>切换后的目录</td>
     * </tr>
     * <tr>
     * <td>/</td>
     * <td>changeDir("testA")</td>
     * <td>相对路径</td>
     * <td>/testA/</td>
     * </tr>
     * <tr>
     * <td>/</td>
     * <td>changeDir("testA/testA_B")</td>
     * <td>相对路径</td>
     * <td>/testA/testA_B/</td>
     * </tr>
     * <tr>
     * <td>/</td>
     * <td>changeDir("/testA")</td>
     * <td>绝对路径</td>
     * <td>/testA/</td>
     * </tr>
     * <tr>
     * <td>/testA/testA_B/</td>
     * <td>changeDir("/testA")</td>
     * <td>绝对路径</td>
     * <td>/testA/</td>
     * </tr>
     * </table>
     * </p>
     *
     * @param pathName 路径
     * @return boolean
     */
    public boolean changeDir(String pathName) {
        if (pathName == null || pathName.trim().equals("")) {
            logger.debug("invalid pathName");
            return false;
        }

        try {
            channel.cd(pathName.replaceAll("\\\\", "/"));
            logger.debug("directory successfully changed,current dir="
                    + channel.pwd());
            return true;
        } catch (SftpException e) {
            logger.error("failed to change directory", e);
            return false;
        }
    }

    /**
     * 切换到上一级目录
     * <p>
     * 使用示例，SFTP服务器上的目录结构如下：/testA/testA_B/
     * <table border="1">
     * <tr>
     * <td>当前目录</td>
     * <td>方法</td>
     * <td>切换后的目录</td>
     * </tr>
     * <tr>
     * <td>/testA/</td>
     * <td>changeToParentDir()</td>
     * <td>/</td>
     * </tr>
     * <tr>
     * <td>/testA/testA_B/</td>
     * <td>changeToParentDir()</td>
     * <td>/testA/</td>
     * </tr>
     * </table>
     * </p>
     *
     * @return boolean
     */
    public boolean changeToParentDir() {
        return changeDir("..");
    }

    /**
     * 切换到根目录
     *
     * @return boolean
     */
    public boolean changeToHomeDir() {
        String homeDir = null;
        try {
            homeDir = channel.getHome();
        } catch (SftpException e) {
            logger.error("can not get home directory", e);
            return false;
        }
        return changeDir(homeDir);
    }

    /**
     * 创建目录
     * <p>
     * 使用示例，SFTP服务器上的目录结构如下：/testA/testA_B/
     * <table border="1">
     * <tr>
     * <td>当前目录</td>
     * <td>方法</td>
     * <td>参数(绝对路径/相对路径)</td>
     * <td>创建成功后的目录</td>
     * </tr>
     * <tr>
     * <td>/testA/testA_B/</td>
     * <td>makeDir("testA_B_C")</td>
     * <td>相对路径</td>
     * <td>/testA/testA_B/testA_B_C/</td>
     * </tr>
     * <tr>
     * <td>/</td>
     * <td>makeDir("/testA/testA_B/testA_B_D")</td>
     * <td>绝对路径</td>
     * <td>/testA/testA_B/testA_B_D/</td>
     * </tr>
     * </table>
     * <br/>
     * <b>注意</b>，当<b>中间目录不存在</b>的情况下，不能够使用绝对路径的方式期望创建中间目录及目标目录。
     * 例如makeDir("/testNOEXIST1/testNOEXIST2/testNOEXIST3")，这是错误的。
     * </p>
     *
     * @param dirName 目录
     * @return boolean
     */
    public boolean makeDir(String dirName) {
        try {
            channel.mkdir(dirName);
            logger.debug("directory successfully created,dir=" + dirName);
            return true;
        } catch (SftpException e) {
            logger.error("failed to create directory", e);
            return false;
        }
    }

    /**
     * 删除文件夹
     *
     * @param dirName
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public boolean delDir(String dirName) {
        if (!changeDir(dirName)) {
            return false;
        }

        Vector<LsEntry> list = null;
        try {
            list = channel.ls(channel.pwd());
        } catch (SftpException e) {
            logger.error("can not list directory", e);
            return false;
        }

        for (LsEntry entry : list) {
            String fileName = entry.getFilename();
            if (!fileName.equals(".") && !fileName.equals("..")) {
                if (entry.getAttrs().isDir()) {
                    delDir(fileName);
                } else {
                    delFile(fileName);
                }
            }
        }

        if (!changeToParentDir()) {
            return false;
        }

        try {
            channel.rmdir(dirName);
            logger.debug("directory " + dirName + " successfully deleted");
            return true;
        } catch (SftpException e) {
            logger.error("failed to delete directory " + dirName, e);
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return boolean
     */
    public boolean delFile(String fileName) {
        if (fileName == null || fileName.trim().equals("")) {
            logger.debug("invalid filename");
            return false;
        }

        try {
            channel.rm(fileName);
            logger.debug("file " + fileName + " successfully deleted");
            return true;
        } catch (SftpException e) {
            logger.error("failed to delete file " + fileName, e);
            return false;
        }
    }

    /**
     * 当前目录下文件及文件夹名称列表
     *
     * @return String[]
     */
    public LsEntry[] ls() {
        return list(Filter.ALL);
    }

    /**
     * 指定目录下文件及文件夹名称列表
     *
     * @return String[]
     */
    public LsEntry[] ls(String pathName) {
        String currentDir = currentDir();
        if (!changeDir(pathName)) {
            return new LsEntry[0];
        }
        ;
        LsEntry[] result = list(Filter.ALL);
        if (!changeDir(currentDir)) {
            return new LsEntry[0];
        }
        return result;
    }

    /**
     * 当前目录下文件名称列表
     *
     * @return String[]
     */
    public LsEntry[] lsFiles() {
        return list(Filter.FILE);
    }

    /**
     * 指定目录下文件名称列表
     *
     * @return String[]
     */
    public LsEntry[] lsFiles(String pathName) {
        String currentDir = currentDir();
        if (!changeDir(pathName)) {
            return new LsEntry[0];
        }
        ;
        LsEntry[] result = list(Filter.FILE);
        if (!changeDir(currentDir)) {
            return new LsEntry[0];
        }
        return result;
    }

    /**
     * 当前目录下文件夹名称列表
     *
     * @return String[]
     */
    public LsEntry[] lsDirs() {
        return list(Filter.DIR);
    }

    /**
     * 指定目录下文件夹名称列表
     *
     * @return String[]
     */
    public LsEntry[] lsDirs(String pathName) {
        String currentDir = currentDir();
        if (!changeDir(pathName)) {
            return new LsEntry[0];
        }
        ;
        LsEntry[] result = list(Filter.DIR);
        if (!changeDir(currentDir)) {
            return new LsEntry[0];
        }
        return result;
    }

    /**
     * 获取路径下该文件的entry对象
     *
     * @param pathName
     * @param fileName
     * @return
     */
    public LsEntry getEntry(String pathName, String fileName) {
        if (!changeDir(pathName)) {
            return null;
        }
        ;
        if (!exist(fileName)) {
            return null;
        }
        LsEntry[] list = ls();
        for (LsEntry entry : list) {
            if (entry.getFilename().equalsIgnoreCase(fileName)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * 当前目录是否存在文件或文件夹
     *
     * @param name 名称
     * @return boolean
     */
    public boolean exist(String name) {
        return exist(ls(), name);
    }

    /**
     * 指定目录下，是否存在文件或文件夹
     *
     * @param path 目录
     * @param name 名称
     * @return boolean
     */
    public boolean exist(String path, String name) {
        return exist(ls(path), name);
    }

    /**
     * 当前目录是否存在文件
     *
     * @param name 文件名
     * @return boolean
     */
    public boolean existFile(String name) {
        return exist(lsFiles(), name);
    }

    /**
     * 指定目录下，是否存在文件
     *
     * @param path 目录
     * @param name 文件名
     * @return boolean
     */
    public boolean existFile(String path, String name) {
        return exist(lsFiles(path), name);
    }

    /**
     * 当前目录是否存在文件夹
     *
     * @param name 文件夹名称
     * @return boolean
     */
    public boolean existDir(String name) {
        return exist(lsDirs(), name);
    }

    /**
     * 指定目录下，是否存在文件夹
     *
     * @param path 目录
     * @param name 文家夹名称
     * @return boolean
     */
    public boolean existDir(String path, String name) {
        return exist(lsDirs(path), name);
    }

    /**
     * 当前工作目录
     *
     * @return String
     */
    public String currentDir() {
        try {
            return channel.pwd();
        } catch (SftpException e) {
            logger.error("failed to get current dir", e);
            return homeDir();
        }
    }

    /**
     * 登出
     */
    public void logout() {
        if (channel != null) {
            channel.quit();
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
        logger.debug("logout successfully");
    }

    /**
     * 上传到当前目录下
     *
     * @param path
     * @param is
     */
    public boolean upload(String fileName, InputStream is) {
        try {
            channel.put(is, fileName, ChannelSftp.OVERWRITE);
            logger.debug("upload successful");
            return true;
        } catch (SftpException e) {
            logger.error("upload failed", e);
            return false;
        }
    }

    /**
     * 下载当前目录下的文件
     *
     * @param remotePath
     * @param fileName
     * @param os
     * @return
     */
    public boolean download(String fileName, OutputStream os) {
        try {
            channel.get(fileName, os);
            logger.debug("download successful");
            return true;
        } catch (SftpException e) {
            logger.error("download file failed", e);
            return false;
        }
    }

    // ------private method ------

    /**
     * 列出当前目录下的文件及文件夹
     *
     * @param filter 过滤参数
     * @return String[]
     */
    @SuppressWarnings("unchecked")
    private LsEntry[] list(Filter filter) {
        Vector<LsEntry> list = null;
        try {
            // ls方法会返回两个特殊的目录，当前目录(.)和父目录(..)
            list = channel.ls(channel.pwd());
        } catch (SftpException e) {
            logger.error("can not list directory", e);
            return new LsEntry[0];
        }

        List<LsEntry> resultList = new ArrayList<LsEntry>();
        for (LsEntry entry : list) {
            if (filter(entry, filter)) {
                resultList.add(entry);
            }
        }
        return resultList.toArray(new LsEntry[0]);
    }

    ;

    /**
     * 判断是否是否过滤条件
     *
     * @param entry LsEntry
     * @param f     过滤参数
     * @return boolean
     */
    private boolean filter(LsEntry entry, Filter f) {
        if (f.equals(Filter.ALL)) {
            return !entry.getFilename().equals(".")
                    && !entry.getFilename().equals("..");
        } else if (f.equals(Filter.FILE)) {
            return !entry.getFilename().equals(".")
                    && !entry.getFilename().equals("..")
                    && !entry.getAttrs().isDir();
        } else if (f.equals(Filter.DIR)) {
            return !entry.getFilename().equals(".")
                    && !entry.getFilename().equals("..")
                    && entry.getAttrs().isDir();
        }
        return false;
    }

    /**
     * 根目录
     *
     * @return String
     */
    private String homeDir() {
        try {
            return channel.getHome();
        } catch (SftpException e) {
            return "/";
        }
    }

    /**
     * 判断字符串是否存在于数组中
     *
     * @param files 文件数组
     * @param str   字符串
     * @return boolean
     */
    private boolean exist(LsEntry[] files, String str) {
        if (files == null || files.length == 0) {
            return false;
        }
        if (str == null || str.trim().equals("")) {
            return false;
        }
        for (LsEntry entry : files) {
            if (entry.getFilename().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 枚举，用于过滤文件和文件夹
     */
    private enum Filter {
        /**
         * 文件及文件夹
         */
        ALL, /**
         * 文件
         */
        FILE, /**
         * 文件夹
         */
        DIR
    }
}