package cn.herry.util.mongoDB

import cn.herry.util.hypixel.MegaWallsUtil
import com.mongodb.client.*
import org.bson.Document
import java.time.LocalDate

object MongoUtil {

    val client: MongoClient = MongoClients.create("mongodb://username:pwd@127.0.0.1:23333/")
    private val database = client.getDatabase("hypixel")
    private val usersCollection = client.getDatabase("hypixel").getCollection("users")

    fun addDataByFirst(data: PlayerData) {
        val addDocument = Document("date", data.date)
            .append("name", data.name)
            .append("finalKills", data.finalKills)
            .append("finalDeaths", data.finalDeaths)
            .append("finalAssists", data.finalAssists)
            .append("wins", data.wins)
            .append("losses", data.losses)

        database.createCollection(data.name)
        database.getCollection(data.name).insertOne(addDocument)
    }

    fun getUsedUser(): FindIterable<Document> {
        return usersCollection.find()
    }

    fun updateData(data: PlayerData) {
        val document = Document("\$set", Document("date", data.date))
            .append("\$set", Document("name", data.name))
            .append("\$set", Document("finalKills", data.finalKills))
            .append("\$set", Document("finalDeaths", data.finalDeaths))
            .append("\$set", Document("finalAssists", data.finalAssists))
            .append("\$set", Document("wins", data.wins))
            .append("\$set", Document("losses", data.losses))

        val dataCollection = database.getCollection(data.name)
        val iterator = dataCollection.find().iterator()
        while (iterator.hasNext()) {
            val dm = iterator.next()
            if (dm["user"] as String == data.name && dm["date"] as String == data.date) {
                dataCollection.updateMany(dm, document)
            }
        }
    }

    fun getDataByDate(name: String, date: String): PlayerData? {
        val dataCollection = database.getCollection(name)
        val iterator = dataCollection.find().iterator()
        while (iterator.hasNext()) {
            val dm: Document = iterator.next()
            if (dm["name"] as String == name && dm["date"] as String == date)
                return PlayerData(
                    dm["date"] as String,
                    dm["name"] as String,
                    dm["finalKills"] as Int,
                    dm["finalDeaths"] as Int,
                    dm["finalAssists"] as Int,
                    dm["wins"] as Int,
                    dm["losses"] as Int
                )
        }

        return null
    }

    fun hasDaily(name: String): Boolean {
        val dataCollection = database.getCollection(name)
        val iterator = dataCollection.find().iterator()
        val date = LocalDate.now().minusDays(1).toString()
        while (iterator.hasNext()) {
            val value = iterator.next()
            if (value["name"] == null || value["date"] == null)
                return false
            if (value["name"] as String == name && value["date"] as String == date) {
                return true
            }
        }
        return false
    }

    fun hasWeekly(name: String): Boolean {
        val dataCollection = database.getCollection(name)
        val iterator = dataCollection.find().iterator()
        val date = LocalDate.now().minusWeeks(1).toString()
        while (iterator.hasNext()) {
            val value = iterator.next()
            if (value["name"] == null || value["date"] == null)
                return false
            if (value["name"] as String == name && value["date"] as String == date) {
                return true
            }
        }
        return false
    }

    fun hasMonthly(name: String): Boolean {
        val dataCollection = database.getCollection(name)
        val iterator = dataCollection.find().iterator()
        val date = LocalDate.now().minusMonths(1).toString()
        while (iterator.hasNext()) {
            val value = iterator.next()
            if (value["name"] == null || value["date"] == null)
                return false
            if (value["name"] as String == name && value["date"] as String == date) {
                return true
            }
        }

        return false
    }

    fun hasDataToday(name: String): Boolean {
        val dataCollection = database.getCollection(name)
        val iterator = dataCollection.find().iterator()
        while (iterator.hasNext()) {
            val dm = iterator.next()
            if (dm["name"] as String == name && dm["date"] as String == LocalDate.now().toString())
                return true
        }
        return false
    }

    fun isFirstTime(name: String): Boolean {
        val users = usersCollection.find().iterator()
        while (users.hasNext()) {
            if (users.next()["name"] as String == name)
                return false
        }

        return true
    }

}

data class PlayerData(
    val date: String? = "",
    val name: String = "",
    val finalKills: Int = 0,
    val finalDeaths: Int = 0,
    val finalAssists: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0
)
