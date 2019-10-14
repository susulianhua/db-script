package org.tinygroup.flowcomponent.db.component;

import org.tinygroup.context.Context;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * Created by wangwy11342 on 2016/8/21.
 */
public class QueryForNumberComponent extends AbstractJdbcTemplateComponent {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(QueryForNumberComponent.class);

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "数据库查询组件开始执行，返回number。查询语句：{0}", sql);
        context.put(resultKey, jdbcTemplate.queryForObject(sql, Number.class));
        LOGGER.logMessage(LogLevel.INFO, "数据库查询组件执行结束，返回number。查询语句：{0}", sql);
    }
}
