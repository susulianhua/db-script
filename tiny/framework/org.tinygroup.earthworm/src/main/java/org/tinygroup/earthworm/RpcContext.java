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
package org.tinygroup.earthworm;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.earthworm.util.EarthWormHelper;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RpcContext extends WormBaseContext {

    public static final int RPC_STAT_SUCESS = 0;
    public static final int RPC_STAT_FAIL = 1;
    private static final String LOCAL_INDEX_PRE = "0.";
    private static final ThreadLocal<RpcContext> threadLocal = new ThreadLocal<RpcContext>();
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcContext.class);
    private static final String SPAN_ID_KEY = "spanId";
    private static final String TRACE_ID_KEY = "traceId";
    private static LogService logService = LogService.getInstance();
    //sucess 0,fail 1;
    int rpcStat = 0;
    String target = "";
    RpcContext parent;
    LocalContext local;
    AtomicInteger childRpcIdx;

    RpcContext(String _traceId, String _spanId) {
        this(_traceId, _spanId, null);
    }

    RpcContext(String _traceId, String _rpcId, RpcContext _parentRpc) {
        this(_traceId, _rpcId, _parentRpc, new AtomicInteger(0), new AtomicInteger(0));
    }

    RpcContext(String _traceId, String _rpcId, RpcContext _parentRpc, AtomicInteger _localIdx) {
        this(_traceId, _rpcId, _parentRpc, new AtomicInteger(0), _localIdx);
    }

    RpcContext(String _traceId, String _rpcId, RpcContext _parentRpc, AtomicInteger _childRpcIdx,
               AtomicInteger _localIdx) {
        super(_traceId, _rpcId, _localIdx);
        this.parent = _parentRpc;
        this.childRpcIdx = _childRpcIdx;
    }

    public static RpcContext get() {
        return (RpcContext) threadLocal.get();
    }

    public static void set(RpcContext ctx) {
        threadLocal.set(ctx);
    }

    public static RpcContext fromMap(Map<String, String> map) {
        return new RpcContext(map.get(TRACE_ID_KEY), map.get(SPAN_ID_KEY));
    }

    String nextChildRpcId() {
        return this.spanId + "." + this.childRpcIdx.incrementAndGet();
    }

    String nextLocalId(LocalContext tmpContext) {
        return tmpContext == null ? LOCAL_INDEX_PRE + this.localIdx.incrementAndGet()
                : tmpContext.localId + "." + tmpContext.localIdx.incrementAndGet();
    }

    String getInnerLocalId() {
        if (null == this.local) {
            return "";
        } else {
            LocalContext tmpContext = this.getLocalContext();
            return tmpContext == null ? "" : this.nextLocalId(tmpContext);
        }
    }

    public RpcContext getParentRpcContext() {
        return parent;
    }

    public RpcContext createChildRpc() {
        RpcContext parent = this;
        RpcContext ctx = new RpcContext(this.traceId, this.nextChildRpcId(), parent, this.localIdx);
        if (this.customInofos != null) {
            ctx.customInofos = new HashMap<String, String>(this.customInofos);
        }
        ctx.localId = this.getInnerLocalId();
        ctx.local = this.local;
        return ctx;
    }

    LocalContext getLocalContext() {
        LocalContext temp = this.local;
        while (null != temp && temp.isEnd()) {
            temp = temp.getLocalParent();
        }
        this.local = temp;
        return this.local;
    }

    public void startLocal(String tag) {
        LocalContext tmpContext = this.getLocalContext();
        LocalContext localCtx = new LocalContext(this.traceId, this.spanId + "." + this.childRpcIdx.get());
        localCtx.startTime = System.currentTimeMillis();
        localCtx.tag = tag;
        localCtx.localId = this.nextLocalId(tmpContext);
        localCtx.localParent = tmpContext;
        this.local = localCtx;
        logService.changeMDC();
        logService.logStage(EarthWormHelper.STAGE_START_LOCAL);
    }

    public void endLocal() {
        LocalContext tmpContext = this.local;
        if (tmpContext == null) {
            LOGGER.infoMessage("end local:has no local to end");
        } else if (tmpContext.isEnd()) {
            LOGGER.infoMessage("end local:local has ended");
        } else {
            this.local = tmpContext.localParent;
            tmpContext.setEnd(true);
            tmpContext.endTime = (int) (System.currentTimeMillis() - tmpContext.startTime);
            logService.logStage(EarthWormHelper.STAGE_END_LOCAL);
        }
        logService.changeMDC();
    }

    public void startTrace(String name) {
        this.startTime = System.currentTimeMillis();
        this.traceName = name;
        logService.changeMDC();
        logService.logStage(EarthWormHelper.STAGE_START_TRACE);
    }

    public void endTrace(String errorMsg) {
        this.errorMsg = errorMsg;
        this.endTime = (int) (System.currentTimeMillis() - this.startTime);
        logService.logStage(EarthWormHelper.STAGE_END_TRACE);
        logService.clear();
    }

    public void endTrace() {
        endTrace("");
    }

    public void startRpc(String tag) {
        this.tag = tag;
        this.startTime = System.currentTimeMillis();
        this.milestone = 0;
        logService.changeMDC();
        logService.logStage(EarthWormHelper.STAGE_START_RPC);
    }

    public void endRpc() {
        endRpc(RPC_STAT_SUCESS);
    }

    public void endRpc(int stat) {
        endRpc(stat, "", "");
    }

    public void endRpc(int stat, String errorCode, String errorMessage) {
        this.endTime = (int) (System.currentTimeMillis() - this.startTime);
        if (!StringUtil.isBlank(errorMessage)) {
            this.errorMsg = errorMessage;
            this.errorCode = errorCode;
        }
        rpcStat = stat;
        logService.logStage(EarthWormHelper.STAGE_CR);
        RpcContext.set(parent);
        logService.changeMDC();
    }

    public void rpcClientSend() {
        this.milestone = (int) (System.currentTimeMillis() - this.startTime);
        logService.logStage(EarthWormHelper.STAGE_CS);
        logService.changeMDC();
    }

    public void rpcServerRecv(String tag, Map<String, String> initData) {
        this.startTime = System.currentTimeMillis();
        this.tag = tag;
        if (initData != null) {
            target = initData.get(EarthWormHelper.PROPERTY_TARGET);
            relationId = initData.get(EarthWormHelper.PROPERTY_RELATION);
        }
        logService.changeMDC();
        logService.logStage(EarthWormHelper.STAGE_SR);
    }

    public void rpcServerSend(int stat, String errorCode, String errorMessage) {
        long currentTimeMillis = System.currentTimeMillis();
        this.endTime = (int) (currentTimeMillis - this.startTime);
        if (!StringUtil.isBlank(errorMessage)) {
            this.errorMsg = errorMessage;
            this.errorCode = errorCode;
        }
        rpcStat = stat;
        logService.logStage(EarthWormHelper.STAGE_SS, currentTimeMillis);
        logService.changeMDC();
    }

    public void rpcServerSend() {
        long currentTimeMillis = System.currentTimeMillis();
        this.endTime = (int) (currentTimeMillis - this.startTime);
        logService.logStage(EarthWormHelper.STAGE_SS, currentTimeMillis);
        logService.changeMDC();
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TRACE_ID_KEY, this.traceId);
        map.put(SPAN_ID_KEY, this.spanId);
        return map;
    }

    public RpcContext cloneInstance() {
        RpcContext clone = new RpcContext(this.traceId, this.spanId, this.parent, this.childRpcIdx, this.localIdx);
        clone.traceName = this.traceName;
        clone.tag = this.tag;
        clone.milestone = this.milestone;
        clone.endTime = this.endTime;
        clone.startTime = this.startTime;
        clone.localId = this.localId;
        clone.customInofos = this.customInofos;
        clone.local = local;
        return clone;
    }

    public Map<String, String> totalMap() {
        Map<String, String> map = super.totalMap();
        map.put("ResponseStatus", rpcStat + "");
        map.put("target", target);
        map.put("IP", target);
        return map;
    }
}
