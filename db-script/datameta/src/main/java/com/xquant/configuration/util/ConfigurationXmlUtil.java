package com.xquant.configuration.util;


import com.xquant.common.StreamUtil;
import com.xquant.parser.filter.NameFilter;
import com.xquant.vfs.FileObject;
import com.xquant.xmlparser.node.XmlNode;
import com.xquant.xmlparser.parser.XmlStringParser;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 所有对配置xml操作的代码都放置到此类中
 * @author chenjiao
 *
 */
public class ConfigurationXmlUtil {
	/**
	 * 根据关键属性进行子节点合并
	 *
	 * @param applicationNode
	 * @param componentNode
	 * @param keyPropertyName
	 * @return
	 */
	public static List<XmlNode> combineSubList(XmlNode applicationNode, XmlNode componentNode, String nodeName,
											   String keyPropertyName) {
		checkNodeName(applicationNode, componentNode);
		List<XmlNode> result = new ArrayList<XmlNode>();
		if (applicationNode == null && componentNode == null) {
			return result;
		}
		List<XmlNode> applicationNodeList = getNodeList(applicationNode, nodeName);
		List<XmlNode> componentNodeList = getNodeList(componentNode, nodeName);
		if (componentNodeList.isEmpty()) {// 如果组件配置为空
			result.addAll(applicationNodeList);
			return result;
		}
		if (applicationNodeList.isEmpty()) {// 如果应用配置为空
			result.addAll(componentNodeList);
			return result;
		}
		combineSubList(keyPropertyName, result, applicationNodeList, componentNodeList);
		return result;
	}

	private static void checkNodeName(XmlNode applicationNode, XmlNode componentNode) {
		if (applicationNode == null || componentNode == null) {// 如果有一个为空，则返回
			return;
		}
		String applicationNodeName = applicationNode.getNodeName();
		String componentNodeName = componentNode.getNodeName();
		if (applicationNodeName != null && componentNodeName != null
				&& !applicationNodeName.equals(componentNodeName)) {
			throw new RuntimeException(applicationNodeName + "与" + componentNodeName + "两个节点名称不一致！");
		}
	}

	private static void combineSubList(String keyPropertyName, List<XmlNode> result, List<XmlNode> applicationNodeList,
			List<XmlNode> componentNodeList) {
		Map<String, XmlNode> appConfigMap = nodeListToMap(applicationNodeList, keyPropertyName);
		Map<String, XmlNode> compConfigMap = nodeListToMap(componentNodeList, keyPropertyName);
		for (String key : appConfigMap.keySet()) {
			XmlNode compNode = compConfigMap.get(key);
			XmlNode appNode = appConfigMap.get(key);
			if (compNode == null) {
				result.add(appNode);
			} else {// 如果两个都有，则合并之
				result.add(combine(appNode, compNode));
			}
		}
		for (String key : compConfigMap.keySet()) {
			// 判断是否配置了应用级别的信息
			XmlNode appNode = appConfigMap.get(key);
			// 未配置应用级别的信息，使用默认的组件级别信息
			if (appNode == null) {
				result.add(compConfigMap.get(key));
			}
		}
	}

	private static Map<String, XmlNode> nodeListToMap(List<XmlNode> subNodes, String keyPropertyName) {
		Map<String, XmlNode> nodeMap = new HashMap<String, XmlNode>();
		for (XmlNode node : subNodes) {
			String value = node.getAttribute(keyPropertyName);
			nodeMap.put(value, node);
		}
		return nodeMap;
	}

	private static List<XmlNode> getNodeList(XmlNode node, String nodeName) {
		List<XmlNode> nodeList = new ArrayList<XmlNode>();
		if (node != null) {
			nodeList = node.getSubNodes(nodeName);
		}
		return nodeList;
	}

	/**
	 * 合并单个节点
	 *
	 * @param applicationNode
	 * @param componentNode
	 * @return
	 */
	public static XmlNode combineXmlNode(XmlNode applicationNode, XmlNode componentNode) {
		checkNodeName(applicationNode, componentNode);
		if (applicationNode == null && componentNode == null) {
			return null;
		}
		XmlNode result = null;
		if (applicationNode != null && componentNode == null) {
			result = applicationNode;
		} else if (applicationNode == null && componentNode != null) {
			result = componentNode;
		} else {
			result = combine(applicationNode, componentNode);
		}
		return result;
	}

	private static XmlNode combine(XmlNode appNode, XmlNode compNode) {
		XmlNode result = new XmlNode(appNode.getNodeName());
		result.setAttribute(compNode.getAttributes());
		result.setAttribute(appNode.getAttributes());
		if (!CollectionUtils.isEmpty(compNode.getSubNodes())) {
			result.addAll(compNode.getSubNodes());
		}
		if (!CollectionUtils.isEmpty(appNode.getSubNodes())) {
			result.addAll(appNode.getSubNodes());
		}
		return result;
	}

	/**
	 * 简单合并两个XmlNode
	 *
	 * @param applicationNode
	 * @param componentNode
	 * @return
	 */
	public static List<XmlNode> combineSubList(XmlNode applicationNode, XmlNode componentNode) {

		checkNodeName(applicationNode, componentNode);
		List<XmlNode> result = new ArrayList<XmlNode>();
		if (applicationNode == null && componentNode == null) {
			return result;
		}
		if (componentNode != null && componentNode.getSubNodes() != null) {
			result.addAll(componentNode.getSubNodes());
		}
		if (applicationNode != null && applicationNode.getSubNodes() != null) {
			result.addAll(applicationNode.getSubNodes());
		}
		return result;
	}

	/**
	 * 简单合并
	 *
	 * @param nodeName
	 * @param applicationNode
	 * @param componentNode
	 * @return
	 */
	public static List<XmlNode> combineSubList(String nodeName, XmlNode applicationNode, XmlNode componentNode) {
		checkNodeName(applicationNode, componentNode);
		List<XmlNode> result = new ArrayList<XmlNode>();
		if (applicationNode == null && componentNode == null) {
			return result;
		}
		if (componentNode != null && componentNode.getSubNodes(nodeName) != null) {
			result.addAll(componentNode.getSubNodes(nodeName));
		}
		if (applicationNode != null && applicationNode.getSubNodes(nodeName) != null) {
			result.addAll(applicationNode.getSubNodes(nodeName));
		}
		return result;
	}

	/**
	 * 简单合并
	 *
	 * @param nodeName
	 * @param applicationNode
	 * @param componentNode
	 * @return
	 */
	public static List<XmlNode> combineFindNodeList(String nodeName, XmlNode applicationNode, XmlNode componentNode) {
		checkNodeName(applicationNode, componentNode);
		List<XmlNode> result = new ArrayList<XmlNode>();
		if (applicationNode == null && componentNode == null) {
			return result;
		}
		if (componentNode != null) {
			NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(componentNode);
			List<XmlNode> nodes = nameFilter.findNodeList(nodeName);
			if (nodes != null) {
				result.addAll(nodes);
			}
		}
		if (applicationNode != null) {
			NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(applicationNode);
			List<XmlNode> nodes = nameFilter.findNodeList(nodeName);
			if (nodes != null) {
				result.addAll(nodes);
			}
		}
		return result;
	}

	/**
	 * 将文件内容读取为xmlnode对象
	 * 
	 * @param fileObject
	 * @return
	 * @throws IOException
	 */
	public static XmlNode parseXmlFromFileObject(FileObject fileObject) throws IOException {
		String config = StreamUtil.readText(fileObject.getInputStream(), "UTF-8", true);
		return new XmlStringParser().parse(config).getRoot();
	}
	
	/**
     * 获取属性值，应用配置的优先级更高
     *
     * @param applicationNode
     * @param componentNode
     * @param attributeName
     * @return
     */
    public static String getPropertyName(XmlNode applicationNode, XmlNode componentNode, String attributeName) {
        String value = null;
        checkNodeName(applicationNode, componentNode);
        if (applicationNode != null) {
            value = applicationNode.getAttribute(attributeName);
        }
        if (value == null && componentNode != null) {
            value = componentNode.getAttribute(attributeName);
        }
        return value;
    }

    /**
     * 获取属性值，应用配置的优先级更高。<br>
     * 如果读取的结果为Null或为""，则返回默认值
     *
     * @param applicationNode
     * @param componentNode
     * @param attributeName
     * @param defaultValue
     * @return
     */
    public static String getPropertyName(XmlNode applicationNode, XmlNode componentNode, String attributeName,
                                         String defaultValue) {
        String value = getPropertyName(applicationNode, componentNode, attributeName);
        if (value == null || value.trim().length() == 0) {
            value = defaultValue;
        }
        return value;
    }
}
