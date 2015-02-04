package com.eagle.dao.entity;

/**
 * Created by Achia.Rifman on 30/01/2015.
 */
public class CallBackEntity {

    String jobId;
    String status;

    public CallBackEntity(String jobId, String status) {
        this.jobId = jobId;
        this.status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
