package com.eagle.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by Achia.Rifman on 04/06/2014.
 */
@Configuration
public class MongoDaoBootStrap {

    @Value("${mongo.host}")
    String mongoHost;

    @Value("${mongo.port}")
    int mongoPort;

    @Value("${mongo.morphia.package}")
    String morphiaPackage;

    @Value("${mongo.username}")
    String username;

    @Value("${mongo.password}")
    String password;

    @Value("${mongo.users.db}")
    String usersDB;

    @Value("${mongo.auth}")
    boolean auth;

    @Bean(name = "mongo")
    public MongoClient mongoClient() {

        MongoClient mongo;
        try {

            MongoCredential credential = MongoCredential.createMongoCRCredential(username, usersDB, password.toCharArray());
            if(auth)
                mongo = new MongoClient(new ServerAddress(mongoHost, mongoPort), Arrays.asList(credential));
            else
                mongo = new MongoClient(new ServerAddress(mongoHost, mongoPort));
        } catch (UnknownHostException e) {
            throw new BeanCreationException(e.toString(), e);
        }

        return mongo;
    }

    @Bean(name = "morphia")
    @DependsOn("mongo")
    public Morphia morphia() {

        Morphia morphia = new Morphia();
        //morphia.mapPackage("com.eagle");
        //morphia.mapPackage(morphiaPackage);
        return morphia;

    }
}
