package com.xquant.script.controller;

import com.xquant.database.util.DataBaseUtil;
import com.xquant.databasebuilstaller.DatabaseInstallerStart;
import com.xquant.script.pojo.MetaDataReturn.SqlResult;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.service.SqlResultResolveService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/comparison")
public class MetadataCompareController {

    @RequestMapping("/getChangeSql")
    @ResponseBody
    public NormalResponse getChangeSql(HttpServletRequest request) {
        String type = request.getParameter("type");
        List<SqlResult> sqlResultList = new ArrayList<SqlResult>();
        List<String> processSqls = new ArrayList<String>();
        if(type.equals("getChangeSql")){
            try {
                DataBaseUtil.fromSourceLocal.set("tool");
                try {
                    DatabaseInstallerStart installerStart = new DatabaseInstallerStart();
                    installerStart.bundlingDataSourceFromConfig();
                    processSqls = installerStart
                            .getChangeSqls();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }finally {
                DataBaseUtil.fromSourceLocal.remove();
            }
            sqlResultList = SqlResultResolveService.getChangeSql(processSqls);
            sqlResultList = SqlResultResolveService.sortSql(sqlResultList);
        }
        else{
            try {
                DataBaseUtil.fromSourceLocal.set("tool");
                DatabaseInstallerStart installerStart = new DatabaseInstallerStart();
                processSqls = installerStart.getFullSqls();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DataBaseUtil.fromSourceLocal.remove();
            }
            sqlResultList = SqlResultResolveService.getFullSql(processSqls);
            sqlResultList = SqlResultResolveService.sortSql(sqlResultList);
        }
        return new NormalResponse(sqlResultList, (long) sqlResultList.size());
    }

}
