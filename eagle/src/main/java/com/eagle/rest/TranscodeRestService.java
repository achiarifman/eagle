package com.eagle.rest;

import com.eagle.entity.Record;
import com.eagle.mgmt.TranscodeManagerService;
import com.sun.jersey.api.core.InjectParam;

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
    public String recordChannel(Record record){

            transcodeManagerService.addJob(record);
            return "{status : \"started\"}";
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


}
