package cn.herry.util.mongoDB

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document

object MongoUtil {

    // 默认认证方式为用户名密码
    val server: MongoClient = MongoClients.create("mongodb://herry:HerryBestLegitLLL*@127.0.0.1:27017/?authMechanism=SCRAM-SHA-256")

    fun getDocuments(database: String, collectionName: String): MongoCollection<Document> {
        return server.getDatabase(database)
            .getCollection(collectionName)
    }

    // if u want to update your documents, u can use this function as well.
    fun writeDocumentToCollection(database: String, collectionName: String, document: Document) {
        server.getDatabase(database)
            .getCollection(collectionName)
            .insertOne(document)
    }

}