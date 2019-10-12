package com.xquant.script.dao;

import com.xquant.script.pojo.Record;
import com.xquant.script.pojo.RecordResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordMapper {

    List<RecordResult> getAllRecords();

    Record getRecordBySno(@Param("sno") String Sno);

    List<RecordResult> getRecordByBookName(@Param("book_name") String book_name);

    int updateState(@Param("state") int state, @Param("book_id") int book_id);

    int addRecord(Record record);

    int deleteRecord(@Param("id") int id);

}
