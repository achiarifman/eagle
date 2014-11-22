package com.eagle.jobs;

import com.eagle.service.FtpManagerService;

/**
 * Created by Achia.Rifman on 28/06/2014.
 */
public class OutputHandlerJob extends AbstractJob{



    FtpManagerService ftpManagerService;

    public OutputHandlerJob(FtpManagerService ftpManagerService) {
        this.ftpManagerService = ftpManagerService;
    }

    @Override
    public Boolean call() throws Exception {



        return null;
    }


    @Override
    public String getJobName() {
        return "OutputUpload job";
    }
}
