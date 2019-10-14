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

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.StringUtils;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

public class SftpFileObjectTest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    // 使用此测试需要修改username，password和sftpserver三个参数值
    private static String username = "";
    private static String password = "";
    private static String sftpserver = "";
    private static String sftpurl = "sftp://" + username + ":" + password + "@"
            + sftpserver + ":22";

    public static void main(String[] args) {
        String path = SftpFileObjectTest.class.getResource("/test/VFSTest.txt")
                .getFile();
        // 上传
        upload(path);
        // 下载
        download(path);
        try {
            vfsSftpReadTest();
            vfsSftpWriteTest(path);
            vfsSftpChildrenTest();
            sftpUrlTest();
            jschConnectionTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void vfsSftpReadTest() throws Exception {
        VFS.addSchemaProvider(new SftpSchemaProvider());
        FileObject obj = VFS.resolveFile(sftpurl + "/opt/VFSTest/VFSTest.txt");
        printInfo(obj);
    }

    public static void printInfo(FileObject fileObject) throws Exception {
        System.out.println("MTime: "
                + sdf.format(new Date(fileObject.getLastModifiedTime())));
        InputStream in = fileObject.getInputStream();
        in.close();
        BufferedReader read = new BufferedReader(new InputStreamReader(in,
                "GB2312"));
        String str = null;
        while ((str = read.readLine()) != null) {
            System.out.println(str);
        }
        System.out.println("inputStream=" + fileObject.getInputStream());
        // OutputStream out = fileObject.getOutputStream();
        // System.out.println("outputStream=" + fileObject.getOutputStream());
        // out.close();
    }

    public static void vfsSftpWriteTest(String path) throws Exception {
        VFS.addSchemaProvider(new SftpSchemaProvider());
        FileObject obj = VFS.resolveFile(sftpurl + "/opt/VFSTest/VFSTest.txt");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                path));
        BufferedOutputStream bos = new BufferedOutputStream(
                obj.getOutputStream());
        byte[] buf = new byte[1024];
        while ((bis.read(buf) > 0)) {
            bos.write(buf);
        }
        bis.close();
        bos.flush();
        bos.close();
    }

    public static void vfsSftpChildrenTest() throws Exception {
        VFS.addSchemaProvider(new SftpSchemaProvider());
        FileObject obj = VFS.resolveFile(sftpurl + "/opt/VFSTest");
        System.out.println(obj.getAbsolutePath() + "<><><>" + obj.getPath());
        System.out.println("MTime: "
                + sdf.format(new Date(obj.getLastModifiedTime())));
        System.out.println("Size: " + obj.getSize());
        List<FileObject> list = obj.getChildren();
        for (FileObject fo : list) {
            System.out.println(fo.getAbsolutePath() + "<===>" + fo.getPath());
            System.out.println("MTime: "
                    + sdf.format(new Date(fo.getLastModifiedTime())));
            System.out.println("Size: " + fo.getSize());
            if (fo.isFolder()) {
                List<FileObject> folist = fo.getChildren();
                for (FileObject o : folist) {
                    System.out.println("===>" + o.getAbsolutePath() + "<===>"
                            + o.getPath());
                    System.out.println("MTime: "
                            + sdf.format(new Date(o.getLastModifiedTime())));
                    System.out.println("Size: " + o.getSize());
                }
            }
        }
        // 查找特定子文件
        FileObject temp = obj.getChild("VFSTest.txt");
        if (temp != null && !temp.isFolder()) {
            BufferedReader read = new BufferedReader(new InputStreamReader(
                    temp.getInputStream(), "gb2312"));
            String str = null;
            while ((str = read.readLine()) != null) {
                System.out.println(str);// 原样输出读到的内容
            }
        }
    }

    public static void upload(String path) {
        VFS.addSchemaProvider(new SftpSchemaProvider());
        IFileObject obj = (IFileObject) VFS.resolveFile(sftpurl
                + "/opt/VFSTest/VFSTest.txt");
        obj.upload(path);
    }

    public static void download(String path) {
        VFS.addSchemaProvider(new SftpSchemaProvider());
        IFileObject obj = (IFileObject) VFS.resolveFile(sftpurl
                + "/opt/VFSTest/VFSTest.txt");
        obj.download(path);
    }

    public static void sftpUrlTest() throws MalformedURLException {
        URL url = new URL(null, sftpurl + "/opt/", new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                return null;
            }
        });
        System.out.println(url.getPath());
        System.out.println(url.getUserInfo());
        System.out.println(url.getProtocol());
    }

    @SuppressWarnings("unused")
    public static void jschConnectionTest() throws Exception {
        String SFTP_PRIV_KEY = "";
        // 请根据实际测试环境修改IP,PORT,USERNAME和PASSWORD
        String SFTP_SERVER_HOST = sftpserver;
        int SFTP_SERVER_PORT = 22;
        String SFTP_SERVER_USERNAME = username;
        String SFTP_SERVER_PASSWORD = password;
        int SFTP_DEFAULT_TIMEOUT = 30000;

        Session session = null;
        Channel channel = null;
        JSch jsch = new JSch(); // 创建JSch对象
        // 按照用户名,主机ip,端口获取一个Session对象

        // 添加私钥(信任登录方式)
        if (StringUtils.isNotBlank(SFTP_PRIV_KEY)) {
            jsch.addIdentity(SFTP_PRIV_KEY);
        }

        if (SFTP_SERVER_PORT <= 0) {
            session = jsch.getSession(SFTP_SERVER_USERNAME, SFTP_SERVER_HOST);// 连接服务器，采用默认端口
        } else {
            session = jsch.getSession(SFTP_SERVER_USERNAME, SFTP_SERVER_HOST,
                    SFTP_SERVER_PORT);// 采用指定的端口连接服务器
        }

        session.setPassword(SFTP_SERVER_PASSWORD); // 设置密码

        // 如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new NullPointerException("session is null");
        }

        // 具体config中需要配置那些内容，请参照sshd服务器的配置文件/etc/ssh/sshd_config的配置
        Properties config = new Properties();

        // 设置不用检查hostKey
        // 如果设置成“yes”，ssh就不会自动把计算机的密匙加入“$HOME/.ssh/known_hosts”文件，
        // 并且一旦计算机的密匙发生了变化，就拒绝连接。
        config.put("StrictHostKeyChecking", "no");

        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(SFTP_DEFAULT_TIMEOUT); // 设置timeout时候
        session.connect(); // 经由过程Session建立链接

        channel = (ChannelSftp) session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
    }
}
