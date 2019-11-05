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
package com.xquant.databasebuilstaller;

import com.xquant.common.BeanContainerFactory;
import com.xquant.common.BeanWrapperHolder;
import com.xquant.common.StreamUtil;
import com.xquant.database.ProcessorManager;
import com.xquant.database.customesql.CustomSqlProcessor;
import com.xquant.database.customesql.impl.CustomSqlProcessorImpl;
import com.xquant.database.fileresolver.*;
import com.xquant.database.impl.ProcessorManagerImpl;
import com.xquant.database.initdata.InitDataProcessor;
import com.xquant.database.initdata.impl.InitDataProcessorImpl;
import com.xquant.database.procedure.ProcedureProcessor;
import com.xquant.database.procedure.impl.ProcedureProcessorImpl;
import com.xquant.database.sequence.SequenceProcessor;
import com.xquant.database.sequence.impl.SequenceProcessorImpl;
import com.xquant.database.table.TableProcessor;
import com.xquant.database.table.impl.TableProcessorImpl;
import com.xquant.database.tablespace.impl.TableSpaceProcessorImpl;
import com.xquant.database.trigger.TriggerProcessor;
import com.xquant.database.trigger.impl.TriggerProcessorImpl;
import com.xquant.database.view.ViewProcessor;
import com.xquant.database.view.impl.ViewProcessorImpl;
import com.xquant.databasebuilstaller.impl.*;
import com.xquant.dialectfunction.impl.DialectFunctionProcessorImpl;
import com.xquant.fileresolver.FileResolver;
import com.xquant.fileresolver.FileResolverFactory;
import com.xquant.fileresolver.util.FileResolverUtil;
import com.xquant.metadata.bizdatatype.impl.BusinessTypeProcessorImpl;
import com.xquant.metadata.constants.impl.ConstantsProcessorImpl;
import com.xquant.metadata.fileresolver.BusinessTypeFileResolver;
import com.xquant.metadata.fileresolver.ConstantFileResolver;
import com.xquant.metadata.fileresolver.StandardFieldFileResolver;
import com.xquant.metadata.fileresolver.StandardTypeFileResolver;
import com.xquant.metadata.stddatatype.impl.StandardTypeProcessorImpl;
import com.xquant.metadata.stdfield.impl.StandardFieldProcessorImpl;
import com.xquant.metadata.util.ConfigUtil;
import com.xquant.parser.filter.PathFilter;
import com.xquant.xmlparser.node.XmlNode;
import com.xquant.xmlparser.parser.XmlStringParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;

import javax.sql.DataSource;
import java.io.*;
import java.util.*;

/**
 * 数据库按照启动类
 *
 * @author renhui
 */
public class DatabaseInstallerStart {

    private static final String CHECK_STRICT_KEY = "checkStrict";
    private static final String LANGUAGE_KEY = "language";
    private static final String INITDATA_ISDELETE_KEY = "initDataConflictDel";
    private static final String DB_CHAR2BYTE_LEN = "dbChar2ByteLength";
    private static final String DB_VARCHAR2BYTE_LEN = "dbVarchchar2ByteLength";
    private static final String TYPE = "type";
    private static final String PROPERTY = "property";
    private static final String TINY_JAR_PATTERN = "org\\.tinygroup\\.(.)*\\.jar";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DatabaseInstallerStart.class);
    private static final String CHECK_MODIFIED = "checkModified";
    private static final String DEFAULT_CONFIG = "application.xml";
    private static final String PROPERTIES_CONFIG = "metadata_tool.properties";
    private static final String DEFAULT_LANGUAGE = "oracle";
    private static final String DB_TYPE_KEY = "type";
    private static final String USE_DB_TRIGGER = "userDbTrigger";
    private static final String TABLE_NAME_PREFIX = "tableNamePrefix";
    private DatabaseInstallerProcessor installer;
    private XmlNode applicationNode;
    private Properties toolProps;

    public DatabaseInstallerStart() {
        Properties properties = parsePropertiesFile();
        Map<String, String> databaseConfig;
        if (properties != null) {
            databaseConfig = resolverDatabaseFromProps();
        } else {
            applicationNode = getRootNode();
            databaseConfig = resolverDatabaseNode();
        }

        installer = initInstaller();
        if (databaseConfig.get(CHECK_STRICT_KEY) != null
                && "false".equalsIgnoreCase(databaseConfig.get(CHECK_STRICT_KEY))) {
            ConfigUtil.setIsCheckStrict(false);
        }
        if (databaseConfig.get(CHECK_MODIFIED) != null
                && "false".equalsIgnoreCase(databaseConfig.get(CHECK_MODIFIED))) {
            ConfigUtil.setIsCheckModified(false);
        }
        installer.setDbLanguage(databaseConfig.get(LANGUAGE_KEY));
        if (databaseConfig.get(INITDATA_ISDELETE_KEY) != null
                && "true".equalsIgnoreCase(databaseConfig.get(INITDATA_ISDELETE_KEY))) {
            ConfigUtil.setInitDataDel(true);
        }
        if (!StringUtils.isEmpty(databaseConfig.get(DB_CHAR2BYTE_LEN))) {
            ConfigUtil.setChar2ByteSize(Integer.valueOf(databaseConfig.get(DB_CHAR2BYTE_LEN)));
        }
        if (!StringUtils.isEmpty(databaseConfig.get(DB_VARCHAR2BYTE_LEN))) {
            ConfigUtil.setVarchar2ByteSize(Integer.valueOf(databaseConfig.get(DB_VARCHAR2BYTE_LEN)));
        }
        if (!StringUtils.isEmpty(databaseConfig.get(USE_DB_TRIGGER))
                && "false".equalsIgnoreCase(databaseConfig.get(USE_DB_TRIGGER))) {
            ConfigUtil.setUseDbTrigger(false);
        }
        if(!StringUtils.isEmpty(databaseConfig.get(TABLE_NAME_PREFIX))){
            ConfigUtil.setTableNamePrefix(databaseConfig.get(TABLE_NAME_PREFIX));
        }

        initFileResolver();
    }

    public static void setProperties(Object object,
                                     Map<String, String> properties) {
        BeanWrapper wrapperImpl = BeanWrapperHolder.getInstance()
                .getBeanWrapper(object);
        for (String attribute : properties.keySet()) {
            try {
                String value = properties.get(attribute);
                wrapperImpl.setPropertyValue(attribute, value);
            } catch (Exception e) {
                throw new RuntimeException("设置对象属性出现异常", e);
            }
        }
    }

    /**
     * 从application.xml配置获取数据源并创建dataSource
     */
    public void bundlingDataSourceFromConfig() {
        DataSourceHolder.setDataSource(createDataSource());// 绑定数据源
    }

    private DatabaseInstallerProcessor initInstaller() {
        DatabaseInstallerProcessor installer = new DatabaseInstallerProcessor();
        List<InstallProcessor> installProcessors = new ArrayList<InstallProcessor>();
        ProcessorManager processorManager = ProcessorManagerImpl
                .getProcessorManager();

        TableInstallProcessor tableInstallProcessor = new TableInstallProcessor();
        TableProcessor tableProcessor = createTableProcessor(processorManager);
        tableInstallProcessor.setTableProcessor(tableProcessor);
        installProcessors.add(tableInstallProcessor);

        ViewInstallProcessor viewInstallProcessor = new ViewInstallProcessor();
        ViewProcessor viewProcessor = createViewProcessor(processorManager);
        viewInstallProcessor.setViewProcessor(viewProcessor);
        installProcessors.add(viewInstallProcessor);

        InitDataInstallProcessor initDataInstallProcessor = new InitDataInstallProcessor();
        InitDataProcessor initDataProcessor = createInitDataProcessor(processorManager);
        initDataInstallProcessor.setInitDataProcessor(initDataProcessor);
        installProcessors.add(initDataInstallProcessor);

        ProcedureInstallProcessor procedureInstallProcessor = new ProcedureInstallProcessor();
        ProcedureProcessor procedureProcessor = createProcedureProcessor(processorManager);
        procedureInstallProcessor.setProcedureProcessor(procedureProcessor);
        installProcessors.add(procedureInstallProcessor);

        TriggerInstallProcessor triggerInstallProcessor = new TriggerInstallProcessor();
        TriggerProcessor triggerProcessor = createTriggerProcessor(processorManager);
        triggerInstallProcessor.setProcessor(triggerProcessor);
        installProcessors.add(triggerInstallProcessor);

        SequenceInstallProcessor sequenceInstallProcessor = new SequenceInstallProcessor();
        SequenceProcessor sequenceProcessor = createSequenceProcessor(processorManager);
        sequenceInstallProcessor.setProcessor(sequenceProcessor);
        installProcessors.add(sequenceInstallProcessor);

        CustomSqlInstallProcessor customSqlInstallProcessor = new CustomSqlInstallProcessor();
        CustomSqlProcessor customSqlProcessor = createCustomSqlProcessor();
        customSqlInstallProcessor.setCustomSqlProcessor(customSqlProcessor);
        installProcessors.add(customSqlInstallProcessor);
        installer.setInstallProcessors(installProcessors);
        return installer;
    }

    private String resolverLanguageType() {
        PathFilter<XmlNode> filter = new PathFilter<XmlNode>(applicationNode);
        XmlNode xmlNode = filter
                .findNode("/application/database-install-processor/database-installer");
        if (xmlNode == null) {
            throw new RuntimeException("application.xml文件application/database-install-processor/database-installer节点未配置");
        }
        XmlNode node = xmlNode.getSubNode("database");
        String language = node.getAttribute("type");
        if (StringUtils.isBlank(language)) {
            language = "oracle";
        }
        return language;
    }

    private Map<String, String> resolverDatabaseNode() {
        Map<String, String> configMap = new HashMap<String, String>();
        PathFilter<XmlNode> filter = new PathFilter<XmlNode>(applicationNode);
        XmlNode xmlNode = filter
                .findNode("/application/database-install-processor/database-installer");
        if (xmlNode == null) {
            throw new RuntimeException("application.xml文件application/database-install-processor/database-installer节点未配置");
        }
        XmlNode node = xmlNode.getSubNode("database");
        String language = node.getAttribute(DB_TYPE_KEY);
        if (StringUtils.isBlank(language)) {
            language = DEFAULT_LANGUAGE;
        }
        configMap.put(LANGUAGE_KEY, language);
        XmlNode checkStrictNode = xmlNode.getSubNode("check-strict");
        if (checkStrictNode != null) {
            configMap.put(CHECK_STRICT_KEY, checkStrictNode.getAttribute("value"));
        }
        XmlNode checkModifiedNode = xmlNode.getSubNode("check-modified");
        if (checkModifiedNode != null) {
            configMap.put(CHECK_MODIFIED, checkModifiedNode.getAttribute("value"));
        }
        XmlNode initDataConflictDeleteNode = xmlNode.getSubNode("initdata-conflict-delete");
        if (initDataConflictDeleteNode != null) {
            configMap.put(INITDATA_ISDELETE_KEY, checkStrictNode.getAttribute("value"));
        }
        XmlNode char2byteNode = xmlNode.getSubNode("db-char2byte-length");
        if (char2byteNode != null) {
            String char2byteDbLen = char2byteNode.getAttribute("value");
            if (!StringUtils.isEmpty(char2byteDbLen)) {
                configMap.put(DB_CHAR2BYTE_LEN, char2byteDbLen);
            }
        }

        String varchar2byteValue = ConfigUtil
                .getPropertyValue(xmlNode, "db-varchar2byte-length", "value");
        if (!StringUtils.isEmpty(varchar2byteValue)) {
            configMap.put(DB_VARCHAR2BYTE_LEN, varchar2byteValue);
        }

        String userDbTrigger = ConfigUtil
                .getPropertyValue(xmlNode, "autoincrement-use-dbtrigger", "value");
        if (!StringUtils.isEmpty(userDbTrigger)) {
            configMap.put(USE_DB_TRIGGER, userDbTrigger);
        }

        putConfig(xmlNode, "table-name-prefix", configMap,TABLE_NAME_PREFIX);
        return configMap;
    }

    private void putConfig(XmlNode xmlNode, String propertyName,
                           Map<String,String> configMap,String configKey){
        String value = ConfigUtil
                .getPropertyValue(xmlNode, propertyName, "value");
        if (!StringUtils.isEmpty(value)) {
            configMap.put(configKey, value);
        }
    }

    private Map<String, String> resolverDatabaseFromProps() {
        Map<String, String> propsMap = new HashMap<String, String>((Map) toolProps);
        for (Object key : toolProps.keySet()) {
            propsMap.put(key.toString(), toolProps.get(key.toString()).toString());
        }
        Map<String, String> configMap = new HashMap<String, String>();
        if (!StringUtils.isBlank(propsMap.get(DB_TYPE_KEY))) {
            configMap.put(DB_TYPE_KEY, DEFAULT_LANGUAGE);
        }
        configMap.put(CHECK_STRICT_KEY, propsMap.get("value"));
        configMap.put(CHECK_MODIFIED, propsMap.get("check-strict"));
        configMap.put(INITDATA_ISDELETE_KEY, propsMap.get("initdata-conflict-delete"));
        return configMap;
    }

    /**
     * 数据库安装启动
     */
    public void installer() {
        LOGGER.info( "开始启动数据库安装操作");
        databaseInstaller();
        LOGGER.info( "数据库安装操作过程结束");

    }

    public List<String> getChangeSqls() {
        LOGGER.info( "开始检测数据库变化");
        List<String> sqls = installer.getChangeSqls();
        LOGGER.info("检测数据库变化过程结束");
        return sqls;
    }

    public List<String> getFullSqls() {
        LOGGER.info( "开始生成全量sql");
        List<String> sqls = installer.getFullSqls();
        LOGGER.info( "生成全量sql结束");
        return sqls;
    }

    public List<String> getDropSqls() {
        LOGGER.info( "开始生成全量删表sql");
        List<String> dropTableSqls = installer.getDropSqls();
        LOGGER.info( "生成全量删表sql结束");
        return dropTableSqls;
    }

    private void initFileResolver() {
        FileResolver fileResolver = createFileResolver();
        addXstreamFileProcessor(fileResolver);
        addConstantFileProcessor(fileResolver);
        addStandardTypeFileProcessor(fileResolver);
        addDialectFunctionFileProcessor(fileResolver);
        addBusinessTypeFileResolver(fileResolver);
        addStandardFieldFileResolver(fileResolver);
        addTableSpaceFileResolver(fileResolver);
        addTableFileResolver(fileResolver);
        addInitDataFileResolver(fileResolver);
        addCustomSqlFileResolver(fileResolver);
        addViewFileResolver(fileResolver);
        addProcedureFileResolver(fileResolver);
        addSequenceFileResolver(fileResolver);
        addTriggerFileResolver(fileResolver);
        startFileResolver(fileResolver);
    }

    private void addDialectFunctionFileProcessor(FileResolver fileResolver) {
        DialectFunctionlFileResolver dialectFunctionlFileResolver = new DialectFunctionlFileResolver();
        dialectFunctionlFileResolver.setFunctionProcessor(DialectFunctionProcessorImpl.getDialectFunctionProcessor());
        fileResolver.addFileProcessor(dialectFunctionlFileResolver);
    }

    private void databaseInstaller() {
        installer.process();
    }

    private void startFileResolver(FileResolver fileResolver) {
        fileResolver.resolve();
    }

    private void addProcedureFileResolver(FileResolver fileResolver) {
        ProcedureFileResolver procedureFileResolver = new ProcedureFileResolver();
        procedureFileResolver.setProcedureProcessor(ProcedureProcessorImpl
                .getProcedureProcessor());
        fileResolver.addFileProcessor(procedureFileResolver);
    }

    private void addSequenceFileResolver(FileResolver fileResolver){
        SequenceFileProcessor sequenceFileProcessor = new SequenceFileProcessor();
        sequenceFileProcessor.setProcessor(SequenceProcessorImpl.getSequenceProcessor());
        fileResolver.addFileProcessor(sequenceFileProcessor);
    }

    public void addTriggerFileResolver(FileResolver fileResolver){
        TriggerFileProcessor triggerFileProcessor = new TriggerFileProcessor();
        triggerFileProcessor.setProcessor(TriggerProcessorImpl.getTriggerProcessor());
        fileResolver.addFileProcessor(triggerFileProcessor);
    }
    private void addViewFileResolver(FileResolver fileResolver) {
        ViewFileResolver viewFileResolver = new ViewFileResolver();
        viewFileResolver.setViewProcessor(ViewProcessorImpl.getViewProcessor());
        fileResolver.addFileProcessor(viewFileResolver);
    }

    private void addCustomSqlFileResolver(FileResolver fileResolver) {
        CustomSqlFileResolver customSqlFileResolver = new CustomSqlFileResolver();
        customSqlFileResolver.setCustomSqlProcessor(CustomSqlProcessorImpl.getCustomSqlProcessor());
        fileResolver.addFileProcessor(customSqlFileResolver);
    }

    private void addInitDataFileResolver(FileResolver fileResolver) {
        InitDataFileResolver initDataFileResolver = new InitDataFileResolver();
        initDataFileResolver.setInitDataProcessor(InitDataProcessorImpl
                .getInitDataProcessor());
        fileResolver.addFileProcessor(initDataFileResolver);
    }

    private void addTableSpaceFileResolver(FileResolver fileResolver) {
        TableSpaceFileResolver tableSpaceFileResolver = new TableSpaceFileResolver();
        tableSpaceFileResolver.setTableSpaceProcessor(TableSpaceProcessorImpl.getTableSpaceProcessor());
        fileResolver.addFileProcessor(tableSpaceFileResolver);
    }

    private void addTableFileResolver(FileResolver fileResolver) {
        TableFileResolver tableFileResolver = new TableFileResolver();
        tableFileResolver.setTableProcessor(TableProcessorImpl
                .getTableProcessor());
        fileResolver.addFileProcessor(tableFileResolver);
    }

    private void addStandardFieldFileResolver(FileResolver fileResolver) {
        StandardFieldFileResolver standardFieldFileResolver = new StandardFieldFileResolver();
        standardFieldFileResolver
                .setStandardFieldProcessor(StandardFieldProcessorImpl
                        .getStandardFieldProcessor());
        fileResolver.addFileProcessor(standardFieldFileResolver);
    }

    private void addBusinessTypeFileResolver(FileResolver fileResolver) {
        BusinessTypeFileResolver businessTypeFileResolver = new BusinessTypeFileResolver();
        businessTypeFileResolver
                .setBusinessTypeProcessor(BusinessTypeProcessorImpl
                        .getBusinessTypeProcessor());
        fileResolver.addFileProcessor(businessTypeFileResolver);
    }


    private void addStandardTypeFileProcessor(FileResolver fileResolver) {
        StandardTypeFileResolver standardTypeFileResolver = new StandardTypeFileResolver();
        standardTypeFileResolver
                .setStandardDataTypeProcessor(StandardTypeProcessorImpl
                        .getStandardTypeProcessor());
        fileResolver.addFileProcessor(standardTypeFileResolver);
    }

    private void addConstantFileProcessor(FileResolver fileResolver) {
        ConstantFileResolver constantFileResolver = new ConstantFileResolver();
        constantFileResolver.setConstantProcessor(ConstantsProcessorImpl
                .getConstantProcessor());
        fileResolver.addFileProcessor(constantFileResolver);
    }

    private void addXstreamFileProcessor(FileResolver fileResolver) {
        fileResolver.addFileProcessor(new XStreamFileProcessor());
    }


    private FileResolver createFileResolver() {
        FileResolver fileResolver = FileResolverFactory.getFileResolver();
        FileResolverUtil.addClassPathPattern(fileResolver);
        fileResolver
                .addResolvePath(FileResolverUtil.getClassPath(fileResolver));
        fileResolver.addResolvePath(FileResolverUtil.getWebClasses());
        loadFileResolverConfig(fileResolver);
        return fileResolver;
    }

    private XmlNode getRootNode() {
        InputStream inputStream = DatabaseInstallerStart.class.getClassLoader()
                .getResourceAsStream(DEFAULT_CONFIG);
        if (inputStream == null) {
            inputStream = DatabaseInstallerStart.class
                    .getResourceAsStream(DEFAULT_CONFIG);
        }
        String applicationConfig = null;

        try {
            applicationConfig = StreamUtil.readText(inputStream, "UTF-8", true);
        } catch (IOException e1) {
            LOGGER.error("读取application.xml文件出错", e1);
            throw new RuntimeException(e1);
        }
        XmlStringParser parser = new XmlStringParser();
        return parser.parse(applicationConfig).getRoot();
    }

    private Properties parsePropertiesFile() {
        InputStream is = DatabaseInstallerStart.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_CONFIG);
        if (is == null) {
            return null;
        }
        try {
            toolProps = new Properties();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            toolProps.load(bf);
            return toolProps;
        } catch (UnsupportedEncodingException e) {
            LOGGER.debug("无法解析配置文件[%s]", e, PROPERTIES_CONFIG);
        } catch (IOException e) {
            LOGGER.debug("找不到配置文件[%s],将试图获取application.xml配置", e, PROPERTIES_CONFIG);
        }
        return null;
    }

    private DataSource createDataSource() {
        PathFilter<XmlNode> filter = new PathFilter<XmlNode>(applicationNode);
        XmlNode xmlNode = filter.findNode("/application/datasource");
        if (xmlNode != null) {
            String type = xmlNode.getAttribute(TYPE);
            try {
                DataSource dataSource = newDataSourceInstance(type);
                setProperties(xmlNode, dataSource);
                return dataSource;
            } catch (Exception e) {
                LOGGER.error("实例化数据源出错", e);
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("application.xml文件application/datasource数据源节点未配置");
    }

    private void setProperties(XmlNode node, Object object) {
        Map<String, String> properties = new HashMap<String, String>();
        List<XmlNode> subNodes = node.getSubNodes(PROPERTY);
        if (!CollectionUtils.isEmpty(subNodes)) {
            for (XmlNode subNode : subNodes) {
                properties.put(subNode.getAttribute("name"),
                        subNode.getAttribute("value"));
            }
        }
        BeanWrapper wrapperImpl = BeanWrapperHolder.getInstance()
                .getBeanWrapper(object);
        wrapperImpl.setPropertyValues(properties);
    }

    private DataSource newDataSourceInstance(String type)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        Class dataSourceType = Class.forName(type);
        DataSource dataSource = (DataSource) dataSourceType.newInstance();
        return dataSource;
    }

    private void loadFileResolverConfig(FileResolver fileResolver) {
        if (applicationNode == null) {
            return;
        }
        PathFilter<XmlNode> filter = new PathFilter<XmlNode>(applicationNode);
        List<XmlNode> classPathList = filter
                .findNodeList("/application/file-resolver-configuration/class-paths/class-path");
        for (XmlNode classPath : classPathList) {
            fileResolver.addResolvePath(classPath.getAttribute("path"));
        }
        List<XmlNode> includePatternList = filter
                .findNodeList("/application/file-resolver-configuration/include-patterns/include-pattern");
        for (XmlNode includePatternNode : includePatternList) {
            fileResolver.addIncludePathPattern(includePatternNode
                    .getAttribute("pattern"));
        }

    }

    private CustomSqlProcessor createCustomSqlProcessor() {
        CustomSqlProcessor customSqlProcessor = CustomSqlProcessorImpl
                .getCustomSqlProcessor();
        return customSqlProcessor;
    }

    private SequenceProcessor createSequenceProcessor(
            ProcessorManager processorManager) {
        SequenceProcessor sequenceProcessor = SequenceProcessorImpl
                .getSequenceProcessor();
        sequenceProcessor.setProcessorManager(processorManager);
        return sequenceProcessor;
    }

    private TriggerProcessor createTriggerProcessor(
            ProcessorManager processorManager) {
        TriggerProcessor triggerProcessor = TriggerProcessorImpl
                .getTriggerProcessor();
        triggerProcessor.setProcessorManager(processorManager);
        return triggerProcessor;
    }

    private ProcedureProcessor createProcedureProcessor(
            ProcessorManager processorManager) {
        ProcedureProcessor procedureProcessor = ProcedureProcessorImpl
                .getProcedureProcessor();
        procedureProcessor.setProcessorManager(processorManager);
        return procedureProcessor;
    }

    private InitDataProcessor createInitDataProcessor(
            ProcessorManager processorManager) {
        InitDataProcessor initDataProcessor = InitDataProcessorImpl
                .getInitDataProcessor();
        initDataProcessor.setProcessorManager(processorManager);
        return initDataProcessor;
    }

    private ViewProcessor createViewProcessor(ProcessorManager processorManager) {
        ViewProcessor viewProcessor = ViewProcessorImpl.getViewProcessor();
        viewProcessor.setProcessorManager(processorManager);
        return viewProcessor;
    }

    private TableProcessor createTableProcessor(
            ProcessorManager processorManager) {
        TableProcessor tableProcessor = TableProcessorImpl.getTableProcessor();
        tableProcessor.setProcessorManager(processorManager);
        return tableProcessor;
    }

}
