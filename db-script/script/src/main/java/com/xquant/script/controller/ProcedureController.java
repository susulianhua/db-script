package com.xquant.script.controller;

import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.SqlBody;
import com.xquant.database.config.procedure.Procedure;
import com.xquant.database.config.procedure.ProcedureParameter;
import com.xquant.database.config.procedure.Procedures;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.otherReturn.ParameterReturn;
import com.xquant.script.pojo.otherReturn.SqlReturen;
import com.xquant.script.service.FileFromXmlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/procedure")
public class ProcedureController {

    @RequestMapping("/getSqlStore")
    @ResponseBody
    public NormalResponse getSqlStore(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getProcedureFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Procedures.class);
        Procedures procedures = (Procedures) xStream.fromXML(file);
        List<SqlReturen> sqlReturnList = new ArrayList<SqlReturen>();
        if(CollectionUtils.isEmpty(procedures.getProcedureList())){
            return new NormalResponse();
        }
        else {
            for(Procedure procedure: procedures.getProcedureList()){
                if(CollectionUtils.isEmpty(procedure.getProcedureBodyList())){
                    for(SqlBody sqlBody: procedure.getProcedureBodyList()){
                        SqlReturen sqlReturen = new SqlReturen();
                        sqlReturen.setName(procedure.getName());
                        sqlReturen.setContent(sqlBody.getContent());
                        sqlReturen.setDialectTypeName(sqlBody.getDialectTypeName());
                        sqlReturnList.add(sqlReturen);
                    }
                }
            }
        }
        return new NormalResponse(sqlReturnList, (long) sqlReturnList.size());
    }

    @RequestMapping("/getParameterStore")
    @ResponseBody
    public NormalResponse getPatameterStore(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getProcedureFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Procedures.class);
        Procedures procedures = (Procedures) xStream.fromXML(file);
        List<ParameterReturn> parameterReturnList = new ArrayList<ParameterReturn>();
        if(CollectionUtils.isEmpty(procedures.getProcedureList())){
            return new NormalResponse();
        }
        else{
            for(Procedure procedure: procedures.getProcedureList()){
                if(CollectionUtils.isEmpty(procedure.getParameterList())){
                    for(ProcedureParameter procedureParameter: procedure.getParameterList()){
                        ParameterReturn parameterReturn = new ParameterReturn();
                        parameterReturn.setName(procedure.getName());
                        parameterReturn.setStandardFieldId(procedureParameter.getStandardFieldId());
                        parameterReturn.setParameterType(procedureParameter.getParameterType().toString());
                        parameterReturnList.add(parameterReturn);
                    }
                }
            }
        }
        return new NormalResponse(parameterReturnList, (long) parameterReturnList.size());
    }


}
