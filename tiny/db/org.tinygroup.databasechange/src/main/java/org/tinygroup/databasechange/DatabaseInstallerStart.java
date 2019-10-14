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
package org.tinygroup.databasechange;

import org.springframework.beans.BeanWrapper;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.beanwrapper.BeanWrapperHolder;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.database.ProcessorManager;
import org.tinygroup.database.customesql.CustomSqlProcessor;
import org.tinygroup.database.customesql.impl.CustomSqlProcessorImpl;
import org.tinygroup.database.dialectfunction.impl.DialectFunctionProcessorImpl;
import org.tinygroup.database.fileresolver.*;
import org.tinygroup.database.impl.ProcessorManagerImpl;
import org.tinygroup.database.initdata.InitDataProcessor;
import org.tinygroup.database.initdata.impl.InitDataProcessorImpl;
import org.tinygroup.database.procedure.ProcedureProcessor;
import org.tinygroup.database.procedure.impl.ProcedureProcessorImpl;
import org.tinygroup.database.sequence.SequenceProcessor;
import org.tinygroup.database.sequence.impl.SequenceProcessorImpl;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.database.table.impl.TableProcessorImpl;
import org.tinygroup.database.tablespace.impl.TableSpaceProcessorImpl;
import org.tinygroup.database.trigger.TriggerProcessor;
import org.tinygroup.database.trigger.impl.TriggerProcessorImpl;
import org.tinygroup.database.view.ViewProcessor;
import org.tinygroup.database.view.impl.ViewProcessorImpl;
import org.tinygroup.databasebuinstaller.DataSourceHolder;
import org.tinygroup.databasebuinstaller.DatabaseInstallerProcessor;
import org.tinygroup.databasebuinstaller.InstallProcessor;
import org.tinygroup.databasebuinstaller.impl.*;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.FileResolverFactory;
import org.tinygroup.fileresolver.FileResolverUtil;
import org.tinygroup.fileresolver.impl.I18nFileProcessor;
import org.tinygroup.fileresolver.impl.XStreamFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.metadata.bizdatatype.impl.BusinessTypeProcessorImpl;
import org.tinygroup.metadata.constants.impl.ConstantsProcessorImpl;
import org.tinygroup.metadata.defaultvalue.impl.DefaultValueProcessorImpl;
import org.tinygroup.metadata.errormessage.impl.ErrorMessageProcessorImpl;
import org.tinygroup.metadata.fileresolver.*;
import org.tinygroup.metadata.stddatatype.impl.StandardTypeProcessorImpl;
import org.tinygroup.metadata.stdfield.impl.StandardFieldProcessorImpl;
import org.tinygroup.metadata.util.ConfigUtil;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.springutil.SpringBeanContainer;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

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
        if (!StringUtil.isEmpty(databaseConfig.get(DB_CHAR2BYTE_LEN))) {
            ConfigUtil.setChar2ByteSize(Integer.valueOf(databaseConfig.get(DB_CHAR2BYTE_LEN)));
        }
        if (!StringUtil.isEmpty(databaseConfig.get(DB_VARCHAR2BYTE_LEN))) {
            ConfigUtil.setVarchar2ByteSize(Integer.valueOf(databaseConfig.get(DB_VARCHAR2BYTE_LEN)));
        }
        if (!StringUtil.isEmpty(databaseConfig.get(USE_DB_TRIGGER))
                && "false".equalsIgnoreCase(databaseConfig.get(USE_DB_TRIGGER))) {
            ConfigUtil.setUseDbTrigger(false);
        }
        if(!StringUtil.isEmpty(databaseConfig.get(TABLE_NAME_PREFIX))){
            ConfigUtil.setTableNamePrefix(databaseConfig.get(TABLE_NAME_PREFIX));
        }

        BeanContainerFactory.initBeanContainer(SpringBeanContainer.class.getName());
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
        if (StringUtil.isBlank(language)) {
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
        if (StringUtil.isBlank(language)) {
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
            if (!StringUtil.isEmpty(char2byteDbLen)) {
                configMap.put(DB_CHAR2BYTE_LEN, char2byteDbLen);
            }
        }

        String varchar2byteValue = ConfigUtil
                .getPropertyValue(xmlNode, "db-varchar2byte-length", "value");
        if (!StringUtil.isEmpty(varchar2byteValue)) {
            configMap.put(DB_VARCHAR2BYTE_LEN, varchar2byteValue);
        }

        String userDbTrigger = ConfigUtil
                .getPropertyValue(xmlNode, "autoincrement-use-dbtrigger", "value");
        if (!StringUtil.isEmpty(userDbTrigger)) {
            configMap.put(USE_DB_TRIGGER, userDbTrigger);
        }

        putConfig(xmlNode, "table-name-prefix", configMap,TABLE_NAME_PREFIX);
        return configMap;
    }

    private void putConfig(XmlNode xmlNode, String propertyName,
                             Map<String,String> configMap,String configKey){
        String value = ConfigUtil
                .getPropertyValue(xmlNode, propertyName, "value");
        if (!StringUtil.isEmpty(value)) {
            configMap.put(configKey, value);
        }
    }

    private Map<String, String> resolverDatabaseFromProps() {
        Map<String, String> propsMap = new HashMap<String, String>((Map) toolProps);
        for (Object key : toolProps.keySet()) {
            propsMap.put(key.toString(), toolProps.get(key.toString()).toString());
        }
        Map<String, String> configMap = new HashMap<String, String>();
        if (!StringUtil.isBlank(propsMap.get(DB_TYPE_KEY))) {
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
        LOGGER.logMessage(LogLevel.INFO, "开始启动数据库安装操作");
        databaseInstaller();
        LOGGER.logMessage(LogLevel.INFO, "数据库安装操作过程结束");

    }

    public List<String> getChangeSqls() {
        LOGGER.logMessage(LogLevel.INFO, "开始检测数据库变化");
        List<String> sqls = installer.getChangeSqls();
        LOGGER.logMessage(LogLevel.INFO, "检测数据库变化过程结束");
        return sqls;
    }

    public List<String> getFullSqls() {
        LOGGER.logMessage(LogLevel.INFO, "开始生成全量sql");
        List<String> sqls = installer.getFullSqls();
        LOGGER.logMessage(LogLevel.INFO, "生成全量sql结束");
        return sqls;
    }

    public List<String> getDropSqls() {
        LOGGER.logMessage(LogLevel.INFO, "开始生成全量删表sql");
        List<String> dropTableSqls = installer.getDropSqls();
        LOGGER.logMessage(LogLevel.INFO, "生成全量删表sql结束");
        return dropTableSqls;
    }

    private void initFileResolver() {
        FileResolver fileResolver = createFileResolver();
        addI18nFileProcessor(fileResolver);
        addXstreamFileProcessor(fileResolver);
        addConstantFileProcessor(fileResolver);
        addStandardTypeFileProcessor(fileResolver);
        addErrorMessageFileProcessor(fileResolver);
        addDialectFunctionFileProcessor(fileResolver);
        addBusinessTypeFileResolver(fileResolver);
        addStandardFieldFileResolver(fileResolver);
        addTableSpaceFileResolver(fileResolver);
        addTableFileResolver(fileResolver);
        addInitDataFileResolver(fileResolver);
        addCustomSqlFileResolver(fileResolver);
        addViewFileResolver(fileResolver);
        addProcedureFileResolver(fileResolver);
        addDefaultValueResolver(fileResolver);
        startFileResolver(fileResolver);
    }

    private void addDialectFunctionFileProcessor(FileResolver fileResolver) {
        DialectFunctionlFileResolver dialectFunctionlFileResolver = new DialectFunctionlFileResolver();
        dialectFunctionlFileResolver.setFunctionProcessor(DialectFunctionProcessorImpl.getDialectFunctionProcessor());
        fileResolver.addFileProcessor(dialectFunctionlFileResolver);
    }

    private void addDefaultValueResolver(FileResolver fileResolver) {
        DefaultValueFileResolver defaultValueFileResolver = new DefaultValueFileResolver();
        defaultValueFileResolver.setDefaultValueProcessor(DefaultValueProcessorImpl.getDefaultValueProcessor());
        fileResolver.addFileProcessor(defaultValueFileResolver);
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

    private void addErrorMessageFileProcessor(FileResolver fileResolver) {
        ErrorMessageFileResolver errorMessageFileResolver = new ErrorMessageFileResolver();
        errorMessageFileResolver
                .setErrorMessageProcessor(ErrorMessageProcessorImpl
                        .getErrorMessageProcessor());
        fileResolver.addFileProcessor(errorMessageFileResolver);
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

    private void addI18nFileProcessor(FileResolver fileResolver) {
        fileResolver.addFileProcessor(new I18nFileProcessor());
    }

    private FileResolver createFileResolver() {
        FileResolver fileResolver = FileResolverFactory.getFileResolver();
        FileResolverUtil.addClassPathPattern(fileResolver);
        fileResolver
                .addResolvePath(FileResolverUtil.getClassPath(fileResolver));
        fileResolver.addResolvePath(FileResolverUtil.getWebClasses());
        try {
            fileResolver.addResolvePath(FileResolverUtil
                    .getWebLibJars(fileResolver));
        } catch (Exception e) {
            LOGGER.errorMessage("为文件扫描器添加webLibJars时出现异常", e);
        }
        fileResolver.addIncludePathPattern(TINY_JAR_PATTERN);
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
            LOGGER.errorMessage("读取application.xml文件出错", e1);
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
            LOGGER.debugMessage("无法解析配置文件[%s]", e, PROPERTIES_CONFIG);
        } catch (IOException e) {
            LOGGER.debugMessage("找不到配置文件[%s],将试图获取application.xml配置", e, PROPERTIES_CONFIG);
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
                LOGGER.errorMessage("实例化数据源出错", e);
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("application.xml文件application/datasource数据源节点未配置");
    }

    private void setProperties(XmlNode node, Object object) {
        Map<String, String> properties = new HashMap<String, String>();
        List<XmlNode> subNodes = node.getSubNodes(PROPERTY);
        if (!CollectionUtil.isEmpty(subNodes)) {
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
