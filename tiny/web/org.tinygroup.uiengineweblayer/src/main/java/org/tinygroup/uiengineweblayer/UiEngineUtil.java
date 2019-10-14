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
package org.tinygroup.uiengineweblayer;

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.vfs.FileObject;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类，合并公用方法
 *
 * @author yancheng11334
 *
 */
public final class UiEngineUtil {

    static Pattern urlPattern = Pattern
            .compile("(url[(][\"\']?)(.*?)([\"\']?[)])");

    public static void writeCss(FileObject fileObject,
                                ByteArrayOutputStream outputStream, String contextPath,
                                String servletPath) throws IOException {
        InputStream stream = new BufferedInputStream(
                fileObject.getInputStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        StreamUtil.io(stream, byteArrayOutputStream, true, false);
        replaceCss(outputStream, contextPath,
                new String(byteArrayOutputStream.toByteArray(), "UTF-8"),
                fileObject.getPath());
    }

    public static void replaceCss(OutputStream outputStream,
                                  String contextPath, String string, String servletPath)
            throws IOException {
        Matcher matcher = urlPattern.matcher(string);
        int curpos = 0;
        while (matcher.find()) {
            outputStream.write(string.substring(curpos, matcher.start())
                    .getBytes("UTF-8"));
            if (matcher.group(2).trim().startsWith("data:")) {
                outputStream.write(matcher.group().getBytes("UTF-8"));
            } else {
                outputStream.write(matcher.group(1).getBytes("UTF-8"));
                outputStream.write(convertUrl(contextPath, matcher.group(2),
                        servletPath).getBytes("UTF-8"));
                outputStream.write(matcher.group(3).getBytes("UTF-8"));
            }
            curpos = matcher.end();
            continue;
        }
        outputStream.write(string.substring(curpos).getBytes("UTF-8"));
    }

    private static String convertUrl(String contextPath, String url,
                                     String servletPath) {
        if (contextPath == null) {
            contextPath = "";
        }
        if (url.startsWith("/") || url.startsWith("\\")) {
            return contextPath + url;
        } else if (url.startsWith("../") || url.startsWith("..\\")) {
            String firstThree = url.substring(0, 3);
            int count = 0;
            while (url.startsWith(firstThree)) {
                count++;
                url = url.substring(3);
            }
            String[] paths = servletPath.split("/");
            StringBuffer sb = new StringBuffer(contextPath);
            for (int i = 0; i < paths.length - count - 1; i++) {
                sb.append(paths[i]).append("/");
            }
            sb.append(url);
            return sb.toString();
        }
        return contextPath
                + servletPath.substring(0, servletPath.lastIndexOf('/') + 1)
                + url;
    }

}
