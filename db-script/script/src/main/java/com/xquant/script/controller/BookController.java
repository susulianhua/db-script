package com.xquant.script.controller;

import com.xquant.script.pojo.*;
import com.xquant.script.service.impl.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("/select")
    @ResponseBody
    public NormalResponse selectBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Book book = bookService.getBookById(2);
        BookReturn bookReturn = new BookReturn(book);
        List<BookReturn> bookReturns = new LinkedList<BookReturn>();
        bookReturns.add(bookReturn);
        NormalResponse result = new NormalResponse(bookReturns,(long)1);
        return result;
    }

    @RequestMapping("foreign")
    @ResponseBody
    public NormalResponse foreign(){
        Foreign foreign = new Foreign("sf","da","fa","fda");
        Foreign foreign1 = new Foreign("fa","fa","dad","fda");
        List<Foreign> foreigns = new LinkedList<Foreign>();
        foreigns.add(foreign);
        foreigns.add(foreign1);
        return new NormalResponse(foreigns,(long)2);
    }

    @RequestMapping("field")
    @ResponseBody
    public NormalResponse field(){
        TableField tableField1 = new TableField("v_id", true,
                true, "id", true, false);
        TableField tableField2 = new TableField("name", false,
                true, "name", false, true);
        TableField tableField3 = new TableField("o_id", false,
                true, "13", true, true);
        List<TableField> tableFields = new LinkedList<TableField>();
        tableFields.add(tableField1);
        tableFields.add(tableField2);
        tableFields.add(tableField3);
        return new NormalResponse(tableFields, (long)3);
    }

    @RequestMapping("/indexField")
    @ResponseBody
    public NormalResponse indexField(){
        IndexField indexField = new IndexField("lijl", "xixixi", "desc");
        IndexField indexField1 = new IndexField("afda", "xixixi", "asc");
        IndexField indexField2 = new IndexField("fada", "string", "asc");
        IndexField indexField3 = new IndexField("jfoa", "string", "asc");
        List<IndexField> indexFields = new ArrayList<IndexField>();
        indexFields.add(indexField);
        indexFields.add(indexField1);
        indexFields.add(indexField2);
        indexFields.add(indexField3);
        return new NormalResponse(indexFields,(long)4);
    };

    @RequestMapping("stdid")
    @ResponseBody
    public NormalResponse stdid(){
        List<Stdid> stdids = new ArrayList<Stdid>();
        stdids.add(new Stdid("v_id", "v_id"));
        stdids.add(new Stdid("o_id","o_id"));
        stdids.add(new Stdid("hj_id", "hj_id"));
        return new NormalResponse(stdids, (long)3);
    }

    @RequestMapping("/tableSave")
    @ResponseBody
    public void save(@RequestBody TableReturn tableReturn){
        TableBase table = tableReturn.getTablebase();
        try{
            File file = new File("E:/tiny/" + table.getFileName());
            File moduleFile = new File("xml/module.xml");

            String[] strs =  table.getFileName().split("\\.");
            String moduleName = strs[0];

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            if(!file.exists()){
                Document document = db.newDocument();
                document.setXmlStandalone(true);
                Element root = document.createElement("tables");
                root.setAttribute("package-name", table.getPackage_name());
                Element tableXml = document.createElement("table");
                tableXml.setAttribute("id", table.getTable_id());
                tableXml.setAttribute("name", table.getTable_name());
                tableXml.setAttribute("title", table.getTable_title());
                Element descriptionXml = document.createElement("description");
                descriptionXml.setTextContent(table.getTable_description());
                tableXml.appendChild(descriptionXml);
                for(TableField tableField: tableReturn.getTableFieldList()){
                    Element tableFieldXml = document.createElement("table-field");
                    tableFieldXml.setAttribute("standard-field-id",tableField.getStandardFieldId());
                    tableFieldXml.setAttribute("primary", "" + tableField.getPrimary());
                    tableFieldXml.setAttribute("unique", "" + tableField.getUnique());
                    tableFieldXml.setAttribute("not-null", "" + tableField.getNotNull());
                    tableFieldXml.setAttribute("id", tableField.getId());
                    tableXml.appendChild(tableFieldXml);
                };
                for(Foreign foreign: tableReturn.getForeignReferences()){
                    Element foreignXml = document.createElement("foreign-reference");
                    foreignXml.setAttribute("name", foreign.getKey_name());
                    foreignXml.setAttribute("foreign-field", foreign.getForeign_field());
                    foreignXml.setAttribute("main-table", foreign.getMain_table());
                    foreignXml.setAttribute("reference-id", foreign.getReference_field());
                    tableXml.appendChild(foreignXml);
                }
                for(Index index: tableReturn.getIndexList()){
                    Element indexXml = document.createElement("index");
                    indexXml.setAttribute("name", index.getIndex_name());
                    indexXml.setAttribute("unique", index.getIndex_unique());
                    Element indexDesciptionXml = document.createElement("description");
                    indexDesciptionXml.setTextContent(index.getIndex_description());
                    indexXml.appendChild(indexDesciptionXml);
                    for(IndexField indexField: tableReturn.getIndexFieldList()){
                        Element indexFieldXml = document.createElement("index-field");
                        if(indexField.getIndex_name().equals(index.getIndex_name())){
                            indexFieldXml.setAttribute("field", indexField.getField());
                            indexFieldXml.setAttribute("direction", indexField.getDirection());
                            indexXml.appendChild(indexFieldXml);
                        }
                    };
                    tableXml.appendChild(indexXml);
                }
                root.appendChild(tableXml);
                document.appendChild(root);
                TransformerFactory tff = TransformerFactory.newInstance();
                Transformer tf = tff.newTransformer();
                tf.setOutputProperty(OutputKeys.INDENT, "yes");
                tf.transform(new DOMSource(document), new StreamResult(tableReturn.getTablebase().getFileName()));
            }
            //文件存在，还需判断表格是否存在
            else{
                Document document = db.parse(file);
                Document document1 = db.parse(moduleFile);
                Element root1 = document1.getDocumentElement();
                NodeList modulelist = root1.getElementsByTagName("module");
                Element root = document.getDocumentElement();
                NodeList tablelist = root.getElementsByTagName("table");
                int flag =0;
                for(int i = 0; i < tablelist.getLength(); i++){
                    //表格存在，则为修改表格内容，删除原表格增加表格内容为新
                    if(tablelist.item(i).getAttributes().item(0).getNodeValue().equals(table.getTable_id())){
                        //删除对应表格
                        Node tableXml1 = tablelist.item(i);
                        root.removeChild(tableXml1);

                        //新增对应表格
                        Element tableXml = document.createElement("table");
                        tableXml.setAttribute("id", table.getTable_id());
                        tableXml.setAttribute("name", table.getTable_name());
                        tableXml.setAttribute("title", table.getTable_title());
                        Element descriptionXml = document.createElement("description");
                        descriptionXml.setTextContent(table.getTable_description());
                        root.appendChild(tableXml);
                        tableXml.appendChild(descriptionXml);
                        for(TableField tableField: tableReturn.getTableFieldList()){
                            Element tableFieldXml = document.createElement("table-field");
                            tableFieldXml.setAttribute("standard-field-id",tableField.getStandardFieldId());
                            tableFieldXml.setAttribute("primary", "" + tableField.getPrimary());
                            tableFieldXml.setAttribute("unique", "" + tableField.getUnique());
                            tableFieldXml.setAttribute("not-null", "" + tableField.getNotNull());
                            tableFieldXml.setAttribute("id", tableField.getId());
                            tableXml.appendChild(tableFieldXml);
                        };
                        for(Foreign foreign: tableReturn.getForeignReferences()){
                            Element foreignXml = document.createElement("foreign-reference");
                            foreignXml.setAttribute("name", foreign.getKey_name());
                            foreignXml.setAttribute("foreign-field", foreign.getForeign_field());
                            foreignXml.setAttribute("main-table", foreign.getMain_table());
                            foreignXml.setAttribute("reference-id", foreign.getReference_field());
                            tableXml.appendChild(foreignXml);
                        }
                        for(Index index: tableReturn.getIndexList()){
                            Element indexXml = document.createElement("index");
                            indexXml.setAttribute("name", index.getIndex_name());
                            indexXml.setAttribute("unique", index.getIndex_unique());
                            Element indexDesciptionXml = document.createElement("description");
                            indexDesciptionXml.setTextContent(index.getIndex_description());
                            indexXml.appendChild(indexDesciptionXml);
                            for(IndexField indexField: tableReturn.getIndexFieldList()){
                                Element indexFieldXml = document.createElement("index-field");
                                if(indexField.getIndex_name().equals(index.getIndex_name())){
                                    indexFieldXml.setAttribute("field", indexField.getField());
                                    indexFieldXml.setAttribute("direction", indexField.getDirection());
                                    indexXml.appendChild(indexFieldXml);
                                }
                            };
                            tableXml.appendChild(indexXml);
                        }
                        TransformerFactory tff = TransformerFactory.newInstance();
                        Transformer tf = tff.newTransformer();
                        tf.setOutputProperty(OutputKeys.INDENT, "yes");
                        tf.transform(new DOMSource(document), new StreamResult(tableReturn.getTablebase().getFileName()));
                        flag = 1;
                        break;

                    }
                }
                //表格不存在，直接新增
                if(flag == 0){
                    for(int i = 0; i < modulelist.getLength(); i++){
                        if(modulelist.item(i).getAttributes().item(0).getNodeValue().equals(moduleName)){
                            Element tableXml = document1.createElement("table");
                            tableXml.setTextContent(table.getTable_name());
                            modulelist.item(i).appendChild(tableXml);
                            TransformerFactory tff = TransformerFactory.newInstance();
                            Transformer tf = tff.newTransformer();
                            tf.setOutputProperty(OutputKeys.INDENT, "yes");
                            tf.transform(new DOMSource(document1), new StreamResult("module.xml"));
                        }
                    }
                    Element tableXml = document.createElement("table");
                    tableXml.setAttribute("id", table.getTable_id());
                    tableXml.setAttribute("name", table.getTable_name());
                    tableXml.setAttribute("title", table.getTable_title());
                    Element descriptionXml = document.createElement("description");
                    descriptionXml.setTextContent(table.getTable_description());
                    root.appendChild(tableXml);
                    tableXml.appendChild(descriptionXml);
                    for(TableField tableField: tableReturn.getTableFieldList()){
                        Element tableFieldXml = document.createElement("table-field");
                        tableFieldXml.setAttribute("standard-field-id",tableField.getStandardFieldId());
                        tableFieldXml.setAttribute("primary", "" + tableField.getPrimary());
                        tableFieldXml.setAttribute("unique", "" + tableField.getUnique());
                        tableFieldXml.setAttribute("not-null", "" + tableField.getNotNull());
                        tableFieldXml.setAttribute("id", tableField.getId());
                        tableXml.appendChild(tableFieldXml);
                    };
                    for(Foreign foreign: tableReturn.getForeignReferences()){
                        Element foreignXml = document.createElement("foreign-reference");
                        foreignXml.setAttribute("name", foreign.getKey_name());
                        foreignXml.setAttribute("foreign-field", foreign.getForeign_field());
                        foreignXml.setAttribute("main-table", foreign.getMain_table());
                        foreignXml.setAttribute("reference-id", foreign.getReference_field());
                        tableXml.appendChild(foreignXml);
                    }
                    for(Index index: tableReturn.getIndexList()){
                        Element indexXml = document.createElement("index");
                        indexXml.setAttribute("name", index.getIndex_name());
                        indexXml.setAttribute("unique", index.getIndex_unique());
                        Element indexDesciptionXml = document.createElement("description");
                        indexDesciptionXml.setTextContent(index.getIndex_description());
                        indexXml.appendChild(indexDesciptionXml);
                        for(IndexField indexField: tableReturn.getIndexFieldList()){
                            Element indexFieldXml = document.createElement("index-field");
                            if(indexField.getIndex_name().equals(index.getIndex_name())){
                                indexFieldXml.setAttribute("field", indexField.getField());
                                indexFieldXml.setAttribute("direction", indexField.getDirection());
                                indexXml.appendChild(indexFieldXml);
                            }
                        };
                        tableXml.appendChild(indexXml);
                    }
                    TransformerFactory tff = TransformerFactory.newInstance();
                    Transformer tf = tff.newTransformer();
                    tf.setOutputProperty(OutputKeys.INDENT, "yes");
                    tf.transform(new DOMSource(document), new StreamResult(tableReturn.getTablebase().getFileName()));
                }
            }



        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成book1.xml失败");
        }

    }


    @RequestMapping("/table")
    @ResponseBody
    public NormalResponse table(){
        TableBase table = new TableBase("crud.table.xml", "sample", "t_user", "t_user",
                "用户表", "用户表");
        List<TableBase> tables = new LinkedList<TableBase>();
        tables.add(table);
        NormalResponse normalResponse = new NormalResponse(tables,(long)1);
        return normalResponse;
    }
    @RequestMapping("/index")
    @ResponseBody
    public NormalResponse qqq(HttpServletRequest request){
        String str = "string";
        String str1 = "xixixi";
        Index index = new Index();
        index.setIndex_description(str);
        index.setIndex_name(str);
        index.setIndex_unique(str);
        Index index1 = new Index();
        index1.setIndex_unique(str1);
        index1.setIndex_name(str1);
        index1.setIndex_description(str1);
        List<Index> indexList = new LinkedList<Index>();
        indexList.add(index);
        indexList.add(index1);
        return new NormalResponse(indexList, (long)2);
    }

    @RequestMapping("/add")
    @ResponseBody
    public NormalResponse addBook(HttpServletRequest request){
        String bookname = request.getParameter("book_name");
        String bookid = request.getParameter("book_id");
        int bookid1 = Integer.parseInt(bookid);
        String author = request.getParameter("author");
        String description = request.getParameter("description");
        String price = request.getParameter("price");
        int price1 = Integer.parseInt(price);
        String state = request.getParameter("state");
        int state1 = state.equals("1")?1:0;
        String date = request.getParameter("publish_time");
        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        try{
            date1 = simpleDateFormat.parse(date);
        }catch (ParseException px){
            px.printStackTrace();
        }
        Book book = new Book();
        book.setauthor(author);
        book.setBook_name(bookname);
        book.setBook_id(bookid1);
        book.setPrice(price1);
        book.setdescription(description);
        book.setState(state1);
        book.setPublish_time(date1);
        int t = bookService.addBook(book);
        return new NormalResponse(t);

    }

    @RequestMapping("/getAll")
    @ResponseBody
    public Object getAll(HttpServletRequest request, HttpServletResponse response) throws IOException{
        int start = Integer.parseInt(request.getParameter("start"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        String query = request.getParameter("query");
        if(query == null){
        List<Book> books = bookService.getAllBooks(start, start + limit);
        List<BookReturn> result = new LinkedList<BookReturn>();
        for(Book book:books){
            BookReturn bookReturn = new BookReturn(book);
            result.add(bookReturn);
        }
        long total = bookService.getBookCount();
        return new NormalResponse(result, total);}
        else{
            List<Book> books = bookService.getBookByName(query);
            List<BookReturn> result = new LinkedList<BookReturn>();
            for(Book book: books){
                BookReturn bookReturn = new BookReturn(book);
                result.add(bookReturn);
            }
            return new NormalResponse(result, (long)books.size());
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Response deleteBook(@RequestBody List<BookId> list) {
        for (BookId m : list) {
            int n = m.getBookid();
            bookService.deleteBook(n);
        }
        NormalResponse response = new NormalResponse();
        return response;
    }

    @RequestMapping("/update")
    @ResponseBody
    public Response updateBook(HttpServletRequest request){
        String bookname = request.getParameter("book_name");
        int bookid = Integer.parseInt(request.getParameter("book_id"));
        String author = request.getParameter("author");
        String description = request.getParameter("description");
        int price = Integer.parseInt(request.getParameter("price"));
        int state = Integer.parseInt(request.getParameter("state"));
        String date = request.getParameter("publish_time");
        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        try{
            date1 = simpleDateFormat.parse(date);
        }catch (ParseException px){
            px.printStackTrace();
        }
        Book book = new Book();
        book.setauthor(author);
        book.setBook_name(bookname);
        book.setBook_id(bookid);
        book.setPrice(price);
        book.setdescription(description);
        book.setState(state);
        book.setPublish_time(date1);
        NormalResponse response = new NormalResponse();
            bookService.updateBook(book);
        return response;


    }
}
