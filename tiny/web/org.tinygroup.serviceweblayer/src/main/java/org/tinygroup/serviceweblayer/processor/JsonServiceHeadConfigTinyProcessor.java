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
package org.tinygroup.serviceweblayer.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.serviceweblayer.json.JsonSysHead;
import org.tinygroup.weblayer.AbstractTinyProcessor;
import org.tinygroup.weblayer.WebContext;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonServiceHeadConfigTinyProcessor extends AbstractTinyProcessor {

    public final static String BEAN_NAME = "jsonServiceHeadConfigTinyProcessor";
    private static Map<String, Class<?>> basicClasses = new HashMap<String, Class<?>>();

    static {
        basicClasses.put("int", int.class);
        basicClasses.put("double", double.class);
        basicClasses.put("float", float.class);
        basicClasses.put("short", float.class);
        basicClasses.put("boolean", boolean.class);
        basicClasses.put("char", char.class);
        basicClasses.put("byte", byte.class);
    }

    private CEPCore core;

    public CEPCore getCore() {
        return core;
    }

    public void setCore(CEPCore core) {
        this.core = core;
    }

    @Override
    protected void customInit() throws ServletException {

    }

    @Override
    public void reallyProcess(String urlString, WebContext context)
            throws ServletException, IOException {
        byte[] data = StreamUtil.readBytes(
                context.getRequest().getInputStream(), true).toByteArray();
        String characterEncoding = context.getRequest().getCharacterEncoding();
        String inputJson = new String(data, characterEncoding);
        String resultJson = process(inputJson);
        context.getResponse().getOutputStream()
                .write(resultJson.getBytes(characterEncoding));
    }

    public String process(String inputJson) {
        JSONObject jsonObject = JSON.parseObject(inputJson, JSONObject.class);
        // 获取service相关信息
        JsonSysHead syshead = JSON.parseObject(jsonObject.get("head").toString(), JsonSysHead.class);
        String serviceId = syshead.getSyshead().get("serviceid");
        ServiceInfo serviceInfo = core.getServiceInfo(serviceId);
        //获取自定义对象信息
//		Object o = null;
//		try {
//			o = JSON.parseObject(head.toString(),BeanContainerFactory.getBeanContainer(
//					this.getClass().getClassLoader()).getBean(
//					ConfigurationUtil.getConfigurationManager().getConfiguration(
//							"SERVICE_WEBLAYER_HEAD")).getClass());
//		} catch (Exception e) {
//			throw new RuntimeException("未找到配置的head类", e);
//		}
        String body = jsonObject.getString("body");
        // 解析参数，放入context
        Context context = ContextFactory.getContext();
        JSONObject jb = new JSONObject();
        String errorCode = "";
        String errorInfo = "交易成功";
        try {
            processParam(body, serviceInfo, context);
            Event e = Event.createEvent(serviceId, context);
            core.process(e);
            dealResult(serviceInfo, e, jb);
        } catch (BaseRuntimeException e) {
            // TODO：这里getErrorCode可能会是null
            errorCode = e.getErrorCode().toString();
            errorInfo = e.getMessage();
            syshead.getSyshead().put("errorCode", errorCode);
            syshead.getSyshead().put("errorInfo", errorInfo);
        } catch (Exception e) {
            // TODO
            errorCode = "未定义的errrCode";
            errorInfo = e.getMessage();
            syshead.getSyshead().put("errorCode", errorCode);
            syshead.getSyshead().put("errorInfo", errorInfo);
        }

        Map<String, Object> head = new HashMap<String, Object>();
        head.put("bizhead", jsonObject.get("body"));
        head.put("syshead", syshead.getSyshead());
        jb.put("head", head);
        return jb.toString();
    }

    private void dealResult(ServiceInfo serviceInfo, Event e, JSONObject jsonObject) {
        List<Parameter> results = serviceInfo.getResults();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        for (Parameter p : results) {
            resultMap.put(p.getName(),
                    e.getServiceRequest().getContext().get(p.getName()));
        }
        jsonObject.put("body", resultMap);
    }

    private void processParam(String body, ServiceInfo serviceInfo,
                              Context context) {

        List<Parameter> list = serviceInfo.getParameters();
        JSONObject object = JSON.parseObject(body);
        for (Parameter p : list) {
            String name = p.getName();
            String type = p.getType();
            boolean isCollection = !StringUtil.isBlank(p.getCollectionType());
            boolean isArray = p.isArray();
            boolean isRequired = p.isRequired();
            Class<?> clazz = getClass(type);
            if (isRequired && !object.containsKey(name)) {
                // throw 必传参数未传
            } else if (!isRequired && !object.containsKey(name)) {
                continue;
            }

            if (isArray) {
                parseArray(context, object, name, clazz);
            } else if (isCollection) {
                parseCollection(context, object, name, clazz,
                        p.getCollectionType());
            } else {
                parseObject(context, object, name, clazz);
            }

        }
    }

    private void parseObject(Context context, JSONObject value, String name,
                             Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (clazz == int.class) {
                context.put(name, value.getIntValue(name));
            } else if (clazz == double.class) {
                context.put(name, value.getDoubleValue(name));
            } else if (clazz == long.class) {
                context.put(name, value.getLongValue(name));
            } else if (clazz == short.class) {
                context.put(name, value.getShortValue(name));
            } else if (clazz == byte.class) {
                context.put(name, value.getByteValue(name));
            } else if (clazz == char.class) {
                context.put(name, value.getString(name));
            } else if (clazz == boolean.class) {
                context.put(name, value.getBooleanValue(name));
            } else {
                context.put(name, value.getString(name));
            }
        } else {
            // String jsonString = value.getJSONObject(name).toJSONString();
            String jsonString = JSON.toJSONString(value.get(name));
            context.put(name, JSON.parseObject(jsonString, clazz));
        }
    }

    private void parseCollection(Context context, JSONObject value,
                                 String name, Class<?> clazz, String collectionType) {
        // context.put(name, JSON.parseArray(value.get(name).toString(),
        // clazz));
        // TODO list 转换为具体的collectionType

        String jsonString = JSON.toJSONString(value.get(name));
        context.put(name, JSON.parseArray(jsonString, clazz));
    }

    private void parseArray(Context context, JSONObject value, String name,
                            Class<?> clazz) {
        // 获取数组类
        Class<?> clazzArray = Array.newInstance(clazz, 0).getClass();
        String valueString = value.getString(name);
        // 解析，并放入上下文
        context.put(name, JSON.parseObject(valueString, clazzArray));
    }

    private Class<?> getClass(String type) {
        try {
            if (basicClasses.containsKey(type)) {
                return basicClasses.get(type);
            }
            return Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到对应类" + type, e);
        }
    }

}
