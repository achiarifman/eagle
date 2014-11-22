package com.eagle.dao.queries;

import com.eagle.dao.commons.MongoConst;
import com.eagle.entity.EagleStatusEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Achia.Rifman on 23/08/2014.
 */
@Component
public class StatusDao extends AbstractDao<EagleStatusEntity> {


    @Autowired
    protected StatusDao(Datastore ds) {
        super(ds);
    }


    public void updateFinishedPhase(ObjectId statusId, ObjectId phaseId){

        //update(queryFindById(statusId).filter(MongoConst.ENTITIES_IDS + MongoConst.DOT + MongoConst.ID, phaseId), createUpdateOperations().set(MongoConst.ENTITIES_IDS + MongoConst.DOLLAR_APPENDER + MongoConst.IS_FINISHED,true));
    }





}
