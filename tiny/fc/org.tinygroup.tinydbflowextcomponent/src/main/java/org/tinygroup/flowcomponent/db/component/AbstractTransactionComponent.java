package org.tinygroup.flowcomponent.db.component;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowcomponent.db.DataSourceHold;

/**
 * Created by wangwy11342 on 2016/8/21.
 */
public abstract class AbstractTransactionComponent implements ComponentInterface {

    protected static ThreadLocal<TransactionStatus> transactionStatusThreadLocal = new ThreadLocal<TransactionStatus>();
    protected DataSourceHold dataSourceHold;
    protected TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
    String resultKey;

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public DataSourceHold getDataSourceHold() {
        return dataSourceHold;
    }

    public void setDataSourceHold(DataSourceHold dataSourceHold) {
        this.dataSourceHold = dataSourceHold;
    }

    public DataSourceTransactionManager getDataSourceTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(
                dataSourceHold.getDataSource());
        return transactionManager;
    }


}
