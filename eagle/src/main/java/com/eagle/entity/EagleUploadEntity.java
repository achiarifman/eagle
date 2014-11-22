package com.eagle.entity;

import com.eagle.consts.PHASES;
import com.eagle.dao.commons.CollectionConst;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;


import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Achia.Rifman on 23/08/2014.
 */
@Entity(CollectionConst.UPLOADS)
public class EagleUploadEntity extends EagleBaseEntity{




    //@Embedded
    Set<ObjectId> jobId;

    @Transient
    Path uploadFolder;

    public EagleUploadEntity() {
        setPhase(PHASES.UPLOAD);
    }

    public Path getUploadFolder() {
        return uploadFolder;
    }

    public void setUploadFolder(Path uploadFolder) {
        this.uploadFolder = uploadFolder;
    }

    public ObjectId getJobId() {
        return jobId.iterator().next();
    }

    public void setJobId(ObjectId jobId) {

         this.jobId = new HashSet<ObjectId>();
        this.jobId.add(jobId);
    }


}
