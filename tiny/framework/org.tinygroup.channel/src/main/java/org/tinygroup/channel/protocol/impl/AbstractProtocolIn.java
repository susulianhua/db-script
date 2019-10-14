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
package org.tinygroup.channel.protocol.impl;

import org.tinygroup.channel.protocol.ProtocolInInterface;
import org.tinygroup.channel.protocol.ProtocolInListener;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractProtocolIn<ProRequest, ProResponse> extends
        AbstractProtocol implements
        ProtocolInInterface<ProRequest, ProResponse> {
    private List<ProtocolInListener> listeners = new ArrayList<ProtocolInListener>();

    public List<ProtocolInListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<ProtocolInListener> listeners) {
        this.listeners = listeners;
    }

    public void addProtocolListener(ProtocolInListener listener) {
        listeners.add(listener);
    }

    public ProResponse received(ProRequest proRequest) {
        Context context = ContextFactory.getContext();
        ProResponse proResponse = realDeal(proRequest, context);
        return proResponse;
    }

    protected abstract ProResponse realDeal(ProRequest proRequest,
                                            Context context);

    protected ProRequest afterReceived(ProRequest proRequest, Context context) {
        for (ProtocolInListener listener : listeners) {
            proRequest = (ProRequest) listener.afterReceived(proRequest,
                    context);
        }
        return proRequest;
    }

    protected Object afterRequestToBizTrans(ProRequest proRequest,
                                            Object bizRequest, Context context) {
        for (ProtocolInListener listener : listeners) {
            bizRequest = listener.afterRequestToBizTrans(proRequest,
                    bizRequest, context);
        }
        return bizRequest;
    }

    protected Object afterProcess(ProRequest request, Object bizResponse,
                                  Context context) {
        for (ProtocolInListener listener : listeners) {
            bizResponse = listener.afterProcess(request, bizResponse, context);
        }
        return bizResponse;
    }

    protected ProResponse afterBizToResponseTrans(Object bizResponse,
                                                  ProResponse proResponse, Context context) {
        for (ProtocolInListener listener : listeners) {
            proResponse = (ProResponse) listener.afterBizToResponseTrans(
                    bizResponse, proResponse, context);
        }
        return proResponse;
    }

}
