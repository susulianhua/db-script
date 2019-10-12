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
package com.xquant.metadata.util;


import com.xquant.xmlparser.node.XmlNode;

/**
 * Created by wangwy11342 on 2016/8/24.
 */
public class ConfigUtil {
    private static boolean isCheckStrict = true;
    private static boolean initDataDel = false;
    private static boolean isCheckModified = true;
    //数据库中的字节和char字符的倍数关系
    private static Integer char2ByteSize = 3;

    //数据库中的字节和varchar字符的倍数关系
    private static Integer varchar2ByteSize = 3;

    //是否使用数据库触发器,用于oracle等支持触发器的数据库.选择自增的时候生成触发器
    private static boolean useDbTrigger = true;

    private static boolean tableRemarksConfigured = false;

    private static String tableNamePrefix;

    public static void setIsCheckStrict(boolean isCheckStrict) {
        ConfigUtil.isCheckStrict = isCheckStrict;
    }

    public static boolean isCheckStrict() {
        return isCheckStrict;
    }

    public static boolean isInitDataDel() {
        return initDataDel;
    }

    public static void setInitDataDel(boolean isInitDataDel) {
        initDataDel = isInitDataDel;
    }

    public static boolean isCheckModified() {
        return isCheckModified;
    }

    public static void setIsCheckModified(boolean isCheckModified) {
        ConfigUtil.isCheckModified = isCheckModified;
    }

    public static Integer getChar2ByteSize() {
        return char2ByteSize;
    }


    //可通过配置获取
    public static void setChar2ByteSize(Integer char2ByteSize) {
        ConfigUtil.char2ByteSize = char2ByteSize;
    }

    public static Integer getVarchar2ByteSize() {
        return varchar2ByteSize;
    }

    //可通过配置获取
    public static void setVarchar2ByteSize(Integer varchar2ByteSize) {
        ConfigUtil.varchar2ByteSize = varchar2ByteSize;
    }

    public static String getPropertyValue(XmlNode applicationConfig, String propertyNodeName, String proertyKey) {
        XmlNode xmlNode = applicationConfig.getSubNode(propertyNodeName);
        if (xmlNode != null) {
            return xmlNode.getAttribute(proertyKey);
        }
        return null;
    }

    /**
     * 设置表名前缀
      * @param tableNamePrefix
     */
    public static void setTableNamePrefix(String tableNamePrefix){
        ConfigUtil.tableNamePrefix = tableNamePrefix;
    }

    public static String getTableNamePrefix() {
        return tableNamePrefix;
    }

    public static boolean isUseDbTrigger() {
        return useDbTrigger;
    }

    public static void setUseDbTrigger(boolean useDbTrigger) {
        ConfigUtil.useDbTrigger = useDbTrigger;
    }

    public static boolean isTableRemarksConfigured() {
        return tableRemarksConfigured;
    }

    public static void setTableRemarksConfigured(boolean tableRemarksConfigured) {
        ConfigUtil.tableRemarksConfigured = tableRemarksConfigured;
    }
}
