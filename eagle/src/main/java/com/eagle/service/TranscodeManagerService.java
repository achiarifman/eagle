package com.eagle.service;

import com.eagle.commons.*;
import com.eagle.consts.FFMPEG_CMD;
import com.eagle.consts.VIDEO_CODEC;
import com.eagle.dao.queries.RecordsDao;
import com.eagle.dao.queries.StatusDao;
import com.eagle.dao.queries.UploadDao;
import com.eagle.entity.EagleBaseEntity;
import com.eagle.entity.EagleRecordEntity;
import com.eagle.entity.EagleStatusEntity;
import com.eagle.entity.EagleUploadEntity;
import com.eagle.ffmpeg.FFmpegJob;
import ffmpeg.FFmpegRecorder;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import service.ActorsService;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Achia.Rifman on 20/06/2014.
 */
@Service
public class TranscodeManagerService {

    Logger LOGGER = LoggerFactory.getLogger(TranscodeManagerService.class);

    private static ExecutorService executor;

    private static Future<Boolean> currentFutureTask;

    @Autowired
    FtpManagerService ftpManagerService;

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    RecordsDao recordsDao;

    @Autowired
    StatusDao statusDao;

    @Autowired
    UploadDao uploadDao;

    @Autowired
    ActorsService actorsService;

    @PostConstruct
    protected void init() {
        executor = Executors.newFixedThreadPool(1);
    }

    public TranscodeManagerService() {
        LOGGER.info("Initializing the TranscodeManagerService");
    }

    public void cancelCurrentJob(){
        if (currentFutureTask != null) {
            currentFutureTask.cancel(true);
        }
    }


    public void addJob(EagleRecordEntity eagleRecordEntity){

        //ExecutorService executor = Executors.newFixedThreadPool(1);
        FFmpegJob ffmpegJob = new FFmpegJob();
        String input = eagleRecordEntity.getUrl();
        int time = eagleRecordEntity.getDuration();
        String ouput = " C:\\final-proj\\output\\"+System.currentTimeMillis() + "_out.mp4";
        ffmpegJob.appendPair(FFMPEG_CMD.INPUT,input);
        ffmpegJob.appendPair(FFMPEG_CMD.TIME_LIMIT,String.valueOf(time));
        ffmpegJob.appendPair(FFMPEG_CMD.VIDEO_CODEC, VIDEO_CODEC.H_264);
        //abstractFFmpegJob.appendPair(FFMPEG_CMD.AUDIO_CODEC, AUDIO_CODECS.COPY);
        ffmpegJob.appendParam(ouput);
        currentFutureTask = executor.submit(ffmpegJob);
        try {
            boolean flag = currentFutureTask.get();
            System.out.println("Finished " + flag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //executor.shutdown();

    }

    public EagleRecordEntity initialNewRecordJob(EagleRecordEntity eagleRecordEntity){

        recordsDao.save(eagleRecordEntity);
        actorsService.initialJob(eagleRecordEntity);
        return eagleRecordEntity;
    }


    public boolean isFree(){
        if(currentFutureTask == null || currentFutureTask.isDone())
            return true;
        return false;
    }
}
