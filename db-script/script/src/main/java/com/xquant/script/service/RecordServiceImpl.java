package com.xquant.script.service;

import com.xquant.script.dao.RecordMapper;
import com.xquant.script.pojo.Record;
import com.xquant.script.pojo.RecordResult;
import com.xquant.script.service.impl.RecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("recordService")
public class RecordServiceImpl implements RecordService {
    @Resource
    RecordMapper recordMapper;

    public List<RecordResult> getAllRecords(){
        return recordMapper.getAllRecords();
    };

    public Record getRecordBySno(String Sno){
        return recordMapper.getRecordBySno(Sno);
    };

    public List<RecordResult> getRecordByBookName(String book_name){
        return recordMapper.getRecordByBookName(book_name);
    };

    public int updateState(int state,int book_id){
        return recordMapper.updateState(state,book_id);
    };

    public int addRecord(Record record){
        return recordMapper.addRecord(record);
    };

    public int deleteRecord(int id){
        return recordMapper.deleteRecord(id);
    };
}
