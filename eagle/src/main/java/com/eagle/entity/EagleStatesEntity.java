package com.eagle.entity;

import com.eagle.commons.AbstractPhase;
import com.eagle.dao.commons.CollectionConst;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

/**
 * Created by Achia.Rifman on 22/08/2014.
 */
@Entity(CollectionConst.STATE)
public class EagleStatesEntity {

    @Id
    private ObjectId objectId;

    List<AbstractPhase> states;




}
