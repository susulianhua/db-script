package org.tinygroup.flowbasiccomponent.util;

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.convert.objectxml.jaxb.ObjectToXml;
import org.tinygroup.convert.objectxml.jaxb.XmlToObject;
import org.tinygroup.flowbasiccomponent.FlowComponentConstants;
import org.tinygroup.flowbasiccomponent.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.flowbasiccomponent.exception.FlowComponentException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlparser.XmlNodeType;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.io.IOException;
import java.util.List;

/**
 * XMLxml树处理工具类
 */
public class XMLOperatorUtil {

    private static Logger LOGGER = LoggerFactory
            .getLogger(XMLOperatorUtil.class);

    /**
     * xml树转换
     *
     * @param xml
     * @return
     */
    public static XmlNode getXmlNode(String xml) {
        return new XmlStringParser().parse(xml).getRoot();
    }

    /**
     * 文件转xmlNode
     *
     * @param encoding
     * @param filePath
     * @return
     */
    public static XmlNode getXmlNodeFromFile(String filePath, String encoding) {
        try {
            if (StringUtil.isBlank(encoding)) {
                encoding = FlowComponentConstants.DEFAULT_ENCODING;
            }
            FileObject fileObject = VFS.resolveFile(filePath);
            String applicationConfig = StreamUtil.readText(
                    fileObject.getInputStream(), encoding, true);
            XmlNode xmlNode = new XmlStringParser().parse(applicationConfig)
                    .getRoot();
            return xmlNode;
        } catch (IOException e) {
            LOGGER.logMessage(LogLevel.ERROR, "将文件：{0}序列化为XmlNode对象时失败，错误信息：{1}",
                    filePath, e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.FILE_SAVE_TO_OBJECT_FAILED,
                    filePath, e);
        }
    }

    /**
     * 将修改后的xml写入xml树
     *
     * @param filePath
     * @param xmlNode
     */
    private static void writeToXml(String filePath, XmlNode xmlNode,
                                   String encoding) {
        try {
            if (StringUtil.isBlank(encoding)) {
                encoding = FlowComponentConstants.DEFAULT_ENCODING;
            }
            FileObject fileObject = VFS.resolveFile(filePath);
            XmlNode xn = new XmlNode(XmlNodeType.XML_DECLARATION);
            xn.setAttribute("version", "1.0");
            xn.setAttribute("encoding", encoding);
            StringBuffer sb = new StringBuffer();
            sb.append(xn);
            sb.append(xmlNode);
            StreamUtil.writeText(sb.toString(), fileObject.getOutputStream(),
                    encoding, true);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "将修改后的xml内容导出到xml树：{0}时失败,错误信息：{1}",
                    filePath, e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.OBJECT_SAVE_TO_FILE_FAILED,
                    xmlNode.getClass().getName(), filePath, e);
        }
    }

    public static XmlNode getSubNode(String nodePath, XmlNode xmlNode) {
        PathFilter<XmlNode> pathFilter = new PathFilter<XmlNode>(xmlNode);
        XmlNode node = pathFilter.findNode(nodePath);
        return node;
    }

    public static String getNodeListStr(List<XmlNode> nodes) {
        String xml = "";
        if (nodes != null && nodes.size() > 0) {
            for (XmlNode node : nodes) {
                xml += node.toString();
            }
        }
        return xml;
    }

    /**
     * 在指定的节点路径下追加内容
     *
     * @param filePath
     * @param nodePath
     * @param nodeValue
     */
    public static void appendValue(XmlNode xml, String nodePath,
                                   String nodeValue) {
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作开始：{0}，执行追加内容操作", xml);
        XmlNode node = getSubNode(nodePath, xml);
        if (node == null) {
            LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到指定的节点：{1}", xml,
                    nodePath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.XML_NODE_NOT_FOUND, xml,
                    nodePath);
        }
        LOGGER.logMessage(LogLevel.DEBUG, "在节点：{0}下追加内容：{1}", nodePath,
                nodeValue);
        node.addContent(nodeValue);
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作结束：{0}，执行追加内容操作成功", xml);
    }

    /**
     * 节点插入
     *
     * @param filePath
     * @param nodePath
     * @param nodeValue
     */
    public static void insertNode(XmlNode xml, String nodePath, String xmlNode) {
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作开始：{0}，执行追加内容操作", xml);
        XmlNode node = getSubNode(nodePath, xml);
        if (node == null) {
            LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到指定的节点：{1}", xml,
                    nodePath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.XML_NODE_NOT_FOUND, xml,
                    nodePath);
        }
        XmlNode xn = new XmlStringParser().parse(xmlNode).getRoot();
        LOGGER.logMessage(LogLevel.DEBUG, "在节点：{0}下追加内容：{1}", nodePath,
                xn.toString());
        node.addNode(xn);
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作结束：{0}，执行追加内容操作成功", xml);
    }

    /**
     * 修改指定的节点路径的内容
     *
     * @param filePath
     * @param nodePath
     * @param nodeValue
     */
    public static void updateValue(XmlNode xml, String nodePath,
                                   String nodeValue) {
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作开始：{0}，执行修改节点操作", xml);
        XmlNode node = getSubNode(nodePath, xml);
        if (node == null) {
            LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到指定的节点：{1}", xml,
                    nodePath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.XML_NODE_NOT_FOUND, xml,
                    nodePath);
        }
        List<XmlNode> nodes = node.removeSubNotes();
        LOGGER.logMessage(LogLevel.DEBUG, "修改节点：{0}的原内容为：{1}", nodePath,
                getNodeListStr(nodes));
        node.addContent(nodeValue);
        LOGGER.logMessage(LogLevel.DEBUG, "修改节点：{0}的新内容为：{1}", nodePath,
                nodeValue);
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作结束：{0}，执行修改节点操作成功", xml);
    }

    /**
     * 替换指定的节点路径的内容
     *
     * @param filePath
     * @param nodePath
     * @param nodeValue
     */
    public static void repalceNode(XmlNode xml, String nodePath, String xmlNode) {
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作开始：{0}，执行覆盖节点操作", xml);
        XmlNode node = getSubNode(nodePath, xml);
        if (node == null) {
            LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到指定的节点：{1}", xml,
                    nodePath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.XML_NODE_NOT_FOUND, xml,
                    nodePath);
        }
        List<XmlNode> nodes = node.removeSubNotes();
        LOGGER.logMessage(LogLevel.DEBUG, "覆盖节点：{0}的原内容为：{1}", nodePath,
                getNodeListStr(nodes));
        XmlNode xn = new XmlStringParser().parse(xmlNode).getRoot();
        LOGGER.logMessage(LogLevel.DEBUG, "覆盖节点：{0}的新内容为：{1}", nodePath,
                xn.toString());
        node.addNode(xn);
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作结束：{0}，执行覆盖节点操作", xml);
    }

    /**
     * 删除指定的节点路径，包括该节点本身
     *
     * @param filePath
     * @param nodePath
     * @param nodeValue
     */
    public static String deleteNode(XmlNode xml, String nodePath) {
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作开始：{0}，执行删除节点操作", xml);
        XmlNode node = getSubNode(nodePath, xml);
        if (node == null) {
            LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到指定删除的节点：{1}", xml,
                    nodePath);
            return null;
        }
        LOGGER.logMessage(LogLevel.DEBUG, "在指定节点：{0}删除的内容为：{1}", nodePath,
                node.toString());
        XmlNode parnetNode = node.getParent();
        parnetNode.removeNode(node);
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作结束：{0}，执行删除节点操作成功", xml);
        return node.toString();
    }

    /**
     * 删除指定的节点路径下的子节点，不删除节点本身
     *
     * @param filePath
     * @param nodePath
     * @param nodeValue
     */
    public static String deleteSubNode(XmlNode xml, String nodePath) {
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作开始：{0}，执行删除节点操作", xml);
        XmlNode node = getSubNode(nodePath, xml);
        if (node == null) {
            LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到指定删除的节点：{1}", xml,
                    nodePath);
            return null;
        }
        List<XmlNode> deleteNodes = node.removeSubNotes();
        String removeXml = getNodeListStr(deleteNodes);
        LOGGER.logMessage(LogLevel.DEBUG, "在指定节点：{0}删除的内容为：{1}", nodePath,
                removeXml);
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作结束：{0}，执行删除节点操作成功", xml);
        return removeXml;
    }

    /**
     * 删除指定的节点路径(nodePath)下所有节点名为nodeName的节点
     *
     * @param filePath
     * @param nodePath
     * @param nodeName
     */
    public static String deleteByNodeName(XmlNode xml, String nodePath,
                                          String nodeName) {
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作开始：{0}，执行删除节点操作", xml);
        XmlNode node = getSubNode(nodePath, xml);
        if (node == null) {
            LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到指定删除的节点：{1}", xml,
                    nodePath);
            return null;
        }
        List<XmlNode> deleteNodes = node.removeNode(nodeName);
        String removeXml = getNodeListStr(deleteNodes);
        LOGGER.logMessage(LogLevel.DEBUG, "在指定节点：{0}删除的内容为：{1}", nodePath,
                removeXml);
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作结束：{0}，执行删除节点操作成功", xml);
        return removeXml;
    }

    /**
     * 查询指定节点路径的内容
     *
     * @param filePath
     * @param nodePath
     * @param nodeName
     */
    public static XmlNode queryNode(XmlNode xml, String nodePath) {
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作开始：{0}，执行查节点操作", xml);
        XmlNode node = getSubNode(nodePath, xml);
        if (node == null) {
            LOGGER.logMessage(LogLevel.INFO, "在xml树：{0}中未找到指定查询的节点：{1}", xml,
                    nodePath);
            return null;
        }
        if (node.getSubNodes().size() == 1
                && StringUtil.isBlank(node.getSubNodes().get(0).getNodeName())) {
            return node.getSubNodes().get(0);
        }
        LOGGER.logMessage(LogLevel.DEBUG,
                "XML树操作结束：{0}，执行查节点操作成功。查询指定节点：{1}的查询结果为：{2}", xml, nodePath,
                node.toString());
        return node;
    }

    /**
     * 查询指定节点路径的子节点内容
     *
     * @param filePath
     * @param nodePath
     * @param nodeName
     */
    public static List<XmlNode> querySubNode(XmlNode xml, String nodePath) {
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作开始：{0}，执行查节点操作", xml);
        XmlNode node = getSubNode(nodePath, xml);
        if (node == null) {
            LOGGER.logMessage(LogLevel.INFO, "在xml树：{0}中未找到指定查询的节点：{1}", xml,
                    nodePath);
            return null;
        }
        List<XmlNode> queryNodes = node.getSubNodes();
        LOGGER.logMessage(LogLevel.DEBUG,
                "XML树操作结束：{0}，执行查节点操作成功。查询指定节点：{1}的子节点共有：{2}个", xml, nodePath,
                queryNodes.size());
        return queryNodes;
    }

    /**
     * 查询指定节点路径下节点名称为nodeName的节点内容
     *
     * @param filePath
     * @param nodePath
     * @param nodeName
     */
    public static List<XmlNode> queryByNodeName(XmlNode xml, String nodePath,
                                                String nodeName) {
        LOGGER.logMessage(LogLevel.DEBUG, "XML树操作开始：{0}，执行查节点操作", xml);
        XmlNode node = getSubNode(nodePath, xml);
        if (node == null) {
            LOGGER.logMessage(LogLevel.INFO, "在xml树：{0}中未找到指定查询的节点：{1}", xml,
                    nodePath);
            return null;
        }
        List<XmlNode> queryNodes = node.getSubNodes(nodeName);
        LOGGER.logMessage(LogLevel.DEBUG,
                "XML树操作结束：{0}，执行查节点操作成功。查询指定节点：{1}的子节点名为：{2}的节点共有：{3}个", xml,
                nodePath, nodeName, queryNodes.size());
        return queryNodes;
    }

    /**
     * 节点move，将节点1移动到节点2下面
     *
     * @param filePath
     * @param fromNodePath
     * @param toNodePath
     */
    public static void moveNode(XmlNode xml, String fromNodePath,
                                String toNodePath) {
        LOGGER.logMessage(LogLevel.DEBUG,
                "XML树操作开始：{0}，执行节点move操作，将节点：{1}移动到节点：{2}", xml, fromNodePath,
                toNodePath);
        XmlNode formNode = getSubNode(fromNodePath, xml);
        if (formNode == null) {
            LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到准备移动节点：{1}", xml,
                    fromNodePath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.XML_NODE_NOT_FOUND, xml,
                    fromNodePath);
        }
        XmlNode toNode = getSubNode(toNodePath, xml);
        if (toNode == null) {
            LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到目的地节点：{1}", xml,
                    toNodePath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.XML_NODE_NOT_FOUND, xml,
                    toNodePath);
        }
        // 移动节点到目的地
        toNode.getSubNodes().add(formNode);
        // 删除原节点
        formNode.getParent().removeNode(formNode);
        LOGGER.logMessage(LogLevel.DEBUG,
                "XML树操作结束：{0}，执行节点move操作，成功将节点：{1}移动到节点：{2}", xml,
                fromNodePath, toNodePath);
    }

    /**
     * 节点copy，将节点1复制到节点2下面
     *
     * @param filePath
     * @param fromNodePath
     * @param toNodePath
     */
    public static void copyNode(XmlNode xml, String fromNodePath,
                                String toNodePath) {
        LOGGER.logMessage(LogLevel.DEBUG,
                "XML树操作开始：{0}，执行节点copy操作，将节点：{1}复制到节点：{2}", xml, fromNodePath,
                toNodePath);
        XmlNode formNode = getSubNode(fromNodePath, xml);
        if (formNode == null) {
            LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到准备复制节点：{1}", xml,
                    fromNodePath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.XML_NODE_NOT_FOUND, xml,
                    fromNodePath);
        }
        XmlNode toNode = getSubNode(toNodePath, xml);
        if (toNode == null) {
            LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到目的地节点：{1}", xml,
                    toNodePath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.XML_NODE_NOT_FOUND, xml,
                    toNodePath);
        }
        toNode.getSubNodes().add(formNode);
        LOGGER.logMessage(LogLevel.DEBUG,
                "XML树操作结束：{0}，执行节点copy操作，成功将节点：{1}复制到节点：{2}", xml,
                fromNodePath, toNodePath);
    }

    /**
     * 导出xml树到文件
     *
     * @param xmlFilePath
     * @param exportfilePath
     * @param nodePath
     */
    public static void export(XmlNode xml, String exportfilePath,
                              String nodePath, String encoding) {
        if (!StringUtil.isBlank(nodePath)) {
            LOGGER.logMessage(LogLevel.DEBUG,
                    "XML树操作开始：{0}，执行导出操作，将节点：{1}导出到文件：{2}", xml, nodePath,
                    exportfilePath);
        } else {
            LOGGER.logMessage(LogLevel.DEBUG,
                    "XML树操作开始：{0}，执行导出操作，将xml导出到文件：{2}", xml, exportfilePath);
        }
        if (!StringUtil.isBlank(nodePath)) {
            XmlNode subNode = getSubNode(nodePath, xml);
            if (subNode == null) {
                LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到需要导出的节点：{1}",
                        xml, nodePath);
                throw new FlowComponentException(
                        FlowComponentExceptionErrorCode.XML_NODE_NOT_FOUND,
                        xml, nodePath);
            }
            writeToXml(exportfilePath, subNode, encoding);
            LOGGER.logMessage(LogLevel.DEBUG,
                    "xml树操作成功：{0}，执行导出操作，将节点：{1}成功导出到文件：{2}", xml, nodePath,
                    exportfilePath);
            return;
        }
        writeToXml(exportfilePath, xml, encoding);
        LOGGER.logMessage(LogLevel.DEBUG,
                "xml树操作成功：{0}，执行导出操作，将xml树成功导出到文件：{2}", xml, exportfilePath);
    }

    /**
     * 导出xml到xml树
     *
     * @param xmlFilePath
     * @param exportfilePath
     * @param nodePath
     * @param encoding
     */
    public static void importFile(XmlNode xml, String importfilePath,
                                  String nodePath, String encoding) {
        if (!StringUtil.isBlank(nodePath)) {
            LOGGER.logMessage(LogLevel.DEBUG,
                    "XML树操作开始：{0}，执行导入操作，将文件:{1}导入到节点：{2}", xml,
                    importfilePath, nodePath);
        } else {
            LOGGER.logMessage(LogLevel.DEBUG,
                    "XML树操作开始：{0}，执行导入操作，将文件:{1}导入xml树最后一个节点后方", xml,
                    importfilePath);
        }
        XmlNode importNode = getXmlNodeFromFile(importfilePath, encoding);
        if (!StringUtil.isBlank(nodePath)) {
            XmlNode subNode = getSubNode(nodePath, xml);
            if (subNode == null) {
                LOGGER.logMessage(LogLevel.ERROR, "在xml树：{0}中未找到准备导入的节点：{1}",
                        xml, nodePath);
                throw new FlowComponentException(
                        FlowComponentExceptionErrorCode.XML_NODE_NOT_FOUND,
                        xml, nodePath);
            }
            subNode.getSubNodes().add(importNode);
            LOGGER.logMessage(LogLevel.DEBUG,
                    "xml树操作成功：{0}，执行导入操作，将文件:{1}导入到节点：{2}成功", xml,
                    importfilePath, nodePath);
        }
        xml.getSubNodes().add(importNode);
        LOGGER.logMessage(LogLevel.DEBUG,
                "xml树操作成功：{0}，执行导入操作，将文件:{1}导入xml树最后一个节点后方成功", xml,
                importfilePath);
    }

    /**
     * 获取节点属性值
     *
     * @param xmlNode
     * @param nodePath
     * @return
     */
    public static String getValue(XmlNode xmlNode, String nodePath) {
        XmlNode subNode = getSubNode(nodePath, xmlNode);
        return subNode.getContent();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object formatXml(String xml, String classPath) {
        try {
            Class<?> clazz = Class.forName(classPath);
            XmlToObject xmlToObject = new XmlToObject(clazz);
            return xmlToObject.convert(xml);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "对象：{0}转存到文件：{1}时出错。错误信息：{2}",
                    xml, classPath, e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.OBJECT_SAVE_TO_FILE_FAILED,
                    xml, classPath, e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> String formatObject(T t) {
        try {
            ObjectToXml objectToXml = new ObjectToXml(t.getClass(), true);
            return objectToXml.convert(t);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "对象:{0}转换为XML字符串失败,错误信息：{1}",
                    t, e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.OBJECT_CONVERT_TO_XML_FAILED,
                    t, e);
        }
    }
}
