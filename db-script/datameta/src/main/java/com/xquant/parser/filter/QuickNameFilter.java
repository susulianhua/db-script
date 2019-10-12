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
package com.xquant.parser.filter;



import com.xquant.binarytree.AVLTree;
import com.xquant.binarytree.impl.AVLTreeImpl;
import com.xquant.parser.Node;
import com.xquant.parser.node.Processor;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class QuickNameFilter<T extends Node<T>> extends AbstractFilterImpl<T> {
    private AVLTree<NodeList> tree = null;

    public QuickNameFilter() {
    }

    public QuickNameFilter(T node) {
        init(node);
    }

    public void init(T node) {
        super.init(node);
        tree = new AVLTreeImpl<NodeList>();
        node.foreach(new NodeAdd());

    }

    public List<T> findNodeList(String nodeName) {
        NodeList nv = new NodeList(nodeName);
        List<T> result;
        nv = tree.contains(nv);
        if (nv != null) {
            result = filteNode(nv.getNodeList());
        } else {
            result = new ArrayList<T>();
        }
        return result;
    }

    class NodeAdd implements Processor<T> {
        public void process(T node) {
            if (node.getNodeName() != null) {
                NodeList nv = new NodeList(node.getNodeName());
                NodeList nvIntree = tree.contains(nv);
                if (nvIntree != null) {
                    nvIntree.getNodeList().add(node);
                } else {
                    nv.getNodeList().add(node);
                    tree.add(nv);
                }
            }
        }
    }


}
