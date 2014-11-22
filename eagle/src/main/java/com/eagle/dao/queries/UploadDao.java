package com.eagle.dao.queries;

import com.eagle.entity.EagleRecordEntity;
import com.eagle.entity.EagleUploadEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Achia.Rifman on 23/08/2014.
 */
@Component
public class UploadDao extends AbstractPhaseDao<EagleUploadEntity>{


    @Autowired
    protected UploadDao(Datastore ds) {
        super(ds);
    }
}
