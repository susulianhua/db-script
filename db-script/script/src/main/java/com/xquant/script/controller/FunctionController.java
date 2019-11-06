package com.xquant.script.controller;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.xquant.dialectfunction.DialectFunction;
import com.xquant.dialectfunction.DialectFunctions;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.otherReturn.FunctionFormReturn;
import com.xquant.script.pojo.saveWithModuleName.FunctionWithModuleName;
import com.xquant.script.service.GetCorrespondFileUtils;
import com.xquant.script.service.UpdateMetaDataUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/function")
public class FunctionController {

    @RequestMapping("/getDialectFunctionForm")
    @ResponseBody
    public  NormalResponse getDialectFunctionForm(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String functionName = request.getParameter("functionName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getFunctionFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(DialectFunctions.class);
        DialectFunctions dialectFunctions = (DialectFunctions) xStream.fromXML(file);
        FunctionFormReturn functionFormReturn = new FunctionFormReturn();
        functionFormReturn.setModuleName(moduleName);
        functionFormReturn.setName(functionName);
        for(DialectFunction dialectFunction: dialectFunctions.getFunctions()){
            if(dialectFunction.getName().equals(functionName)){
                functionFormReturn.setDesc(dialectFunction.getDesc());
                functionFormReturn.setFormat(dialectFunction.getFormat());
            }
        }
        List<FunctionFormReturn> functionFormReturnList = new ArrayList<FunctionFormReturn>();
        functionFormReturnList.add(functionFormReturn);
        return new NormalResponse(functionFormReturnList, (long)1);
    }

    @RequestMapping("/getDialectStore")
    @ResponseBody
    public NormalResponse getDialectStore(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String functionName = request.getParameter("functionName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getFunctionFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(DialectFunctions.class);
        DialectFunctions dialectFunctions = (DialectFunctions) xStream.fromXML(file);
        for(DialectFunction dialectFunction: dialectFunctions.getFunctions()){
            if(dialectFunction.getName().equals(functionName)){
                return new NormalResponse(dialectFunction.getDialects(),
                        (long) dialectFunction.getDialects().size());
            }
        }
        return new NormalResponse();
    }

    @RequestMapping("/saveFunction")
    @ResponseBody
    public NormalResponse saveFunction(@RequestBody FunctionWithModuleName functionWithModuleName){
        String moduleName = functionWithModuleName.getModuleName();
        DialectFunction dialectFunction = functionWithModuleName.getDialectFunction();
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getFunctionFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(DialectFunctions.class);
        int flag = 0;
        DialectFunctions dialectFunctions = (DialectFunctions) xStream.fromXML(file);
        for(DialectFunction dialectFunction1: dialectFunctions.getFunctions()){
            if(dialectFunction1.getName().equals(dialectFunction.getName())){
                dialectFunctions.getFunctions().remove(dialectFunction1);
                dialectFunctions.getFunctions().add(dialectFunction);
                flag = 1;
                break;
            }
        }
        if(flag == 0){
            dialectFunctions.getFunctions().add(dialectFunction);
        }
        String xml = xStream.toXML(dialectFunctions);
        UpdateMetaDataUtils.objectToFile(xml, file);
        return new NormalResponse();
    }
}
