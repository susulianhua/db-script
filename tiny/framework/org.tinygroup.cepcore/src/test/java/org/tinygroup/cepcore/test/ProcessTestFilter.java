package org.tinygroup.cepcore.test;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreProcessDealer;
import org.tinygroup.cepcore.CEPCoreProcessFilter;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.Event;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class ProcessTestFilter extends BaseProcess implements CEPCoreProcessFilter {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessTestFilter.class);
    private CEPCoreProcessFilter nextFilter;
    private String index = "";

    public ProcessTestFilter(String index) {
        super();
        this.index = index;

    }

    public CEPCoreProcessFilter getNext() {
        return nextFilter;
    }

    public void setNext(CEPCoreProcessFilter filter) {
        this.nextFilter = filter;
    }

    public void process(CEPCoreProcessDealer dealer, Event e, CEPCore core, EventProcessor processor) {
        LOGGER.infoMessage("=============={}===============", index);
        addValue(index);
        dealer.process(e, core, processor);
    }


}
