package org.tinygroup.cepcorebase.filter;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreProcessDealer;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.aop.CEPCoreAopManager;
import org.tinygroup.cepcore.util.CEPCoreUtil;
import org.tinygroup.event.Event;

public class CEPCoreAopFilter extends AbstractCEPCoreProcessFilter {
    private CEPCoreAopManager aopMananger;

    public CEPCoreAopManager getAopMananger() {
        return aopMananger;
    }

    public void setAopMananger(CEPCoreAopManager aopMananger) {
        this.aopMananger = aopMananger;
    }

    public void process(CEPCoreProcessDealer dealer, Event event, CEPCore core, EventProcessor eventProcessor) {
        CEPCoreAopManager aop = getAopMananger();
        getAopMananger().beforeHandle(event);
        if (CEPCoreUtil.isLocal(eventProcessor)) {
            aop.beforeLocalHandle(event);
            dealer.process(event, core, eventProcessor);
            aop.afterLocalHandle(event);
        } else {
            aop.beforeRemoteHandle(event);
            dealer.process(event, core, eventProcessor);
            aop.afterRemoteHandle(event);
        }
        aop.afterHandle(event);
    }


}