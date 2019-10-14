package org.tinygroup.cepcorebase.filter;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreProcessDealer;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.logger.LoggerFactory;

public class ThreadVariableChangeFilter extends AbstractCEPCoreProcessFilter {
    private void putThreadVariable(Event event) {
        ServiceRequest request = event.getServiceRequest();
        LoggerFactory.putThreadVariable(LoggerFactory.SERVICE_EVENTID, event.getEventId());
        LoggerFactory.putThreadVariable(LoggerFactory.SERVICE_SERVICEID, request.getServiceId());
    }

    private void clearThreadVariable() {
        LoggerFactory.removeThreadVariable(LoggerFactory.SERVICE_EVENTID);
        LoggerFactory.removeThreadVariable(LoggerFactory.SERVICE_SERVICEID);
    }

    public void process(CEPCoreProcessDealer dealer, Event event, CEPCore core, EventProcessor processor) {
        putThreadVariable(event);
        try {
            dealer.process(event, core, processor);
        } finally {
            clearThreadVariable();
        }
    }
}