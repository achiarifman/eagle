package com.eagle.dao.queries;

import com.eagle.dao.commons.MongoConst;
import com.eagle.entity.EagleBaseEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Component;

/**
 * Created by Achia.Rifman on 30/08/2014.
 */
@Component
public abstract class AbstractPhaseDao<T extends EagleBaseEntity> extends AbstractDao<T> {



    protected AbstractPhaseDao(Datastore ds) {
        super(ds);
    }



    public void updateFinishedPhase(ObjectId phaseId){

        update(queryFindById(phaseId),
                createUpdateOperations().set(MongoConst.IS_FINISHED,true));
    }



}
