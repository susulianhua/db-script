package org.tinygroup.flowbasiccomponent.util;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.flowbasiccomponent.DefaultFileFormat;
import org.tinygroup.flowbasiccomponent.FileFormat;
import org.tinygroup.flowbasiccomponent.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.flowbasiccomponent.exception.FlowComponentException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {

    public static final int BUFSIZE = 1024 * 8;
    private static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    private static Map<String, FileFormat> fileFormats = new HashMap<String, FileFormat>();

    static {
        Collection<FileFormat> collection = BeanContainerFactory
                .getBeanContainer(FileUtil.class.getClassLoader()).getBeans(
                        FileFormat.class);
        for (FileFormat fileFormat : collection) {
            fileFormats.put(fileFormat.getType(), fileFormat);
        }
    }

    public static boolean existsFile(String filePath) {
        FileObject fileObject = VFS.resolveFile(filePath);
        if (fileObject.isExist()) {
            return true;
        }
        return false;
    }

    public static Object file2Object(String filePath, String classPath,
                                     String type) {
        try {
            // 文件不存在
            if (StringUtil.isBlank(filePath) || !existsFile(filePath)) {
                LOGGER.logMessage(LogLevel.ERROR, "文件：{0}不存在", filePath);
                throw new FlowComponentException(
                        FlowComponentExceptionErrorCode.FILE_NOT_FOUND,
                        filePath);
            }
            // 如果未指定格式化实现类，则采用默认提供的处理方式
            if (StringUtil.isBlank(type)) {
                type = DefaultFileFormat.TINY_FILE_FORMAT_TYPE;
            }
            // 如果未找到指定的格式化实现类，则抛出错误
            FileFormat fileFormat = fileFormats.get(type);
            if (fileFormat == null) {
                LOGGER.logMessage(LogLevel.ERROR, "文件格式化工具：{0}未找到", type);
                throw new FlowComponentException(
                        FlowComponentExceptionErrorCode.FILE_FORMAT_NOT_FOUND,
                        type);
            }
            return fileFormat.formatFile(filePath, classPath);
        } catch (FlowComponentException e) {
            LOGGER.logMessage(LogLevel.ERROR, "文件：{0}转对象时出错,错误信息：{1}",
                    filePath, e);
            throw e;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "文件：{0}转对象时出错,错误信息：{1}",
                    filePath, e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.FILE_SAVE_TO_OBJECT_FAILED,
                    filePath, e);
        }
    }

    /**
     * @param filePath 存放文件路径
     * @param t        对象
     * @param type     序列化实现方式
     */
    public static <T> void object2File(String filePath, T t, String type) {
        try {
            // 如果未指定格式化实现类，则采用默认提供的处理方式
            if (StringUtil.isBlank(type)) {
                type = DefaultFileFormat.TINY_FILE_FORMAT_TYPE;
            }
            // 如果未找到指定的格式化实现类，则抛出错误
            FileFormat fileFormat = fileFormats.get(type);
            if (fileFormat == null) {
                LOGGER.logMessage(LogLevel.ERROR, "文件格式化工具：{0}未找到", type);
                throw new FlowComponentException(
                        FlowComponentExceptionErrorCode.FILE_FORMAT_NOT_FOUND,
                        type);
            }
            String fileContent = fileFormat.formatObject(t);
            FileObject fileObject = VFS.resolveFile(filePath);
            OutputStreamWriter write = new OutputStreamWriter(
                    fileObject.getOutputStream(), fileFormat.getEncoding());
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(fileContent);
            writer.close();
        } catch (FlowComponentException e) {
            LOGGER.logMessage(LogLevel.ERROR, "对象：{0}转存到文件：{1}时出错。错误信息：{2}", t
                    .getClass().getName(), filePath, e);
            throw e;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "对象：{0}转存到文件：{1}时出错。错误信息：{2}", t
                    .getClass().getName(), filePath, e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.OBJECT_SAVE_TO_FILE_FAILED,
                    t.getClass().getName(), filePath, e);
        }
    }

    @SuppressWarnings("resource")
    public static void mergeFiles(String outFile, List<String> files) {
        FileChannel outChannel = null;
        try {
            outChannel = new FileOutputStream(VFS.resolveFile(outFile)
                    .getAbsolutePath()).getChannel();
            for (String f : files) {
                String absolutePath = VFS.resolveFile(f).getAbsolutePath();
                File file = new File(absolutePath);
                if (!file.exists()) {
                    continue;
                }
                FileChannel fc = new FileInputStream(absolutePath).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                while (fc.read(bb) != -1) {
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                fc.close();
            }
        } catch (IOException ioe) {
            LOGGER.logMessage(LogLevel.ERROR, "文件合并失败，合并预期输出文件：{0}，错误信息：{1}",
                    outFile, ioe);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.FILE_MERGE_FAILED, outFile,
                    ioe);
        } finally {
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException ignore) {
                LOGGER.logMessage(LogLevel.ERROR,
                        "文件合并失败，合并预期输出文件：{0}，错误信息：{1}", outFile, ignore);
                throw new FlowComponentException(
                        FlowComponentExceptionErrorCode.FILE_MERGE_FAILED,
                        outFile, ignore);
            }
        }
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        FileObject fileObject = VFS.resolveFile(filePath);
        if (!fileObject.isExist()) {
            LOGGER.logMessage(LogLevel.ERROR, "文件：{0}不存在", filePath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.FILE_NOT_FOUND, filePath);
        }
        return fileObject.getSize();
    }
}
