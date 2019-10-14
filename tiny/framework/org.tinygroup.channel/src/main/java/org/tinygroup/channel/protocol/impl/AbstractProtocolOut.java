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

import org.tinygroup.channel.protocol.ProtocolOutInterface;
import org.tinygroup.channel.protocol.ProtocolOutListener;
import org.tinygroup.context.Context;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractProtocolOut<ProRequest, ProResponse> extends
        AbstractProtocol implements
        ProtocolOutInterface<ProRequest, ProResponse> {
    private List<ProtocolOutListener> listeners = new ArrayList<ProtocolOutListener>();

    public List<ProtocolOutListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<ProtocolOutListener> listeners) {
        this.listeners = listeners;
    }

    public void addProtocolListener(ProtocolOutListener listener) {
        listeners.add(listener);
    }

    protected Object beforeSend(Object bizRequest, Context context) {
        for (ProtocolOutListener listener : listeners) {
            bizRequest = listener.beforeSend(bizRequest, context);
        }
        return bizRequest;
    }

    protected ProRequest afterBizToRequestTrans(Object bizRequest,
                                                ProRequest request, Context context) {
        for (ProtocolOutListener listener : listeners) {
            request = (ProRequest) listener.afterBizToRequestTrans(bizRequest,
                    request, context);
        }
        return request;
    }

    protected ProResponse afterSend(ProRequest proRequest,
                                    ProResponse proResponse, Context context) {
        for (ProtocolOutListener listener : listeners) {
            proResponse = (ProResponse) listener.afterSend(proRequest,
                    proResponse, context);
        }
        return proResponse;
    }

    protected Object afterResponseToBizTrans(ProResponse proResponse,
                                             Object bizResponse, Context context) {
        for (ProtocolOutListener listener : listeners) {
            bizResponse = listener.afterResponseToBizTrans(proResponse,
                    bizResponse, context);
        }
        return bizResponse;
    }
}
