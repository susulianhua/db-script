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
package org.tinygroup.weblayer.listener.impl;

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.listener.ListenerInstanceBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @param <INSTANCE>
 * @author renhui
 */
public abstract class AbstractListenerBuilder<INSTANCE> implements
        ListenerInstanceBuilder<INSTANCE> {

    private static Logger logger = LoggerFactory
            .getLogger(AbstractListenerBuilder.class);
    protected List<INSTANCE> listeners = Collections
            .synchronizedList(new ArrayList<INSTANCE>());
    private boolean ordered;// 是否已经进行排序了

    public void buildInstances(INSTANCE object) {
        INSTANCE listener = object;
        if (!imptOrdered(object)) {
            listener = replaceListener(object);
            logger.logMessage(LogLevel.DEBUG,
                    "listener:[{0}] not implements Ordered will replace [{1}]",
                    object.getClass().getSimpleName(), listener.getClass()
                            .getSimpleName());
        }
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    protected abstract INSTANCE replaceListener(INSTANCE listener);

    private boolean imptOrdered(Object object) {
        return object instanceof Ordered;
    }

    public List<INSTANCE> getInstances() {
        synchronized (listeners) {
            if (!ordered) {
                Collections.sort(listeners, new Comparator<INSTANCE>() {
                    public int compare(INSTANCE o1, INSTANCE o2) {
                        Ordered order1 = (Ordered) o1;
                        Ordered order2 = (Ordered) o2;
                        if (order1 != null && order2 != null) {
                            return order1.getOrder() > order2.getOrder() ? 1
                                    : (order1.getOrder() == order2.getOrder() ? 0
                                    : -1);
                        }
                        return 0;
                    }
                });
                ordered = true;
            }
        }
        return listeners;
    }

    @Override
    public void clear() {
        listeners.clear();
    }

}
