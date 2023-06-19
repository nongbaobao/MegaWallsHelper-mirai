package cn.herry.util.mongoDB

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document

object MongoUtil {

    val server: MongoClient = MongoClients.create("mongodb://herry:BestLegitLLL*@114.132.185.201:23333/?authMechanism=SCRAM-SHA-256")

    fun getCollection(database: String, collection: String): MongoCollection<Document> {
        return server.getDatabase(database).getCollection(collection)
    }

}