package org.tinygroup.flowcomponent.db.component;

import org.tinygroup.context.Context;
import org.tinygroup.flowcomponent.exception.FlowComponentException;
import org.tinygroup.flowcomponent.exception.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * Created by wangwy11342 on 2016/8/21.
 */
public class JdbcTemplateUpdateComponent extends AbstractJdbcTemplateComponent {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(JdbcTemplateUpdateComponent.class);

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "数据库更新组件开始执行。更新语句：{0}", sql);
        try {
            context.put(resultKey, jdbcTemplate.update(sql));
            LOGGER.logMessage(LogLevel.INFO, "数据库更新件执行成功。更新语句：{0}", sql);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "数据库更新组件开始执行。更新语句：{0}；错误信息：{1}",
                    sql, e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.DB_UPDATE_FAILED, sql, e);
        }
    }
}
