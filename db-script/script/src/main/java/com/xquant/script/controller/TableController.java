package com.xquant.script.controller;

import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.table.*;
import com.xquant.metadata.config.stdfield.StandardField;
import com.xquant.metadata.config.stdfield.StandardFields;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.tablereturn.*;
import com.xquant.script.service.GetCorrespondFileUtils;
import com.xquant.script.service.UpdateMetaDataUtils;
import com.xquant.script.service.UpdateModuleUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/table")
public class TableController {

    @RequestMapping("/tableBase")
    @ResponseBody
    public NormalResponse tableBase(HttpServletRequest request) {
        String moduleName = request.getParameter("moduleName");
        String tableName = request.getParameter("metadataName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        System.out.println("filePath: " + filePath);
        File file = GetCorrespondFileUtils.getTableFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Tables.class);
        Tables tables = (Tables) xStream.fromXML(file);
        Table table = new Table();
        for(Table table1: tables.getTableList()){
            if(table1.getId().equals(tableName)){
                table = table1;
                break;
            }
        }
        TableBase tableBase = new TableBase(moduleName + ".table.xml", tables.getPackageName(),tableName,
                table.getName(), table.getTitle(), table.getDescription());
        List<TableBase> tableBaseList = new ArrayList<TableBase>();
        tableBaseList.add(tableBase);
        return new NormalResponse(tableBaseList, (long)1 );
    }

    @RequestMapping("/field")
    @ResponseBody
    public NormalResponse field(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String tableName = request.getParameter("metadataName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getTableFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Tables.class);
        Tables tables = (Tables) xStream.fromXML(file);
        List<TableFieldReturn> fields = new ArrayList<TableFieldReturn>();
        for(Table table: tables.getTableList()){
            if(table.getId().equals(tableName)){
                for(TableField tableField: table.getFieldList()){
                    fields.add(new TableFieldReturn(tableField.getStandardFieldId(),tableField.getPrimary(), tableField.getUnique()
                            , tableField.getId(), tableField.getNotNull(), tableField.isAutoIncrease()));
                }
                break;
            }
        }
        return new NormalResponse(fields, (long) fields.size());
    }

    @RequestMapping("/foreign")
    @ResponseBody
    public NormalResponse foreign(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String tableName = request.getParameter("metadataName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getTableFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Tables.class);
        Tables tables = (Tables) xStream.fromXML(file);
        List<ForeignReturn> foreignReturns = new ArrayList<ForeignReturn>();
        for(Table table: tables.getTableList()){
            if(table.getId().equals(tableName)){
                for(ForeignReference foreignReference: table.getForeignReferences()){
                    foreignReturns.add(new ForeignReturn(foreignReference.getName(), foreignReference.getMainTable(),
                            foreignReference.getForeignField(), foreignReference.getReferenceField()));
                }
                break;
            }
        }
        return new NormalResponse(foreignReturns, (long) foreignReturns.size());

    }

    @RequestMapping("/stdid")
    @ResponseBody
    public NormalResponse stdid(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getStandardFieldFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(StandardFields.class);
        StandardFields standardFields = (StandardFields) xStream.fromXML(file);
        List<Stdid> stdids = new ArrayList<Stdid>();
        for(StandardField standardField: standardFields.getStandardFieldList()){
            String id = standardField.getId();
            String title = standardField.getTitle();
            String idAndTitle = id +  "(" +title + ")";
            stdids.add(new Stdid(id,idAndTitle));
        }
        return new NormalResponse(stdids, (long) stdids.size());
    }

    @RequestMapping("/index")
    @ResponseBody
    public NormalResponse index(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String tableName = request.getParameter("metadataName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getTableFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Tables.class);
        Tables tables = (Tables) xStream.fromXML(file);
        List<IndexReturn> indexList = new ArrayList<IndexReturn>();
        for(Table table: tables.getTableList()){
            if(table.getId().equals(tableName)){
                for(Index index: table.getIndexList()){
                    indexList.add(new IndexReturn(index.getName(),
                            index.getUnique().toString(), index.getDescription()));
                }
                break;
            }
        }
        return new NormalResponse(indexList, (long) indexList.size());
    }

    @RequestMapping("/indexField")
    @ResponseBody
    public NormalResponse indexField(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String tableName = request.getParameter("metadataName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getTableFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Tables.class);
        Tables tables = (Tables) xStream.fromXML(file);
        List<IndexFieldReturn> indexFieldReturns = new ArrayList<IndexFieldReturn>();
        for(Table table: tables.getTableList()){
            if(table.getId().equals(tableName)){
                for(Index index: table.getIndexList()){
                    for(IndexField indexField: index.getFields()){
                        indexFieldReturns.add(new IndexFieldReturn(indexField.getField(),
                                index.getName(), indexField.getDirection()));
                    }
                }
                break;
            }
        }
        return new NormalResponse(indexFieldReturns, (long) indexFieldReturns.size());
    }

    @RequestMapping("/tableSave")
    @ResponseBody
    public NormalResponse tableSave(@RequestBody Table table){
        String moduleName = table.getPackageName();
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getTableFile(moduleName, filePath);

        XStream xStream = new XStream();
        xStream.processAnnotations(Tables.class);
        Tables tables = (Tables) xStream.fromXML(file);
        int flag = 0;
        for(int i = 0; i < tables.getTableList().size(); i++){
            if(tables.getTableList().get(i).getId().equals(table.getId())){
                table.setPackageName(null);
                tables.getTableList().set(i,table);
                flag = 1;
                break;
            }
        }
        if(flag == 0) tables.getTableList().add(table);
        String xml = xStream.toXML(tables);
        UpdateMetaDataUtils.objectToFile(xml, file);
        return new NormalResponse();
    }

}
