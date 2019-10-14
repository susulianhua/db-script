/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.cepcore.impl;

import org.tinygroup.cepcore.*;
import org.tinygroup.event.Event;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CEPCoreProcessFilterChainImpl implements CEPCoreProcessFilterChain {
    private static final Logger LOGGER = LoggerFactory.getLogger(CEPCoreProcessFilterChainImpl.class);
    List<CEPCoreProcessFilter> filters = new ArrayList<CEPCoreProcessFilter>();
    // boolean filtersSetTag = false;

    public CEPCoreProcessFilterChainImpl(List<CEPCoreProcessFilter> filters) {
        setFilters(filters);
    }

    public List<CEPCoreProcessFilter> getFilters() {
        return filters;
    }

    private void setFilters(List<CEPCoreProcessFilter> filters) {
        this.filters = filters;
    }

    public CEPCoreProcessDealer getDealer(CEPCoreProcessDealer d, Event e, CEPCore core, EventProcessor processor) {
        CEPCoreProcessDealer last = d;
        for (int i = filters.size() - 1; i >= 0; i--) {
            final CEPCoreProcessFilter filter = filters.get(i);
            final CEPCoreProcessDealer next = last;
            last = new CEPCoreProcessDealer() {
                public void process(Event e, CEPCore core, EventProcessor processor) {
                    filter.process(next, e, core, processor);
                }
            };

        }
        return last;
    }


    @Override
    public void insertFilterToBegin(CEPCoreProcessFilter filter) {
        filters.add(0, filter);
    }


    @Override
    public void addFilter(CEPCoreProcessFilter filter) {
        filters.add(filter);
    }


}
