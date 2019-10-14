package org.tinygroup.flowcomponent.util.component;

import org.springframework.beans.factory.InitializingBean;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowcomponent.db.DataSourceHold;
import org.tinygroup.flowcomponent.exception.FlowComponentException;
import org.tinygroup.flowcomponent.exception.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.sequence.impl.DefaultSequence;
import org.tinygroup.sequence.impl.DefaultSequenceDao;
import org.tinygroup.sequence.impl.MultipleSequenceDao;
import org.tinygroup.sequence.impl.MultipleSequenceFactory;

/**
 * 生成流水号
 *
 * @author qiucn
 */
public class SerialNumberCreateComponent implements ComponentInterface,
        InitializingBean {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SerialNumberCreateComponent.class);
    private String serialNumber;

    private String resultKey;

    private DataSourceHold dataSourceHold;

    private MultipleSequenceFactory sequenceFactory;

    private DefaultSequence defaultSequence;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "流水号生成组件开始执行");
        try {
            long next;
            if (sequenceFactory != null) {
                next = sequenceFactory.getNextValue(serialNumber);
            } else {
                defaultSequence.setName(serialNumber);
                next = defaultSequence.nextValue();
            }
            context.put(resultKey, next);
            LOGGER.logMessage(LogLevel.INFO, "流水号生成组件执行完毕");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.INFO, "流水号生成失败，错误信息：{0}", e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.SERIANUM_CREATE_FAILED, e);
        }
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DataSourceHold getDataSourceHold() {
        return dataSourceHold;
    }

    public void setDataSourceHold(DataSourceHold dataSourceHold) {
        this.dataSourceHold = dataSourceHold;
    }

    public void afterPropertiesSet() throws Exception {
        //不允许多次初始化,确保sequenceFactory是单实例
        if (dataSourceHold.getDataSources().size() > 1) {
            sequenceFactory = new MultipleSequenceFactory();
            MultipleSequenceDao multipleSequenceDao = new MultipleSequenceDao();
            multipleSequenceDao.setDataSourceList(dataSourceHold.getDataSources());
            sequenceFactory.setMultipleSequenceDao(multipleSequenceDao);
            sequenceFactory.init();
        } else {
            defaultSequence = new DefaultSequence();
            DefaultSequenceDao sequenceDao = new DefaultSequenceDao();
            sequenceDao.setStep(1);
            sequenceDao.setDataSource(dataSourceHold.getDataSources().get(0));
            defaultSequence.setSequenceDao(sequenceDao);
        }
    }

}
