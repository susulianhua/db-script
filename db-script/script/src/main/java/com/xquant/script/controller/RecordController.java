package com.xquant.script.controller;

import com.xquant.script.pojo.RecordResult;
import com.xquant.script.pojo.RecordReturn;
import com.xquant.script.service.impl.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/record")
public class RecordController {
    @Autowired
    RecordService recordService;

    @RequestMapping("/getAll")
    @ResponseBody
    public List<RecordReturn> getAllRecordResult(){
        List<RecordResult> result = recordService.getAllRecords();
        List<RecordReturn> recordReturns = new LinkedList<RecordReturn>();
        for(RecordResult result1: result){
            RecordReturn recordReturn = new RecordReturn(result1);
            recordReturns.add(recordReturn);
        }
        return recordReturns;
    }

    @RequestMapping("/select")
    @ResponseBody
    public List<RecordReturn> selectRecord(HttpServletRequest request){
        String str = request.getParameter("bookname");
        List<RecordResult> result = recordService.getRecordByBookName(str);
        List<RecordReturn> recordReturns = new LinkedList<RecordReturn>();
        for(RecordResult result1: result){
            RecordReturn recordReturn = new RecordReturn(result1);
            recordReturns.add(recordReturn);
        }
        return recordReturns;
    }
}
