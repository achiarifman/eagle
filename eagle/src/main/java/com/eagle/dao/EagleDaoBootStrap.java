package com.eagle.dao;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by Achia.Rifman on 04/06/2014.
 */
@Configuration
public class EagleDaoBootStrap {

    @Autowired
    Morphia morphia;

    @Autowired
    MongoClient mongoClient;


    @Value("${mongo.db}")
    String dbName;

    @Bean
    @DependsOn("morphia")
    public Datastore datastore(){

        Datastore datastore = morphia.createDatastore(mongoClient, dbName);
        //morphia.mapPackage("com.eagle");
        datastore.ensureIndexes();
        return datastore;
    }

}
