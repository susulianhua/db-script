package org.tinygroup.flowcomponent.db.component;

import org.springframework.transaction.TransactionStatus;
import org.tinygroup.context.Context;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * Created by wangwy11342 on 2016/8/21.
 */
public class TransactionRollbackComponent extends AbstractTransactionComponent {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(TransactionRollbackComponent.class);

    public void execute(Context context) {
        TransactionStatus status = transactionStatusThreadLocal.get();
        if (status != null && !status.isCompleted()) {
            LOGGER.logMessage(LogLevel.INFO, "事务将回滚");
            getDataSourceTransactionManager().rollback(status);
            context.put(resultKey, "rollback transaction");
            return;
        }
        context.put(resultKey, "status is null or completed!");
    }
}
