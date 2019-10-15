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
package com.xquant.fileresolver.util;


import com.xquant.common.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileDealUtil {
    public static void write(File file, String content) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(content.getBytes("UTF-8"));// utf8的方式保存文件（即文件时UTF-8的，即里面的文字就是UTF-8的）
        outputStream.close();
    }

    public static String read(File file) throws IOException {
        StringBuffer str = new StringBuffer("");
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            int ch = 0;
            while ((ch = fr.read()) != -1) {
                str.append((char) ch);
            }
        } catch (IOException e) {
            System.out.println("File reader出错");
        } finally {
            if (fr != null) {
                fr.close();
            }
        }

        return str.toString();

    }


    private static boolean createFolder(File baseFolder, File baseFile) throws IOException {
        String timeInfo = baseFile.lastModified() + "";
        if (!baseFolder.mkdir()) { //如果目录存在
            File f = new File(baseFolder, "info");//读取文件修改日志
            if (f.exists()) {
                String content = read(f);
                if (content.equals(timeInfo)) { //日期匹配则返回
                    return true;
                } else {
                    FileUtil.delete(baseFolder);
                    return false;
                }
            }
        }
        File f = new File(baseFolder, "info");
        if (!f.exists()) {
            f.createNewFile();
        }
        write(f, timeInfo);
        return true;

    }

    public static List<String> unpack(String[] pathArray) throws IOException {
        List<String> list = new ArrayList<String>();
        // 总目录
        File superFolder = new File(System.getProperty("java.io.tmpdir"));
        // 顶级文件路径
        File baseFile = new File(pathArray[0].replace("jar:", "").replace("file:", ""));
        // 根据顶级文件在总目录下生成顶级目录
        File baseFolder = new File(superFolder, computeFileName(baseFile.getName(), true));
        if (!createFolder(baseFolder, baseFile)) {
            boolean flag = createFolder(baseFolder, baseFile);
            if (!flag) {
                throw new RuntimeException("删除失败，无法创建");
            }
        }
        File targetFolder = baseFolder;
        File targetFile = baseFile;
        for (int i = 1; i < pathArray.length - 1; i++) {
            if (i != 1) {
                targetFile = new File(targetFolder, computeFileName(pathArray[i - 1], false));
                targetFolder = new File(targetFolder, computeFileName(pathArray[i - 1], true));
                targetFolder.mkdir();
            }
            File unpackedFile = unpack(targetFolder, targetFile, pathArray[i]);
            list.add(unpackedFile.getAbsolutePath());
        }
        return list;
    }

    private static File unpack(File unpackFolder, File targetParentFile, String unpackTarget) throws IOException {
        JarFile targetParentJar = new JarFile(targetParentFile);
        if (unpackTarget.startsWith("/")) {
            unpackTarget = unpackTarget.substring(1);
        }
        JarEntry entry = targetParentJar.getJarEntry(unpackTarget);
        String targetFileName = computeFileName(unpackTarget, false);
        File targetJarFile = new File(unpackFolder, targetFileName);
        if (targetJarFile.exists()) {
            return targetJarFile;
        }
        unpack(targetParentJar, targetJarFile, entry);
        return targetJarFile;
    }

    private static String computeFileName(String path, boolean dealJar) {
        String name = path; // a
        if (name.lastIndexOf("/") != -1) { // 这里不能改成File.separator
            name = name.substring(name.lastIndexOf("/") + 1);
        }
        if (dealJar && name.endsWith(".jar")) {
            name = name.substring(0, name.length() - 4);
        }
        return name;
    }

    private static void unpack(JarFile jarFile, File file, JarEntry entry) throws IOException {
        InputStream inputStream = jarFile.getInputStream(entry);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[32768];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } finally {
                outputStream.close();
            }
        } finally {
            inputStream.close();
        }
    }

}
