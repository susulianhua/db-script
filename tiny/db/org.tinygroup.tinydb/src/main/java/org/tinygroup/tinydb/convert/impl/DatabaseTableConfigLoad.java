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
package org.tinygroup.tinydb.convert.impl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.TableField;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.metadata.config.stddatatype.DialectType;
import org.tinygroup.metadata.config.stddatatype.StandardType;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.config.ColumnConfiguration;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.exception.TinyDbException;

import java.util.List;

/**
 * tiny database描述的表结构配置
 *
 * @author renhui
 */
public class DatabaseTableConfigLoad extends AbstractTableConfigLoad {

    private static final String DECIMAL_DIGITS_HOLDER = "scale";

    private static final String COLUMN_SIZE_HOLDER = "length,precision";

    private String database;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    protected void realLoadTable() throws TinyDbException {
        TableProcessor processor = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                TableProcessor.BEAN_NAME);
        List<Table> tables = processor.getTables();
        if (!CollectionUtil.isEmpty(tables)) {
            for (Table table : tables) {
                logger.logMessage(LogLevel.DEBUG, "开始转化表对象:{}", table.getName());
                TableConfiguration configuration = new TableConfiguration();
                String tableName = table.getNameWithOutSchema();
                String schema = getSchema(table.getSchema());
                if (existsTable(tableName, schema)) {
                    logger.logMessage(LogLevel.WARN, "表格:{0}已存在，无需重新加载",
                            tableName);
                    continue;
                }
                configuration.setName(tableName);
                configuration.setSchema(schema);
                List<TableField> fields = table.getFieldList();
                if (!CollectionUtil.isEmpty(fields)) {
                    for (TableField tableField : fields) {
                        tableFieldConvert(configuration, tableField);
                    }
                    addTableConfiguration(configuration);
                }
                logger.logMessage(LogLevel.DEBUG, "转化表对象:{}结束", table.getName());
            }
        }

    }

    protected void realConvert(BeanOperatorManager manager) throws TinyDbException {

    }

    private void tableFieldConvert(TableConfiguration configuration,
                                   TableField tableField) {
        ColumnConfiguration column = new ColumnConfiguration();
        String standardFieldId = tableField.getStandardFieldId();
        StandardField standardField = MetadataUtil.getStandardField(
                standardFieldId, this.getClass().getClassLoader());
        if (standardField == null) {
            logger.logMessage(LogLevel.ERROR, "找不到[{}]对应的标准字段信息",
                    standardFieldId);
            return;
        }
        StandardType standardType = MetadataUtil.getStandardType(standardFieldId, this.getClass().getClassLoader());
        DialectType dialectType = MetadataUtil.getDialectType(standardFieldId,
                getDatabase(), this.getClass().getClassLoader());
        column.setColumnName(standardField.getName());
        column.setAllowNull(Boolean.toString(!tableField.getNotNull()));
        column.setColumnSize(MetadataUtil.getPlaceholderValue(standardFieldId,
                COLUMN_SIZE_HOLDER, this.getClass().getClassLoader()));
        column.setDataType(standardType.getDataType());
        column.setPrimaryKey(tableField.getPrimary());
        column.setDecimalDigits(MetadataUtil.getPlaceholderValue(
                standardFieldId, DECIMAL_DIGITS_HOLDER, "0", this.getClass()
                        .getClassLoader()));
        column.setTypeName(dialectType.getType());
        configuration.addColumn(column);
    }


}
