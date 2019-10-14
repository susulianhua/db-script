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
package org.tinygroup.metadata.checkupdate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwy11342 on 2016/10/8.
 * 元数据文件信息管理
 */
public class MetaDataFileManager {
    public static final String TABLE_CREATE_SQL
            = "CREATE TABLE TINY_METADATA_RESOURCE (TYPE VARCHAR(20),RESOURCE_ID VARCHAR(32),MODIFIED_TIME  VARCHAR(20)) ";
    private static MetaDataFileManager metaDataFileManager = new MetaDataFileManager();
    //metaDataInfo表中的上下文
    private MetaDataInfoContext metaDataInfoContext = MetaDataInfoContext.getInstance();

    //元数据表插入语句
    private List<String> insertSqls = new ArrayList<String>();

    //元数据表更新语句
    private List<String> updateSqls = new ArrayList<String>();

    private MetaDataFileManager() {

    }

    public static MetaDataFileManager getInstance() {
        return metaDataFileManager;
    }


    public List<String> getInsertSqls() {
        return insertSqls;
    }

    public List<String> getUpdateSqls() {
        return updateSqls;
    }

    public void put(MetaDataFileInfo metaDataFileInfo) {
        metaDataInfoContext.put(metaDataFileInfo);
    }

    public List<MetaDataFileInfo> getMetaDataFileInfoList() {
        return metaDataInfoContext.getMetaDataFileInfoList();
    }


    /**
     * 创建info表的处理语句(insert/update)
     *
     * @param metaDataFileInfo 各个文件中的info
     *                         return 是否发生变更
     */
    public boolean createSqlAndCheckUpdate(MetaDataFileInfo metaDataFileInfo) {
        String key = metaDataFileInfo.getResourceId() + metaDataFileInfo.getType();
        //key中不包含表示新增
        if (!metaDataInfoContext.getKeys().contains(key)) {
            String formatSql = "insert into TINY_METADATA_RESOURCE(TYPE,RESOURCE_ID,MODIFIED_TIME) values('%s','%s','%s')";
            String sql = String.format(formatSql, metaDataFileInfo.getType(), metaDataFileInfo.getResourceId()
                    , metaDataFileInfo.getModifiedTime());
            //防止重复
            if (!insertSqls.contains(sql)) {
                insertSqls.add(sql);
            }
            return true;
        }
        //key包含但是value不一致表示update
        if (!getMetaDataFileInfoList().contains(metaDataFileInfo)) {
            String formatSql =
                    "update TINY_METADATA_RESOURCE set MODIFIED_TIME='%s' where TYPE='%s' and RESOURCE_ID='%s'";
            String sql = String.format(formatSql, metaDataFileInfo.getModifiedTime(),
                    metaDataFileInfo.getType(), metaDataFileInfo.getResourceId());
            //防止重复
            if (!updateSqls.contains(sql)) {
                updateSqls.add(sql);
            }
            return true;
        }
        return false;
    }


}
