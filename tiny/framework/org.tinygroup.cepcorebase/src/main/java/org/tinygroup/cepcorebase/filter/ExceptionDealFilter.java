package org.tinygroup.cepcorebase.filter;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreProcessDealer;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.util.CEPCoreUtil;
import org.tinygroup.event.Event;

public class ExceptionDealFilter extends AbstractCEPCoreProcessFilter {
    private void dealException(Throwable e, Event event) {
        CEPCoreUtil.handle(e, event, this.getClass().getClassLoader());
        Throwable t = e.getCause();
        while (t != null) {
            CEPCoreUtil.handle(t, event, this.getClass().getClassLoader());
            t = t.getCause();
        }
    }

    @Override
    public void process(CEPCoreProcessDealer dealer, Event event, CEPCore core, EventProcessor processor) {
        try {
            dealer.process(event, core, processor);
        } catch (RuntimeException e) {
            dealException(e, event);
            throw e;
        }
    }
}
