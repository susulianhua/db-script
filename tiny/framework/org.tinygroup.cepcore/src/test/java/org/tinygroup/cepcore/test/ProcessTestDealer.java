package org.tinygroup.cepcore.test;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreProcessDealer;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.Event;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class ProcessTestDealer extends BaseProcess implements CEPCoreProcessDealer {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessTestDealer.class);

    private String index = "";

    public ProcessTestDealer(String index) {
        super();
        this.index = index;

    }

    public void process(Event e, CEPCore core, EventProcessor processor) {
        LOGGER.infoMessage("=============={}===============", index);
        addValue(index);
    }

}
