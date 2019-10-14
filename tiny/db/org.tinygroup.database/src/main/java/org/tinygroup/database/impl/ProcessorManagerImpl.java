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
package org.tinygroup.database.impl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.database.ProcessorManager;
import org.tinygroup.database.config.processor.Processor;
import org.tinygroup.database.config.processor.Processors;
import org.tinygroup.database.initdata.InitDataSqlProcessor;
import org.tinygroup.database.initdata.impl.*;
import org.tinygroup.database.procedure.impl.*;
import org.tinygroup.database.sequence.impl.Db2SequenceSqlProcessor;
import org.tinygroup.database.sequence.impl.OracleSequenceSqlProcessor;
import org.tinygroup.database.table.impl.*;
import org.tinygroup.database.trigger.impl.MysqlTriggerSqlProcessor;
import org.tinygroup.database.trigger.impl.OracleTriggerSqlProcessor;
import org.tinygroup.database.trigger.impl.SqlServerTriggerSqlProcessor;
import org.tinygroup.database.view.impl.*;

import java.util.HashMap;
import java.util.Map;

public class ProcessorManagerImpl implements ProcessorManager {
    private static ProcessorManager processorManager = new ProcessorManagerImpl();
    private Map<String, Map<String, Object>> processorsMap = new HashMap<String, Map<String, Object>>();

    public static ProcessorManager getProcessorManager() {
        managerInit((ProcessorManagerImpl) processorManager);
        return processorManager;
    }

    private static void managerInit(ProcessorManagerImpl processorManager) {
        InitDataSqlProcessor initDataSqlProcessor = new InitDataSqlProcessorImpl();
        InitDataSqlProcessor oracleInitDataSqlProcessor = new OracleInitDataSqlProcessorImpl();
        InitDataSqlProcessor mysqlInitDataSqlProcessor = new MySqlInitDataSqlProcessorImpl();
        InitDataSqlProcessor h2InitDataSqlProcessor = new H2InitDataSqlProcessorImpl();
        InitDataSqlProcessor db2InitDataSqlProcessor = new Db2InitDataSqlProcessorImpl();
        InitDataSqlProcessor derbyInitDataSqlProcessor = new DerbyInitDataSqlProcessorImpl();
        InitDataSqlProcessor sqlserverInitDataSqlProcessor = new SqlserverInitDataSqlProcessorImpl();
        InitDataSqlProcessorImpl sybaseInitDataSqlProcessor = new SybaseInitDataSqlProcessorImpl();
        Map<String, Map<String, Object>> processorsMap = processorManager.processorsMap;

        Map<String, Object> db2ProcessMap = new HashMap<String, Object>();
        db2ProcessMap.put("table", Db2SqlProcessorImpl.getTableSqlProcessor());
        db2ProcessMap.put("view", new Db2ViewSqlProcessorImpl());
        db2ProcessMap.put("sequence", new Db2SequenceSqlProcessor());
        db2ProcessMap.put("procedure", new Db2ProcedureSqlProcessorImpl());
        db2ProcessMap.put("initData", db2InitDataSqlProcessor);
        processorsMap.put("db2", db2ProcessMap);

        Map<String, Object> informixProcessMap = new HashMap<String, Object>();
        informixProcessMap.put("table", InformixSqlProcessorImpl.getTableSqlProcessor());
        informixProcessMap.put("initData", initDataSqlProcessor);
        informixProcessMap.put("view", new InformixViewSqlProcessorImpl());
//        informixProcessMap.put("sequence", new InformixSequenceSqlProcessor());
        informixProcessMap.put("procedure", new InformixProcedureSqlProcessorImpl());
        processorsMap.put("informix", informixProcessMap);

        Map<String, Object> sybaseProcessMap = new HashMap<String, Object>();
        sybaseProcessMap.put("table", SybaseSqlProcessorImpl.getTableSqlProcessor());
        sybaseProcessMap.put("initData", sybaseInitDataSqlProcessor);
        sybaseProcessMap.put("view", new SybaseViewSqlProcessorImpl());
        sybaseProcessMap.put("procedure", new SybaseSqlProcessorImpl());
        processorsMap.put("sybase", sybaseProcessMap);

        Map<String, Object> derbyProcessMap = new HashMap<String, Object>();
        derbyProcessMap.put("table",
                DerbySqlProcessorImpl.getTableSqlProcessor());
        derbyProcessMap.put("initData", derbyInitDataSqlProcessor);
        derbyProcessMap.put("view", new DerbyViewSqlProcessorImpl());
        derbyProcessMap.put("procedure", new DerbyProcedureSqlProcessorImpl());

        processorsMap.put("derby", derbyProcessMap);

        Map<String, Object> h2ProcessMap = new HashMap<String, Object>();
        h2ProcessMap.put("table", H2SqlProcessorImpl.getTableSqlProcessor());
        h2ProcessMap.put("initData", h2InitDataSqlProcessor);
        h2ProcessMap.put("view", new H2ViewSqlProcessorImpl());
        h2ProcessMap.put("procedure", new H2ProcedureSqlProcessorImpl());
        processorsMap.put("h2", h2ProcessMap);

        Map<String, Object> mysqlProcessMap = new HashMap<String, Object>();
        mysqlProcessMap.put("table",
                MysqlSqlProcessorImpl.getTableSqlProcessor());
        mysqlProcessMap.put("initData", mysqlInitDataSqlProcessor);
        mysqlProcessMap.put("view", new MysqlViewSqlProcessorImpl());
        mysqlProcessMap.put("procedure", new MySqlProcedureSqlProcessorImpl());
        mysqlProcessMap.put("trigger", new MysqlTriggerSqlProcessor());
        processorsMap.put("mysql", mysqlProcessMap);

        Map<String, Object> oracleProcessMap = new HashMap<String, Object>();
        oracleProcessMap.put("table",
                OracleSqlProcessorImpl.getTableSqlProcessor());
        oracleProcessMap.put("initData", oracleInitDataSqlProcessor);
        oracleProcessMap.put("view", new OracleViewSqlProcessorImpl());
        oracleProcessMap
                .put("procedure", new OracleProcedureSqlProcessorImpl());
        oracleProcessMap.put("sequence", new OracleSequenceSqlProcessor());
        oracleProcessMap.put("trigger", new OracleTriggerSqlProcessor());
        processorsMap.put("oracle", oracleProcessMap);

        Map<String, Object> sqlserverSqlProcessMap = new HashMap<String, Object>();
        sqlserverSqlProcessMap.put("table",
                SqlserverSqlProcessorImpl.getTableSqlProcessor());
        sqlserverSqlProcessMap.put("initData", sqlserverInitDataSqlProcessor);
        sqlserverSqlProcessMap.put("view", new SqlserverViewSqlProcessorImpl());
        sqlserverSqlProcessMap.put("trigger",
                new SqlServerTriggerSqlProcessor());
        sqlserverSqlProcessMap.put("procedure", new SqlServerProcedureSqlProcessorImpl());
        processorsMap.put("sqlserver", sqlserverSqlProcessMap);

    }

    @Deprecated
    public void addPocessors(Processors processors) {
        addProcessors(processors);
    }

    @Deprecated
    public void removePocessors(Processors processors) {
        removeProcessors(processors);
    }

    public void addProcessors(Processors processors) {
        String language = processors.getLanguage();

        if (!processorsMap.containsKey(language)) {
            processorsMap.put(language, new HashMap<String, Object>());
        }
        Map<String, Object> map = processorsMap.get(language);
        for (Processor processor : processors.getList()) {
            String processorName = processor.getName();
            String bean = processor.getBean();
            map.put(processorName,
                    BeanContainerFactory.getBeanContainer(
                            this.getClass().getClassLoader()).getBean(bean));
        }
    }

    public void removeProcessors(Processors processors) {
        String language = processors.getLanguage();
        Map<String, Object> map = processorsMap.get(language);
        if (!CollectionUtil.isEmpty(map)) {
            for (Processor processor : processors.getList()) {
                String processorName = processor.getName();
                map.remove(processorName);
            }
        }

    }

    public Object getProcessor(String language, String name) {
        if (processorsMap.containsKey(language)) {
            return processorsMap.get(language).get(name);
        }
        return null;
    }

}
