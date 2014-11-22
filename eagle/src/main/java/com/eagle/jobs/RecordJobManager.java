package com.eagle.jobs;

import com.eagle.consts.FFMPEG_CMD;
import com.eagle.consts.VIDEO_CODEC;
import com.eagle.entity.EagleRecordEntity;
import com.eagle.ffmpeg.FFmpegJob;
import com.eagle.service.FtpManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Achia.Rifman on 28/06/2014.
 */

public class RecordJobManager extends AbstractJob{


    Logger LOGGER = LoggerFactory.getLogger(RecordJobManager.class);

    EagleRecordEntity eagleRecordEntity;
    FtpManagerService ftpManagerService;
    ExecutorService executor;

    public RecordJobManager(EagleRecordEntity eagleRecordEntity, FtpManagerService ftpManagerService) {
        this.eagleRecordEntity = eagleRecordEntity;
        this.ftpManagerService = ftpManagerService;
    }

    @Override
    public Boolean call() throws Exception {
        executor = Executors.newFixedThreadPool(1);
        List<AbstractJob> jobsList = initialJobs();
        for(AbstractJob job : jobsList){
            Future<Boolean> future = executor.submit(job);
            if (future.get()) {
                LOGGER.info(job.getJobName() + " was finished successfully");
            }
            else{
                LOGGER.info(job.getJobName() + " was failed, going to cancel the record job");
                return false;
            }
        }


        return true;
    }

    private List<AbstractJob> initialJobs(){

        List<AbstractJob> jobs = new ArrayList<AbstractJob>();
        FFmpegJob fFmpegJob = buildFFmpegJob();
        jobs.add(fFmpegJob);
        OutputHandlerJob outputHandlerJob = new OutputHandlerJob(ftpManagerService);
        jobs.add(outputHandlerJob);
        return jobs;
    }

    private FFmpegJob buildFFmpegJob(){

        FFmpegJob ffmpegJob = new FFmpegJob();
        String input = eagleRecordEntity.getUrl();
        int time = eagleRecordEntity.getDuration();
        String ouput = " C:\\final-proj\\output\\"+System.currentTimeMillis() + "_out.mp4";
        ffmpegJob.appendPair(FFMPEG_CMD.INPUT,input);
        ffmpegJob.appendPair(FFMPEG_CMD.TIME_LIMIT,String.valueOf(time));
        ffmpegJob.appendPair(FFMPEG_CMD.VIDEO_CODEC, VIDEO_CODEC.H_264);
        //abstractFFmpegJob.appendPair(FFMPEG_CMD.AUDIO_CODEC, AUDIO_CODECS.COPY);
        ffmpegJob.appendParam(ouput);
        return ffmpegJob;
    }

    @Override
    public String getJobName() {
        return "Record job";
    }
}
