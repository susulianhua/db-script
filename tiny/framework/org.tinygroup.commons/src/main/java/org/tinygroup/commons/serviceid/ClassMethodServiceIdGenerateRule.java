package org.tinygroup.commons.serviceid;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassMethodServiceIdGenerateRule implements ServiceIdGenerateRule {

    Map<String,List<String>> overloadsMap = new HashMap<String, List<String>>();

    @Override
    public String generateServiceId(Class type, Method method) {
        return type.getName()+"."+method.getName()+computePostStr(type,method);
    }

    protected String computePostStr(Class type, Method method){
        List<String> overloads = getOverLoadMethods(type);
        String postStr = "";
        if(overloads!=null){
            Class<?>[] parameterTypes = method.getParameterTypes();
            for(Class clazz:parameterTypes){
                if(clazz.isArray()){
                    postStr = "_" + clazz.getComponentType()+"_";//如果是数组，在后面加一个"_"替代[]
                }else{
                    //不考虑hell(a.b.c.User u) 与 hell(c.d.e.User u)这种极小概率冲突的场景
                    postStr = "_" + clazz.getSimpleName();
                }
            }
        }
        return postStr;
    }

    private List<String> getOverLoadMethods(Class clazz){
        if(!overloadsMap.containsKey(clazz.getName())){
            List<String> list = computeOverLoadMethods(clazz);
            if(list.size()==0){
                overloadsMap.put(clazz.getName(),null);
            }else {
                overloadsMap.put(clazz.getName(),list);
            }
        }
        return overloadsMap.get(clazz.getName());
    }

    private List<String> computeOverLoadMethods(Class clazz) {
        Method[] methods = null;
        if(clazz.isInterface()) {
            methods = clazz.getMethods();
        }
        List<String> allNames = new ArrayList<String>();
        List<String> overloads = new ArrayList<String>();
        methods = clazz.getMethods();
        for(Method m:methods) {
            if (Object.class.getName().equals(m.getDeclaringClass().getName())) {
                continue;
            }
            String methodName = m.getName();
            if(allNames.contains(methodName)){
                overloads.add(methodName);
            }else{
                allNames.add(methodName);
            }
        }
        return overloads;
    }
}
