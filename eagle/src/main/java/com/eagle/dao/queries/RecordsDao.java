package com.eagle.dao.queries;

import com.eagle.dao.commons.MongoConst;
import com.eagle.entity.EagleRecordEntity;
import com.eagle.entity.EagleStatusEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Achia.Rifman on 23/08/2014.
 */
@Component
public class RecordsDao extends AbstractPhaseDao<EagleRecordEntity>{

    @Autowired
    protected RecordsDao(Datastore ds) {
        super(ds);
    }




}
