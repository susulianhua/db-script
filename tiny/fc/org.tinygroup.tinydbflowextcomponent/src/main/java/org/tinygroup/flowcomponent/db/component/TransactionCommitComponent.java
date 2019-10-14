package org.tinygroup.flowcomponent.db.component;

import org.springframework.transaction.TransactionStatus;
import org.tinygroup.context.Context;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * Created by wangwy11342 on 2016/8/21.
 */
public class TransactionCommitComponent extends AbstractTransactionComponent {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(TransactionCommitComponent.class);

    public void execute(Context context) {
        TransactionStatus status = transactionStatusThreadLocal.get();
        if (status != null && !status.isCompleted()) {
            LOGGER.logMessage(LogLevel.INFO, "提交事务");
            getDataSourceTransactionManager().commit(status);
            context.put(resultKey, "commit transaction");
            return;
        }
        context.put(resultKey, "status is null or completed!");
    }
}
