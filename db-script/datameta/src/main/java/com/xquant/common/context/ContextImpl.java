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
package com.xquant.common.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContextImpl extends BaseContextImpl implements Context,
        Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6522200746253638309L;

    private Map<String, Context> subContextMap = new ConcurrentHashMap<String, Context>();
    private Context parent = null;

    public ContextImpl() {

    }

    public ContextImpl(Map map) {
        super.setItemMap(map);
    }

    public Context getParent() {
        return parent;
    }

    public void setParent(Context parent) {
        this.parent = parent;
    }

    public Context createSubContext(String contextName) {
        Context context = new ContextImpl();
        putSubContextAndSetParent(contextName, context);
        return context;
    }

    public Context putSubContext(String contextName, Context context) {
        putSubContextAndSetParent(contextName, context);
        return context;
    }

    /**
     * 设置儿子环境，但儿子的不设置当前为父亲
     */
    public void putContext(String contextName, Context context) {
        subContextMap.put(contextName, context);
    }

    private void putSubContextAndSetParent(String contextName, Context context) {
        context.setParent(this);
        subContextMap.put(contextName, context);
    }

    public Context removeSubContext(String contextName) {
        return subContextMap.remove(contextName);
    }

    public Context getSubContext(String contextName) {
        return subContextMap.get(contextName);
    }

    public void clearSubContext() {
        subContextMap.clear();

    }

    public void clear() {
        super.clear();
        clearSubContext();
    }

    public Map<String, Context> getSubContextMap() {
        return subContextMap;
    }

    public <T> T remove(String contextName, String name) {
        Context subContext = subContextMap.get(contextName);
        if (subContext != null) {
            return (T) subContext.remove(name);
        }
        return null;
    }

    public <T> T getInSubContext(String contextName, String name) {
        Context subContext = subContextMap.get(contextName);
        if (subContext != null) {
            return (T) subContext.get(name);
        }
        return null;
    }

    public <T> T put(String contextName, String name, T object) {
        Context subContext = subContextMap.get(contextName);
        if (subContext == null) {
            subContext = createSubContext(contextName);
        }
        subContext.put(name, object);
        return object;
    }

    /**
     * 改写get方法，使得可以从父环境中查找，同时，也可以从子环境中查找 先找自己，再找子，再找父
     */
    public <T> T get(String name) {
        Map<Context, String> nodeMap = new HashMap<Context, String>();
        if (nodeMap.get(this) == null) {
            return (T) findNodeMap(name, this, nodeMap);
        }
        return null;
    }
    public Map<String, Object> getTotalItemMap() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        for (Context subContext : subContextMap.values()) {
//            map.putAll(subContext.getItemMap());
//        }
//        map.putAll(getItemMap());
        List<Context> list = new ArrayList<Context>();
        getTotalContext(this,list);
        Map<String, Object> map = new HashMap<String,Object>();
        for(int i=list.size()-1;i>=0;i--){
            map.putAll(list.get(i).getItemMap());
        }
        return map;
    }

    protected  void getTotalContext(Context contextNode, List<Context> list){
        if(!list.contains(contextNode)){
            list.add(contextNode);
        }else{
            return;
        }
        for (Context subContext : contextNode.getSubContextMap().values()) {
            getTotalContext(subContext,list);
        }
        if(contextNode.getParent()!=null){
            getTotalContext(contextNode.getParent(),list);
        }
    }

    protected <T> T findNodeMap(String name, Context contextNode,
                                Map<Context, String> nodeMap) {

        // 如果当前不存在，则查找父亲中有没有
        // 如果已经存在，则返回之
        if (contextNode.getItemMap().containsKey(name)) {
            Object object = contextNode.getItemMap().get(name);
            if (object == null) {
                return null;
            } else {
                return (T) object;
            }
        }

        T result = (T) contextNode.getItemMap().get(name);
        if (result != null) {
            return result;
        } else {
            nodeMap.put(contextNode, "");
        }
        if (!contextNode.getSubContextMap().isEmpty()) {
            for (Context context : contextNode.getSubContextMap().values()) {
                if (nodeMap.get(context) == null) {
                    T subResult = findNodeMap(name, context, nodeMap);
                    if (subResult != null) {
                        return subResult;
                    }
                }
            }
        }
        Context theParent = contextNode.getParent();
        if (theParent != null && nodeMap.get(theParent) == null) {
            return (T) findNodeMap(name, theParent, nodeMap);
        }
        return null;
    }

    protected boolean existNodeMap(String name, Context contextNode,
                                   Map<Context, String> nodeMap) {

        // 如果当前不存在，则查找父亲中有没有
        // 如果已经存在，则返回之
        if (contextNode.getItemMap().containsKey(name)) {
            return true;
        } else {
            nodeMap.put(contextNode, "");
        }

        if (!contextNode.getSubContextMap().isEmpty()) {
            for (Context context : contextNode.getSubContextMap().values()) {
                if (nodeMap.get(context) == null) {
                    boolean exist = existNodeMap(name, context, nodeMap);
                    if (exist) {
                        return true;
                    }
                }
            }
        }
        Context theParent = contextNode.getParent();
        if (theParent != null && nodeMap.get(theParent) == null) {
            return existNodeMap(name, theParent, nodeMap);
        }
        return false;
    }

    protected Context containNodeMap(String name, Context contextNode,
                                     Map<Context, String> nodeMap) {

        // 如果当前不存在，则查找父亲中有没有
        // 如果已经存在，则返回之
        if (contextNode.getItemMap().containsKey(name)) {
            return contextNode;
        } else {
            nodeMap.put(contextNode, "");
        }

        if (!contextNode.getSubContextMap().isEmpty()) {
            for (Context context : contextNode.getSubContextMap().values()) {
                if (nodeMap.get(context) == null) {
                    Context con = containNodeMap(name, context, nodeMap);
                    if (con != null) {
                        return con;
                    }
                }
            }
        }
        Context theParent = contextNode.getParent();
        if (theParent != null && nodeMap.get(theParent) == null) {
            return containNodeMap(name, theParent, nodeMap);
        }
        return null;
    }

    public boolean renameKey(String key, String newKey) {
        Map<Context, String> nodeMap = new HashMap<Context, String>();
        if (nodeMap.get(this) == null) {
            return renameKeyNodeMap(key, newKey, this, nodeMap);
        }
        return false;
    }

    protected boolean renameKeyNodeMap(String key, String newKey,
                                       Context contextNode, Map<Context, String> nodeMap) {
        boolean renamed = renameKeyOfSuper(key, newKey, contextNode);
        Context theParent = contextNode.getParent();
        if (renamed) {
            return true;
        } else {
            nodeMap.put(contextNode, "");
        }
        if (!contextNode.getSubContextMap().isEmpty()) {
            for (Context context : contextNode.getSubContextMap().values()) {
                if (nodeMap.get(context) == null) {
                    renamed = renameKeyNodeMap(key, newKey, context, nodeMap);
                    if (renamed) {
                        return true;
                    }
                }
            }
        }
        if (theParent != null && nodeMap.get(theParent) == null) {
            renamed = renameKeyNodeMap(key, newKey, theParent, nodeMap);
            if (renamed) {
                return true;
            }
        }
        return false;
    }

    private boolean renameKeyOfSuper(String key, String newKey, Context context) {
        Map<String, Object> itemMap = context.getItemMap();
        if (itemMap.containsKey(key)) {
            itemMap.put(newKey, itemMap.get(key));
            itemMap.remove(key);
            return true;
        }
        return false;
    }

    public boolean exist(String name) {
        Map<Context, String> nodeMap = new HashMap<Context, String>();
        return existNodeMap(name, this, nodeMap);

    }

    public Context contain(String name) {
        Map<Context, String> nodeMap = new HashMap<Context, String>();
        return containNodeMap(name, this, nodeMap);

    }

    public Map<String, Object> getItemMap() {
        return super.getItemMap();
    }



}
