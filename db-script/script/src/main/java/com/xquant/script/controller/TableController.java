package com.xquant.script.controller;

import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.table.Table;
import com.xquant.database.config.table.Tables;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.TableBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/table")
public class TableController {

    @RequestMapping("/tableBase")
    @ResponseBody
    public NormalResponse tableBase(HttpServletRequest request) throws Exception{
        String filename = request.getParameter("FileName");
        String tablename = request.getParameter("tableName");
        String package_name = filename.substring(0, filename.length() - 10);
        String filepath = this.getClass().getClassLoader().getResource("/xml/" + package_name +"/").getPath() +"/" + filename;
        File file = new File(filepath);
        InputStream is = new FileInputStream(file);
        XStream xStream = new XStream();
        xStream.processAnnotations(Tables.class);
        Tables tables = (Tables) xStream.fromXML(is);
        System.out.println("table.size:" +tables.getTableList().size());
        Table table = new Table();
        for(Table table1: tables.getTableList()){
            if(table1.getName().equals(tablename)){
                table = table1;
                break;
            }
        }
        TableBase tableBase = new TableBase(filename, tables.getPackageName(),tablename,
                table.getName(), table.getTitle(), table.getDescription());
        List<TableBase> tableBaseList = new ArrayList<TableBase>();
        tableBaseList.add(tableBase);
        return new NormalResponse(tableBaseList, (long)1 );
    }


}
