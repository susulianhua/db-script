package org.tinygroup.cepcorebase.filter;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreProcessDealer;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcoreimpl.ServiceParamUtil;
import org.tinygroup.context.Context;
import org.tinygroup.event.Event;

public class ContextChangeFilter extends AbstractCEPCoreProcessFilter {


    public void process(CEPCoreProcessDealer dealer, Event event, CEPCore core, EventProcessor processor) {
        Context oldContext = event.getServiceRequest().getContext();
        ServiceParamUtil.changeEventContext(event, core, Thread.currentThread().getContextClassLoader());
        try {
            dealer.process(event, core, processor);
        } finally {
            ServiceParamUtil.resetEventContext(event, core, oldContext);
        }
    }


}
