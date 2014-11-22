package com.eagle.commons;

import com.eagle.consts.PHASES;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;

import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * Created by Achia.Rifman on 22/08/2014.
 */

public abstract class AbstractPhase implements Callable<Boolean> {


    PHASES PHASE_NAME;


    //public abstract Boolean call() throws Exception;

    public PHASES getPHASE_NAME() {
        return PHASE_NAME;
    }

    public void setPHASE_NAME(PHASES PHASE_NAME) {
        this.PHASE_NAME = PHASE_NAME;
    }

    public abstract ObjectId getPhaseId();


}

