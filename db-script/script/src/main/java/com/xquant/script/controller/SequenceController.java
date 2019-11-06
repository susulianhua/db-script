package com.xquant.script.controller;

import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.sequence.SeqCacheConfig;
import com.xquant.database.config.sequence.Sequence;
import com.xquant.database.config.sequence.Sequences;
import com.xquant.database.config.sequence.ValueConfig;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.otherReturn.SequenceFormReturn;
import com.xquant.script.pojo.saveWithModuleName.SequenceWithModuleName;
import com.xquant.script.service.GetCorrespondFileUtils;
import com.xquant.script.service.UpdateMetaDataUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sequence")
public class SequenceController {

    @RequestMapping("/getSequenceForm")
    @ResponseBody
    public NormalResponse getSequenceForm(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String sequenceName = request.getParameter("sequenceName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getSequenceFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Sequences.class);
        Sequences sequences = (Sequences) xStream.fromXML(file);
        SequenceFormReturn sequenceFormReturn = new SequenceFormReturn();
        sequenceFormReturn.setModuleName(moduleName);
        sequenceFormReturn.setName(sequenceName);
        for(Sequence sequence: sequences.getSequences()){
            if(sequence.getName().equals(sequenceName)){
                sequenceFormReturn.setCycle(sequence.isCycle());
                sequenceFormReturn.setOrder(sequence.isOrder());
                sequenceFormReturn.setDataType(sequence.getDataType());
                sequenceFormReturn.setIncrementBy(sequence.getIncrementBy());
                sequenceFormReturn.setStartWith(sequence.getStartWith());
                break;
            }
        }
        List<SequenceFormReturn> sFR = new ArrayList<SequenceFormReturn>();
        sFR.add(sequenceFormReturn);
        return new NormalResponse(sFR, (long) sFR.size());
    }

    @RequestMapping("/getValueConfigForm")
    @ResponseBody
    public NormalResponse getValueConfigForm(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String sequenceName = request.getParameter("sequenceName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getSequenceFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Sequences.class);
        Sequences sequences = (Sequences) xStream.fromXML(file);
        ValueConfig valueConfig = new ValueConfig();
        for(Sequence sequence: sequences.getSequences()){
            if(sequence.getName().equals(sequenceName)){
                valueConfig = sequence.getValueConfig();
                break;
            }
        }
        List<ValueConfig> valueConfigList = new ArrayList<ValueConfig>();
        valueConfigList.add(valueConfig);
        return new NormalResponse(valueConfigList, (long) 1);
    }

    @RequestMapping("/getSeqCacheConfigForm")
    @ResponseBody
    public NormalResponse getSeqCacheConfigForm(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String sequenceName = request.getParameter("sequenceName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getSequenceFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Sequences.class);
        Sequences sequences = (Sequences) xStream.fromXML(file);
        SeqCacheConfig seqCacheConfig = new SeqCacheConfig();
        for(Sequence sequence: sequences.getSequences()){
            if(sequence.getName().equals(sequenceName)){
                seqCacheConfig = sequence.getSeqCacheConfig();
                break;
            }
        }
        List<SeqCacheConfig> seqCacheConfigList = new ArrayList<SeqCacheConfig>();
        seqCacheConfigList.add(seqCacheConfig);
        return new NormalResponse(seqCacheConfigList, (long) 1);
    }

    @RequestMapping("/saveSequence")
    @ResponseBody
    public NormalResponse saveSequence(@RequestBody SequenceWithModuleName sequenceWithModuleName){
        Sequence sequence = sequenceWithModuleName.getSequence();
        String moduleName = sequenceWithModuleName.getModuleName();
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getSequenceFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Sequences.class);
        int flag = 0;
        Sequences sequences = (Sequences) xStream.fromXML(file);
        for(Sequence sequence1: sequences.getSequences()){
            if(sequence1.getName().equals(sequence.getName())){
                sequences.getSequences().remove(sequence1);
                sequences.getSequences().add(sequence);
                flag = 1;
                break;
            }
        }
        if(flag == 0) sequences.getSequences().add(sequence);
        String xml = xStream.toXML(sequences);
        UpdateMetaDataUtils.objectToFile(xml, file);
        return new NormalResponse();
    }
}
