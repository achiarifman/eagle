package com.eagle.entity;

import com.eagle.consts.PHASES;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonProperty;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Achia.Rifman on 23/08/2014.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EagleBaseEntity {

    @Id
    @JsonProperty("id")
    private ObjectId id;

    public PHASES getPhase() {
        return phase;
    }

    public void setPhase(PHASES phase) {
        this.phase = phase;
    }

    private PHASES phase;

    @JsonIgnore
    public ObjectId getId() {
        return id;
    }



    public void setId(ObjectId id) {
        this.id = id;
    }

    @JsonGetter("id")
    public String getObjectIdAsString(){
        return id.toString();
    }

    boolean isFinished;

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }
}
