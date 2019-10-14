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
package org.tinygroup.fulltext;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.fulltext.document.HighlightDocument;
import org.tinygroup.fulltext.exception.FullTextException;
import org.tinygroup.fulltext.impl.AbstractFullText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全文检索全局辅助类
 *
 * @author yancheng11334
 */
public class FullTextHelper {

    private static volatile String _id = "_id";
    private static volatile String _type = "_type";
    private static volatile String _title = "_title";
    private static volatile String _abstract = "_abstract";

    private static Map<String, FullText> dynmicMaps = new HashMap<String, FullText>();

    /**
     * 得到默认的ID存储名
     *
     * @return
     */
    public static String getStoreId() {
        return _id;
    }

    /**
     * 设置默认的ID存储名
     *
     * @param id
     */
    public static void setStoreId(String id) {
        _id = id;
    }

    /**
     * 得到默认的type存储名
     *
     * @return
     */
    public static String getStoreType() {
        return _type;
    }

    /**
     * 设置默认的type存储名
     *
     * @param type
     */
    public static void setStoreType(String type) {
        _type = type;
    }

    /**
     * 得到默认的title存储名
     *
     * @return
     */
    public static String getStoreTitle() {
        return _title;
    }

    /**
     * 设置默认的title存储名
     *
     * @param type
     */
    public static void setStoreTitle(String title) {
        _title = title;
    }

    /**
     * 得到默认的abstract存储名
     *
     * @return
     */
    public static String getStoreAbstract() {
        return _abstract;
    }

    /**
     * 设置默认的type存储名
     *
     * @param type
     */
    public static void setStoreAbstract(String Abstract) {
        _abstract = Abstract;
    }

    /**
     * 得到FullText的实例
     *
     * @return
     */
    public static FullText getFullText() {
        String bean = ConfigurationUtil.getConfigurationManager()
                .getConfiguration(FullText.FULLTEXT_BEAN_NAME);
        if (StringUtil.isEmpty(bean)) {
            throw new FullTextException(String.format("FullText的bean配置不存在，请检查%s全局配置参数!", FullText.FULLTEXT_BEAN_NAME));
        }
        return (FullText) BeanContainerFactory.getBeanContainer(FullTextHelper.class.getClassLoader()).getBean(bean);
    }

    /**
     * 获取默认模板的不同用户的实例
     *
     * @param userId
     * @param configId
     * @return
     */
    public static synchronized FullText getFullText(String userId) {
        return getFullText(userId, null);
    }

    /**
     * 获取指定模板的不同用户的实例
     *
     * @param userId
     * @param configId
     * @return
     */
    public static synchronized FullText getFullText(String userId, String configId) {
        FullText fulltext = dynmicMaps.get(userId);
        if (fulltext == null) {
            fulltext = createFullText(userId, configId);
            dynmicMaps.put(userId, fulltext);
        }
        return fulltext;
    }

    private static FullText createFullText(String userId, String configId) {
        String bean = ConfigurationUtil.getConfigurationManager()
                .getConfiguration(FullText.FULLTEXT_DYNAMIC_BEAN);
        if (StringUtil.isEmpty(bean)) {
            throw new FullTextException(String.format("FullText的bean配置不存在，请检查%s全局配置参数!", FullText.FULLTEXT_DYNAMIC_BEAN));
        }
        AbstractFullText fulltext = (AbstractFullText) BeanContainerFactory.getBeanContainer(FullTextHelper.class.getClassLoader()).getBean(bean);
        //设置底层操作接口,动态注入userId,注意IndexOperator配置本身需要多例
        IndexOperator indexOperator = fulltext.getIndexOperator();
        if (indexOperator == null) {
            throw new FullTextException(String.format("创建动态FullText失败:底层操作接口IndexOperator为空,请检查%s对应的bean配置!", FullText.FULLTEXT_DYNAMIC_BEAN));
        }
        if (indexOperator instanceof UserIndexOperator) {
            UserIndexOperator userIndexOperator = (UserIndexOperator) indexOperator;
            userIndexOperator.setUserId(userId);
            userIndexOperator.setConfigId(configId);
            userIndexOperator.reset();
        } else {
            throw new FullTextException(String.format("创建动态FullText失败:底层操作接口%s不支持UserIndexOperator,请检查%s对应的bean配置!", indexOperator.getClass().getName(), FullText.FULLTEXT_DYNAMIC_BEAN));
        }
        return fulltext;
    }

    /**
     * 高亮结果
     *
     * @param pager
     * @param arguments
     * @return
     */
    public static Pager<HighlightDocument> highlight(Pager<Document> pager, Object... arguments) {
        if (pager == null) {
            return null;
        }
        List<HighlightDocument> list = new ArrayList<HighlightDocument>();
        if (pager.getRecords() != null) {
            for (Document doc : pager.getRecords()) {
                list.add(new HighlightDocument(doc, arguments));
            }
        }
        return new Pager<HighlightDocument>(pager.getTotalCount(), pager.getStart(), pager.getLimit(), list);
    }

}
