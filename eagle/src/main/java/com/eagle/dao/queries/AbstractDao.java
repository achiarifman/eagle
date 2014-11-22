package com.eagle.dao.queries;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Achia.Rifman on 30/08/2014.
 */
@Component
public abstract class AbstractDao<T> extends BasicDAO<T,ObjectId> {

    @Autowired
    protected AbstractDao(Datastore ds) {
        super(ds);
    }

    public Query<T> queryFindById(ObjectId objectId){

        return createQuery().field(Mapper.ID_KEY).equal(objectId);
    }
}
