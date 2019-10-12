package com.xquant.script.service.impl;

import com.xquant.script.pojo.Record;
import com.xquant.script.pojo.RecordResult;

import java.util.List;

public interface RecordService {
    List<RecordResult> getAllRecords();

    Record getRecordBySno(String sno);

    List<RecordResult> getRecordByBookName(String book_name);

    int updateState(int state, int book_id);

    int addRecord(Record record);

    int deleteRecord(int id);
}
