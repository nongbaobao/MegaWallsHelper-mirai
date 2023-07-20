package cn.herry.util.hypixel.game

import cn.herry.Helper
import cn.herry.config.Config
import cn.herry.util.hypixel.fks.MegaWallsPlayer
import cn.herry.util.mongoDB.MongoUtil
import cn.herry.util.other.MinecraftUtil
import cn.herry.util.other.WebUtil
import cn.hutool.json.JSONUtil
import net.mamoe.mirai.message.data.toPlainText
import org.bson.Document
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

object MegaWallsUtil {

    private val publicApi: String = Config.apiKey

    val mwCounter: IntArray?
        get() {

            if (publicApi == "") {
                return null
            }

            val json = JSONUtil.parseObj(WebUtil.getJson("https://api.hypixel.net/counts?key=$publicApi"))

            var standard = 0
            var faceoff = 0

            if (json.getBool("success")) {
                val WALLS3 = json.getJSONObject("games").getJSONObject("WALLS3")
                return if (WALLS3.getJSONObject("modes") != null) {
                    val modes = WALLS3.getJSONObject("modes")

                    if (modes.getInt("standard") != null) {
                        standard = modes.getInt("standard")
                    }

                    if (modes.getInt("face_off") != null) {
                        faceoff = modes.getInt("face_off")
                    }

                    intArrayOf(standard, faceoff)

                } else {
                    intArrayOf(0, 0)
                }
            }

            return intArrayOf(0, 0)
        }

    fun getMegaWallsPlayerData(name: String): MegaWallsPlayer? {
        if (publicApi == "") {
            return null
        }

        if (!MinecraftUtil.hasUser(name)) {
            return null
        }

        val json = JSONUtil.parseObj(WebUtil.getJson("https://api.hypixel.net/player?key=$publicApi&uuid=${MinecraftUtil.getUUID(name)}"))

        var name: String = ""
        var rank: String = ""
        var finalKills: Int = 0
        var finalAssists: Int = 0
        var finalDeaths: Int = 0
        var wins: Int = 0
        var losses: Int = 0

        if (json.getBool("success")) {
            val WALLS3 = json.getJSONObject("player").getJSONObject("stats").getJSONObject("Walls3")

            if (WALLS3.getInt("final_kills_standard") != 0) {
                finalKills = WALLS3.getInt("final_kills_standard")
            }

            if (WALLS3.getInt("final_assists_standard") != 0) {
                finalAssists = WALLS3.getInt("final_assists_standard")
            }

            if (WALLS3.getInt("final_deaths_standard") != 0) {
                finalDeaths = WALLS3.getInt("final_deaths_standard")
            }

            if (WALLS3.getInt("wins_standard") != 0) {
                wins = WALLS3.getInt("wins_standard")
            }

            if (WALLS3.getInt("losses_standard") != 0) {
                losses = WALLS3.getInt("losses_standard")
            }

            if (json.getJSONObject("player").getStr("playername") != "") {
                name = json.getJSONObject("player").getStr("playername")
            }

            if (json.getJSONObject("player").getStr("newPackageRank") != "") {
                rank = json.getJSONObject("player").getStr("newPackageRank")
            }

            return MegaWallsPlayer(name, rank, LocalDate.now().toString(), finalKills, finalAssists, finalDeaths, wins, losses)
        }

        return null
    }

    fun updateMegaWallsPlayerData(name: String) {
        val megaWallsPlayerData = getMegaWallsPlayerData(name)
        if (megaWallsPlayerData != null) {
            val document = Document("\$set", Document("name", megaWallsPlayerData.name)
                .append("rank", megaWallsPlayerData.rank)
                .append("date", megaWallsPlayerData.date)
                .append("finalKills", megaWallsPlayerData.finalKills)
                .append("finalAssists", megaWallsPlayerData.finalAssists)
                .append("finalDeaths", megaWallsPlayerData.finalDeaths)
                .append("wins", megaWallsPlayerData.wins)
                .append("losses", megaWallsPlayerData.losses))

            val iterator = MongoUtil.getDocuments("mw", "data").find().iterator()
            while (iterator.hasNext()) {
                val document_ = iterator.next()
                if (document_["name"] as String == name && document_["date"] as String == LocalDate.now().toString()) {
                    MongoUtil.getDocuments("mw","data").updateOne(document_, document)
                }
            }
        }


    }

    fun hasDataByDate(name: String, date: LocalDate): Boolean {
        var collections = MongoUtil.getDocuments("mw", "data")
        var iterator = collections.find().iterator()
        while (iterator.hasNext()) {
            val document = iterator.next()
            if (document["name"] as String == name && document["date"] as String == date.toString()) {
                return true
            } else {
                continue
            }
        }

        return false
    }

    fun hasData(name: String): Boolean {
        val iterator = MongoUtil.getDocuments("mw", "data").find().iterator()
        while (iterator.hasNext()) {
            val document = iterator.next()
            if (document["name"] as String == name) {
                return true
            } else {
                continue
            }
        }

        return false
    }

    fun getAllMegawallsPlayersName(): ArrayList<String> {
        var collections = MongoUtil.getDocuments("mw", "data")
        var iterator = collections.find().iterator()
        var listsOfNames = ArrayList<String>()
        while (iterator.hasNext()) {
            val document = iterator.next()
            if (document["date"] as String == LocalDate.now().toString()) {
                listsOfNames.add(document["name"] as String)
            } else if (document["date"] as String == LocalDate.now().minusDays(1).toString()) {
                listsOfNames.add(document["name"] as String)
            }
        }

        return listsOfNames
    }

    fun getMegaWallsPlayerDataByDate(name: String, date: LocalDate): MegaWallsPlayer? {
        var collections = MongoUtil.getDocuments("mw", "data")
        var iterator = collections.find().iterator()
        while (iterator.hasNext()) {
            val document = iterator.next()
            if ((document["name"] as String).equals(name,true) && document["date"] as String == date.toString()) {
                return MegaWallsPlayer(document["name"] as String,
                    document["rank"] as String,
                    document["date"] as String,
                    document["finalKills"] as Int,
                    document["finalAssists"] as Int,
                    document["finalDeaths"] as Int,
                    document["wins"] as Int,
                    document["losses"] as Int
                )
            }
        }

        return null
    }

    fun writeDataByFirst(name: String): Boolean {
        // by first
        val megaWallsPlayerData = getMegaWallsPlayerData(name)
        return if (megaWallsPlayerData != null) {
            val document: Document = Document("name", megaWallsPlayerData.name)
                .append("rank", megaWallsPlayerData.rank)
                .append("date", megaWallsPlayerData.date)
                .append("finalKills", megaWallsPlayerData.finalKills)
                .append("finalAssists", megaWallsPlayerData.finalAssists)
                .append("finalDeaths", megaWallsPlayerData.finalDeaths)
                .append("wins", megaWallsPlayerData.wins)
                .append("losses", megaWallsPlayerData.losses)

            MongoUtil.writeDocumentToCollection("mw", "data", document)
            true
        } else {
            false
        }
    }
}