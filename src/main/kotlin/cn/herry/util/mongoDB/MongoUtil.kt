package cn.herry.util.mongoDB

import cn.herry.util.hypixel.MegaWallsUtil
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document
import java.time.LocalDate

object MongoUtil {

    val client: MongoClient = MongoClients.create("mongodb://username:password@127.0.0.1:23333/")

    private val DataCollection: MongoCollection<Document> = client.getDatabase("hypixel").getCollection("datas")
    private val UsersCollection: MongoCollection<Document> = client.getDatabase("hypixel").getCollection("users")

    fun addDataByFirst(data: PlayerData) {
        val addDocument = Document("date", data.date)
            .append("name", data.name)
            .append("finalKills", data.finalKills)
            .append("finalDeaths", data.finalDeaths)
            .append("wins", data.wins)
            .append("losses", data.losses)

        val userDocument = Document("date", data.date)
            .append("name", data.name)

        DataCollection.insertOne(addDocument)
        UsersCollection.insertOne(userDocument)
    }

    fun getUsedUser(): FindIterable<Document> {
        return UsersCollection.find()
    }

    fun updateData(data: PlayerData) {
        val document = Document("\$set", Document("date", data.date))
            .append("\$set", Document("name", data.name))
            .append("\$set", Document("finalKills", data.finalKills))
            .append("\$set", Document("finalDeaths", data.finalDeaths))
            .append("\$set", Document("wins", data.wins))
            .append("\$set", Document("losses", data.losses))

        val iterator = DataCollection.find().iterator()
        while (iterator.hasNext()) {
            val dm = iterator.next()
            if (dm["user"] as String == data.name && dm["date"] as String == data.date) {
                DataCollection.updateMany(dm, document)
            }
        }
    }

    fun getDataByDate(name: String, date: String): PlayerData? {
        val iterator = DataCollection.find().iterator()
        while (iterator.hasNext()) {
            val dm: Document = iterator.next()
            if (dm["name"] as String == name && dm["date"] as String == date)
                return PlayerData(
                    dm["date"] as String,
                    dm["name"] as String,
                    dm["finalKills"] as Int,
                    dm["finalDeaths"] as Int,
                    dm["wins"] as Int,
                    dm["losses"] as Int
                )
        }

        return MegaWallsUtil.getPlayerDataAndSave(name)
    }

    fun hasDaily(name: String): Boolean {
        val iterator = DataCollection.find().iterator()
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
        val iterator = DataCollection.find().iterator()
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
        val iterator = DataCollection.find().iterator()
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

    fun hasYearly(name: String): Boolean {
        val iterator = DataCollection.find().iterator()
        val date = LocalDate.now().minusYears(1).toString()
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
        val iterator = DataCollection.find().iterator()
        while (iterator.hasNext()) {
            val dm = iterator.next()
            if (dm["name"] as String == name && dm["date"] as String == LocalDate.now().toString())
                return true
        }
        return false
    }

    fun isFirstTime(name: String): Boolean {
        val users = UsersCollection.find().iterator()
        while (users.hasNext()) {
            if (users.next()["name"] as String == name)
                return true
        }

        return false
    }
}

data class PlayerData(
    val date: String? = "",
    val name: String = "",
    val finalKills: Int = 0,
    val finalDeaths: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0
)
