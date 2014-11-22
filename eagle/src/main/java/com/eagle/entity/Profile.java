package com.eagle.entity;

import com.eagle.consts.AUDIO_CODEC;
import com.eagle.consts.VIDEO_CODEC;
import com.eagle.consts.VIDEO_CONTAINER;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonProperty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Achia.Rifman on 28/06/2014.
 */
@Entity("profiles")
public class Profile {

    @Id
    @JsonProperty("id")
    ObjectId id;

    int width;
    int height;
    VIDEO_CONTAINER videoContainer;
    VIDEO_CODEC videoCodecs;
    AUDIO_CODEC audioCodec;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public VIDEO_CONTAINER getVideoContainer() {
        return videoContainer;
    }

    public void setVideoContainer(VIDEO_CONTAINER videoContainer) {
        this.videoContainer = videoContainer;
    }

    public VIDEO_CODEC getVideoCodecs() {
        return videoCodecs;
    }

    public void setVideoCodecs(VIDEO_CODEC videoCodecs) {
        this.videoCodecs = videoCodecs;
    }

    public AUDIO_CODEC getAudioCodec() {
        return audioCodec;
    }

    public void setAudioCodec(AUDIO_CODEC audioCodec) {
        this.audioCodec = audioCodec;
    }
}
