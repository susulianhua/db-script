package com.xquant.script.controller;

import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.procedure.Procedure;
import com.xquant.database.config.procedure.Procedures;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.saveWithModuleName.ProcedureWithModuleName;
import com.xquant.script.service.GetCorrespondFileUtils;
import com.xquant.script.service.UpdateMetaDataUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
        File file = GetCorrespondFileUtils.getProcedureFile(moduleName, filePath);
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
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getProcedureFile(moduleName, filePath);
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

    @RequestMapping("/saveProcedure")
    @ResponseBody
    public NormalResponse saveProcedure(@RequestBody ProcedureWithModuleName procedureWithModuleName){
        Procedure procedure = procedureWithModuleName.getProcedure();
        String moduleName = procedureWithModuleName.getModuleName();
        String procedureName = procedure.getName();
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getProcedureFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Procedures.class);
        int flag = 0;
        Procedures procedures = (Procedures) xStream.fromXML(file);
        for(Procedure procedure1: procedures.getProcedureList()){
            if(procedure1.getName().equals(procedureName)){
                procedures.getProcedureList().remove(procedure1);
                procedures.getProcedureList().add(procedure);
                flag = 1;
                break;
            }
        }
        if(flag == 0) procedures.getProcedureList().add(procedure);
        String xml = xStream.toXML(procedures);
        UpdateMetaDataUtils.objectToFile(xml, file);
        return new NormalResponse();
    }



}
