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
package org.tinygroup.event.central;

import org.tinygroup.event.ServiceInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务节点
 *
 * @author luoguo
 */
public final class Node implements Comparable<Node>, Serializable {
    public static final String CEP_NODE = "cepnode";
    public static final String CENTRAL_NODE = "centralnode";
    /**
     *
     */
    private static final long serialVersionUID = -830350685731524644L;
    // IP地址
    private String ip;
    // 服务端口
    private String port;
    /**
     * 节点名
     */
    private String nodeName;
    /**
     * 分配权重，当一个服务被发现在若干个节点下都提供时，具体调用时，根据权重进行随机分配
     */
    private int weight;
    private String type;
    private List<ServiceInfo> serviceInfos;

    public Node() {
    }

    public Node(String ip, String port, String nodeName, int weight) {
        this.port = port;
        this.ip = ip;
        this.nodeName = nodeName;
        this.weight = weight;
    }

    public Node(String nodeName, int weight) {
        this.nodeName = nodeName;
        this.weight = weight;
    }

    public static boolean checkEquals(String firstNodeString,
                                      String secondNodeString) {
        String[] firstNode = firstNodeString.split(":");
        String[] secondNode = secondNodeString.split(":");
        // 两个长度相等，则认为两个格式相同
        // 可能是ip:port:nodeName格式
        // 也可能是nodeName
        if (firstNode.length == secondNode.length) {
            return firstNodeString.equals(secondNodeString);
        } else if (firstNode.length >= 3 && secondNode.length == 1) {
            return compareNodeString(firstNodeString, firstNode, secondNodeString);
        } else if (firstNode.length == 1 && secondNode.length >= 3) {
            return compareNodeString(secondNodeString, secondNode, firstNodeString);
        } else {
            return false;
        }

    }

    private static boolean compareNodeString(String firstNodeString,
                                             String[] firstNode, String secondNodeString) {
        if (firstNode.length == 3) {
            return firstNode[2].equals(secondNodeString);
        } else {
            String ip = firstNode[0];
            String port = firstNode[1];
            int ipportLength = ip.length() + port.length();
            String computeNodeName = firstNodeString.substring(ipportLength);
            return computeNodeName.equals(secondNodeString);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ServiceInfo> getServiceInfos() {
        if (serviceInfos == null) {
            serviceInfos = new ArrayList<ServiceInfo>();
        }
        return serviceInfos;
    }

    public void setServiceInfos(List<ServiceInfo> serviceInfos) {
        this.serviceInfos = serviceInfos;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * 比较节点名称
     */
    public int compareTo(Node node) {
        return nodeName.compareTo(node.nodeName);
    }

    /**
     * 比较节点名称
     */
    public boolean equals(Object object) {
        if (object instanceof Node) {
            Node n = (Node) object;
            return this.toString().equals(n.toString());
        }
        return false;
    }

    public int hashCode() {
        return nodeName.hashCode();
    }

    public String toString() {
        return String.format("%s:%s:%s", ip, port, nodeName);
    }

}
