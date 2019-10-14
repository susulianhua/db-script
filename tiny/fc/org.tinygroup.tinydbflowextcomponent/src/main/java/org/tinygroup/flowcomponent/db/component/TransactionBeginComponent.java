package org.tinygroup.flowcomponent.db.component;

import org.springframework.transaction.TransactionStatus;
import org.tinygroup.context.Context;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * Created by wangwy11342 on 2016/8/21.
 */
public class TransactionBeginComponent extends AbstractTransactionComponent {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(TransactionBeginComponent.class);

    public void execute(Context context) {
        TransactionStatus status = transactionStatusThreadLocal.get();
        if (status == null || status.isCompleted()) {
            status = getDataSourceTransactionManager().getTransaction(
                    transactionDefinition);
            transactionStatusThreadLocal.set(status);
            if (status.isNewTransaction()) {
                LOGGER.logMessage(LogLevel.INFO, "开启新事务");
            } else {
                LOGGER.logMessage(LogLevel.INFO, "开启新事务未开启新事务,将使用之前开启的事务");
            }
            context.put(resultKey, "begin transaction");
        }
    }
}
