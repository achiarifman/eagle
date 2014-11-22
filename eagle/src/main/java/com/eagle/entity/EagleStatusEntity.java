package com.eagle.entity;

import com.eagle.dao.commons.CollectionConst;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Achia.Rifman on 23/08/2014.
 */
@Entity(CollectionConst.STATUSES)
public class EagleStatusEntity  extends EagleBaseEntity{


    List<EagleBaseEntity> entitiesIds;



    public void addEagleEntityId(EagleBaseEntity id){

        if(entitiesIds == null){
            entitiesIds = new ArrayList<EagleBaseEntity>();
        }
        entitiesIds.add(id);

    }

    public List<EagleBaseEntity> getBaseEntityList() {
        return entitiesIds;
    }

    public void setBaseEntityList(List<EagleBaseEntity> baseEntityList) {
        this.entitiesIds = baseEntityList;
    }


    public void setFinishedPhase(ObjectId id){
        for(EagleBaseEntity entity : entitiesIds){

           if(entity.getId().equals(id)) {
               entity.setFinished(true);
               break;
           }
        }
    }
}
