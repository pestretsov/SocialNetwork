package dao.h2;

import com.mongodb.MongoClient;

/**
 * Created by artemypestretsov on 8/17/16.
 */
public class MongoDBImageDAO {
    MongoClient mongoClient;

    public MongoDBImageDAO(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }



}
