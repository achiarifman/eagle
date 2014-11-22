package com.eagle.commons;

import com.eagle.consts.FFMPEG_CMD;
import com.eagle.consts.FFmpegConst;
import com.eagle.consts.PHASES;
import com.eagle.consts.VIDEO_CODEC;
import com.eagle.entity.EagleRecordEntity;
import com.eagle.ffmpeg.FFmpegJob;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Achia.Rifman on 22/08/2014.
 */
@Component(BeansNames.RECORD_PHASE)
@Scope("prototype")
public class RecordPhase extends AbstractPhase {


    @Autowired
    FFmpegJob ffmpegJob;


    private ExecutorService executor;

    private Future<Boolean> currentFutureTask;

    EagleRecordEntity eagleRecordEntity;

    @Value("${record.output}")
    String OUTPUT_FOLDER;

    String outPutFolderName;
    Path outPutFolderPath;



    @PostConstruct
    protected void init() {
        executor = Executors.newFixedThreadPool(1);
        setPHASE_NAME(PHASES.RECORD);

    }

    @Override
    public Boolean call() throws Exception {
        currentFutureTask = executor.submit(ffmpegJob);
        boolean flag = currentFutureTask.get();
        return flag;
    }

    public EagleRecordEntity getEagleRecordEntity() {
        return eagleRecordEntity;
    }

    public void setEagleRecordEntity(EagleRecordEntity eagleRecordEntity) {
        this.eagleRecordEntity = eagleRecordEntity;
    }

    public void initialFFmpegJob(){

        ffmpegJob = new FFmpegJob();
        ffmpegJob.appendPair(FFMPEG_CMD.INPUT, eagleRecordEntity.getUrl());
        ffmpegJob.appendPair(FFMPEG_CMD.TIME_LIMIT,String.valueOf(eagleRecordEntity.getDuration()));
        ffmpegJob.appendPair(FFMPEG_CMD.VIDEO_CODEC, VIDEO_CODEC.H_264);
        //setOutPutFolder(Paths.get(eagleRecordEntity.getId().toString()));
        setOutPutFolderName(eagleRecordEntity.getId().toString());
        createOutputFolder(OUTPUT_FOLDER + File.separator + eagleRecordEntity.getId().toString());
        ffmpegJob.appendParam(FFmpegConst.SPACE + outPutFolderPath + File.separator + eagleRecordEntity.getId().toString() + FFmpegConst.UNDERSCORE + eagleRecordEntity.getChannelName() + ".mp4");

    }

    public void setOutPutFolder(Path folder){
        outPutFolderPath = folder;
    }

    public Path getOutPutFolder() {
        return outPutFolderPath;
    }

    public String getOutPutFolderName() {
        return outPutFolderName;
    }

    public void setOutPutFolderName(String outPutFolderName) {
        this.outPutFolderName = outPutFolderName;
    }

    public void createOutputFolder(String folderName){

        new File(folderName).mkdir();
        setOutPutFolder(Paths.get(folderName));
    }

    @Override
    public ObjectId getPhaseId() {
        return eagleRecordEntity.getId();
    }
}
