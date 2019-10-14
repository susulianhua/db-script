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

import org.tinygroup.earthworm.impl.CloseSampleTrier;
import org.tinygroup.earthworm.util.EarthWormHelper;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.Map;

public class EarthWorm {
    private static final String ROOT_SPAN_ID = "0";
    private static final String MAL_ROOT_SPAN_ID = "0";
    private static LogService logService = LogService.getInstance();
    private static Logger LOGGER = LoggerFactory.getLogger(EarthWorm.class);
    private static SampleTrier sampleTrier = new CloseSampleTrier();
    ;

    protected static SampleTrier getSampleTrier() {
//		if (sampleTrier == null) {
//			LOGGER.warnMessage("sampleTrier is Null use CloseSampleTrier");
//			sampleTrier = new CloseSampleTrier();
//		}
        return sampleTrier;
    }

    public static void setSampleTrier(String sampleTrierClass) {
        try {
            sampleTrier = (SampleTrier) Class.forName(sampleTrierClass)
                    .newInstance();
            LOGGER.infoMessage("using sampleTrierClass class:{0}", sampleTrierClass);
        } catch (Exception e) {
            LOGGER.warnMessage("set sampleTrierClass exception.class:{0},reason:{1}",
                    sampleTrierClass, e.getClass().getName());
        }

    }

    public static void startTrace(String traceName) {
        startTrace(null, null, traceName);
    }

    public static void startTrace(String traceId, String traceName) {
        startTrace(traceId, null, traceName);
    }

    public static void startTrace(String traceId, String spanId,
                                  String traceName) {
        if (traceName != null) {
            traceId = EarthWormHelper.trim(traceId);
            if (!EarthWormHelper.validTraceId(traceId)) {
                traceId = EarthWormHelper.generate();
                spanId = ROOT_SPAN_ID;
            } else if (!EarthWormHelper.validSpanId(spanId)) {
                spanId = ROOT_SPAN_ID;
            }

            RpcContext ctx = RpcContext.get();
            if (ctx != null && ctx.traceId != null) {
                if (ctx.traceId.equals(traceId)
                        && traceName.equals(ctx.traceName)) {
                    return;
                }
                endTrace();
            }
            try {
                ctx = new RpcContext(traceId, spanId);
                RpcContext.set(ctx);
                if (!getSampleTrier().check(traceId)) {
                    return;
                }
                ctx.startTrace(traceName);
            } catch (Throwable t) {
                LOGGER.errorMessage(
                        "startTrace exception,traceId:{},spanId:{},traceName:{}",
                        t, traceId, spanId, traceName);
            }
        } else {
            LOGGER.infoMessage("trace name为空，不开启trace");
        }
    }

    public static void endTrace() {
        endTrace("");
    }

    public static void endTrace(String errorMsg) {
        try {
            RpcContext re = RpcContext.get();
            if (null == re) {
                return;
            }
            if (!checkSample(re)) {
                return;
            }
            while (null != re.parent) {
                re = re.parent;
            }
            re.endTrace(errorMsg);
        } finally {
            clearRpcContext();
        }
    }

    public static void startRpc(String tag) {
        RpcContext re = RpcContext.get();
        RpcContext childCtx;
        if (null == re) {
            childCtx = new RpcContext(EarthWormHelper.generate(),
                    MAL_ROOT_SPAN_ID);
        } else {
            childCtx = re.createChildRpc();
        }
        //clientRecv的时候没有调用到后面的RpcContext.set(parent)无问题
        //因为在这里的时候，如过不取样，没有set()
        //如过这里调整成不取样也set，那么后面需要同步修改
        //已修改
        RpcContext.set(childCtx);
        if (!checkSample(childCtx)) {
            return;
        }
        childCtx.startRpc(tag);
    }

    public static boolean checkSample(RpcContext childCtx) {
        return getSampleTrier().check(childCtx.traceId);
    }

    public static void startLocal(String tag) {
        RpcContext re = RpcContext.get();
        if (null == re) {
            return;
        }
        if (!checkSample(re)) {
            return;
        }
        RpcContext cloneCtx = re.cloneInstance();
        RpcContext.set(cloneCtx);
        cloneCtx.startLocal(tag);
    }

    public static void endLocal() {
        RpcContext re = RpcContext.get();
        if (null == re) {
            return;
        }
        if (!checkSample(re)) {
            return;
        }
        re.endLocal();
    }

    public static void rpcClientSend() {
        RpcContext re = RpcContext.get();
        if (null == re) {
            return;
        }
        if (!checkSample(re)) {
            return;
        }
        re.rpcClientSend();
    }

    public static void rpcClientRecv() {
        rpcClientRecv(RpcContext.RPC_STAT_SUCESS, "", "");
    }

    public static void rpcClientRecvFail(String errorMessage) {
        rpcClientRecv(RpcContext.RPC_STAT_FAIL, "", errorMessage);

    }

    public static void rpcClientRecvFail(String errorCode, String errorMessage) {
        rpcClientRecv(RpcContext.RPC_STAT_FAIL, errorCode, errorMessage);

    }

    private static void rpcClientRecv(int stat, String errorCode, String errorMessage) {
        RpcContext re = RpcContext.get();
        if (null == re) {
            return;
        }
        //这里没有调用到后面的RpcContext.set(parent),但是没问题
        //因为startRpc的时候，如过不取样，没有set()
        //已修改
        if (!checkSample(re)) {
            RpcContext.set(re.parent);
            return;
        }
        re.endRpc(stat, errorCode, errorMessage);

    }

    public static void rpcServerRecv(String tag, Map<String, String> initData) {
        rpcServerRecv(tag, initData, true);
    }

    public static void rpcServerRecv(String tag, Map<String, String> initData, boolean setToThreadLocal) {
        RpcContext re = createContextIfNotExists(setToThreadLocal);
        if (!checkSample(re)) {
            return;
        }
        re.rpcServerRecv(tag, initData);

    }

    public static void rpcServerSend() {
        rpcServerSend(RpcContext.RPC_STAT_SUCESS, "", "");
    }

    public static void rpcServerSendFail(String errorMessage) {
        rpcServerSend(RpcContext.RPC_STAT_FAIL, "", errorMessage);
    }

    public static void rpcServerSendFail(String errorCode, String errorMessage) {
        rpcServerSend(RpcContext.RPC_STAT_FAIL, errorCode, errorMessage);
    }

    private static void rpcServerSend(int stat, String errorCode, String errorMessage) {
        try {
            RpcContext re = RpcContext.get();
            if (re != null) {
                if (!checkSample(re)) {
                    return;
                }
                re.rpcServerSend(stat, errorCode, errorMessage);
            }
        } finally {
            clearRpcContext();
        }

    }

    public static String getTraceId() {
        RpcContext ctx = RpcContext.get();
        return null == ctx ? null : ctx.traceId;
    }

    public static String getSpanId() {
        RpcContext ctx = RpcContext.get();
        return null == ctx ? null : ctx.spanId;
    }

    public static String getLocalId() {
        RpcContext ctx = RpcContext.get();
        return null == ctx ? "" : ctx.localId;
    }

    public static Object currentRpcContext() {
        RpcContext re = RpcContext.get();
        if (null != re) {
            return re.toMap();
        }
        return null;
    }

    public static RpcContext getRpcContext() {
        return RpcContext.get();
    }


    public static RpcContext createRootRpcContext(String traceId, String rpcId) {
        if (traceId == null) {
            traceId = EarthWormHelper.generate();
        }

        if (rpcId == null) {
            rpcId = MAL_ROOT_SPAN_ID;
        }

        return new RpcContext(traceId, rpcId);
    }

    public static RpcContext createRpcContextFromMap(Map<String, String> map) {
        return RpcContext.fromMap(map);
    }

    static final RpcContext createContextIfNotExists(boolean setToThreadLocal) {
        RpcContext ctx = RpcContext.get();
        if (null == ctx) {
            RpcContext newCtx = new RpcContext(EarthWormHelper.generate(),
                    MAL_ROOT_SPAN_ID);
            if (setToThreadLocal) {
                RpcContext.set(newCtx);
            }
            return newCtx;
        } else {
            return ctx;
        }
    }

    @SuppressWarnings("unchecked")
    public static void setRpcContext(Object rpcCtx) {
        try {
            RpcContext re = null;
            if (rpcCtx instanceof Map) {
                re = RpcContext.fromMap((Map<String, String>) rpcCtx);
            } else if (rpcCtx instanceof RpcContext) {
                re = (RpcContext) rpcCtx;
            }

            RpcContext.set(re);
        } catch (Throwable t) {
            LOGGER.errorMessage("set RpcContext exception", t);
        }
    }

    public static void clearRpcContext() {
        RpcContext.set(null);
        logService.clear();
    }

    public static void target(String target) {
        RpcContext context = RpcContext.get();
        if (context != null) {
            context.target = target;
        }

    }

    public static void relateion(String relationId) {
        RpcContext context = RpcContext.get();
        if (context != null) {
            context.relationId = relationId;
        }

    }

    public static boolean checkContextExist() {
        return !(getRpcContext() == null);
    }
}
