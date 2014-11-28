package com.eagle.entity;

import com.eagle.consts.PHASES;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.eagle.dao.entity.FailedEntity;
import org.codehaus.jackson.annotate.JsonProperty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;

import java.nio.file.Path;
import java.util.Set;

/**
 * Created by Achia.Rifman on 20/06/2014.
 */
@Entity("records")
public class EagleRecordEntity extends EagleBaseEntity {




    @JsonProperty("url")
    private String url;

    @JsonProperty("duration")
    private int duration;

    private String channelName;

    private String programId;

    private String outputFolder;

    private boolean failed;

    private FailedEntity failedEntity;

    private String callBackUrl;

    @Transient
    @JsonIgnore
    private Path outPutFolderPath;

    int numberOfAds;

    Set<String> adsPaths;

    public EagleRecordEntity() {
        setPhase(PHASES.RECORD);
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("duration")
    public int getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed, FailedEntity failedEntity) {
        this.failed = failed;
        this.failedEntity = failedEntity;
    }

    public Path getOutPutFolderPath() {
        return outPutFolderPath;
    }

    public void setOutPutFolderPath(Path outPutFolderPath) {
        this.outPutFolderPath = outPutFolderPath;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public Set<String> getAdsPaths() {
        return adsPaths;
    }

    public void setAdsPaths(Set<String> adsPaths) {
        this.adsPaths = adsPaths;
    }

    public int getNumberOfAds() {
        return numberOfAds;
    }

    public void setNumberOfAds(int numberOfAds) {
        this.numberOfAds = numberOfAds;
    }
}
