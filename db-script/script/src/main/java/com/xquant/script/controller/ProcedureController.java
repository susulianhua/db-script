package com.xquant.script.controller;

import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.procedure.Procedure;
import com.xquant.database.config.procedure.Procedures;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.service.FileFromXmlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Controller
@RequestMapping("/procedure")
public class ProcedureController {

    @RequestMapping("/getSqlStore")
    @ResponseBody
    public NormalResponse getSqlStore(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String procedureName = request.getParameter("procedureName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getProcedureFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Procedures.class);
        Procedures procedures = (Procedures) xStream.fromXML(file);
        for(Procedure procedure: procedures.getProcedureList()){
            if(procedure.getName().equals(procedureName) &&
                    !CollectionUtils.isEmpty(procedure.getProcedureBodyList())){
                    return new NormalResponse(procedure.getProcedureBodyList(),
                            (long) procedure.getProcedureBodyList().size());
                }
            }
        return  new NormalResponse();
    }

    @RequestMapping("/getParameterStore")
    @ResponseBody
    public NormalResponse getParameterStore(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String procedureName = request.getParameter("procedureName");
        System.out.println("moduleName:" + moduleName);
        System.out.println("procedureName:" + procedureName);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getProcedureFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Procedures.class);
        Procedures procedures = (Procedures) xStream.fromXML(file);
        for(Procedure procedure: procedures.getProcedureList()){
            if(procedure.getName().equals(procedureName) &&
                    !CollectionUtils.isEmpty(procedure.getParameterList())){
                return new NormalResponse(procedure.getParameterList(),
                        (long) procedure.getParameterList().size());
            }
        }
        return new NormalResponse();
    }



}
