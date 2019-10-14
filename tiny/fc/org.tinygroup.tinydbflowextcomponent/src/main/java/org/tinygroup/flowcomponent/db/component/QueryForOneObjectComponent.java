package org.tinygroup.flowcomponent.db.component;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.tinygroup.context.Context;
import org.tinygroup.flowcomponent.db.rowmapper.TinyBeanPropertyRowMapper;
import org.tinygroup.flowcomponent.exception.FlowComponentException;
import org.tinygroup.flowcomponent.exception.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * Created by wangwy11342 on 2016/8/21.
 */
public class QueryForOneObjectComponent extends AbstractJdbcTemplateComponent {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(QueryForOneObjectComponent.class);
    String mappedClassName;

    public String getMappedClassName() {
        return mappedClassName;
    }

    public void setMappedClassName(String mappedClassName) {
        this.mappedClassName = mappedClassName;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "数据库查询组件开始执行，返回Object。查询语句：{0}",
                sql);
        try {
            Class<?> requiredType = Class.forName(mappedClassName);
            if (!isPojo(requiredType)) {
                throw new FlowComponentException(FlowComponentExceptionErrorCode.CLASS_INSTANTIATION_FAILED, mappedClassName);
            }
            context.put(resultKey, jdbcTemplate.queryForObject(sql,
                    new TinyBeanPropertyRowMapper(requiredType)));
        } catch (ClassNotFoundException e) {
            LOGGER.logMessage(LogLevel.ERROR,
                    "数据库查询组件执行错误，返回Object。查询语句：{0}", sql);
            throw new FlowComponentException(FlowComponentExceptionErrorCode.CLASS_NOT_FOUND, mappedClassName);
        }
        LOGGER.logMessage(LogLevel.INFO, "数据库查询组件执行结束，返回Object。查询语句：{0}",
                sql);
    }

    private boolean isPojo(Class<?> requiredType) {
        try {
            BeanUtils.instantiateClass(requiredType);
        } catch (BeanInstantiationException e) {
            return false;
        }
        return true;
    }
}
