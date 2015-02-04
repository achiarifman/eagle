package com.eagle.rest;

import com.eagle.consts.RestPath;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Achia.Rifman on 30/01/2015.
 */
@Path(RestPath.MOCK)
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class MockRestService extends RestApplication{

    @POST
    @Path(RestPath.CALLBACK)
    public Response transcoderMockCallBack(String json){

        System.out.println(json);
        return Response.ok().build();
    }
}
