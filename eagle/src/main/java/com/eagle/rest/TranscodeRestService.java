package com.eagle.rest;

import com.eagle.entity.EagleRecordEntity;
import com.eagle.service.TranscodeManagerService;
import com.sun.jersey.api.core.InjectParam;
import com.eagle.dao.entity.BaseJobEntity;
import service.JobsService;

import javax.ws.rs.*;

/**
 * Created by Achia.Rifman on 20/06/2014.
 */
@Path("transcode")
@Consumes("application/json")
@Produces("application/json")
public class TranscodeRestService extends RestApplication{

    @InjectParam
    TranscodeManagerService transcodeManagerService;

    @POST
    @Path("channel")
    public String recordChannel(EagleRecordEntity eagleRecordEntity){
        JobsService.createAndPersistNewJob(eagleRecordEntity);
        return "TEST";
       //return transcodeManagerService.initialNewRecordJob(eagleRecordEntity);
    }

    @GET
    @Path("status")
    public String getTranscoderStatus(){


        if(transcodeManagerService.isFree()){
            return "{status : \"idle\"}";
        }
        else
            return "{status : \"busy\"}";
    }

    @GET
    @Path("cancel")
    public String cancelCurrentTranscodingJob(){

        transcodeManagerService.cancelCurrentJob();
        return "{status : \"canceled\"}";

    }

    @POST
    @Path("test")
    public String test(BaseJobEntity baseJobEntity){
        System.out.println(baseJobEntity.name());
        return baseJobEntity.name();
    }


}
