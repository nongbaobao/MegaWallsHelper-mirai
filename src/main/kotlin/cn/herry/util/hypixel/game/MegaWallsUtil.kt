package cn.herry.util.hypixel.game

import cn.herry.config.Config
import cn.herry.util.hypixel.fks.MegaWallsPlayer
import cn.herry.util.mongoDB.MongoUtil
import cn.herry.util.other.MinecraftUtil
import cn.herry.util.other.WebUtil
import cn.hutool.json.JSONUtil
import org.bson.Document
import java.time.LocalDate

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
        var date: String = ""
        var finalKills: Int = 0
        var finalAssists: Int = 0
        var finalDeaths: Int = 0
        var wins: Int = 0
        var losses: Int = 0

        if (json.getBool("success")) {
            val WALLS3 = json.getJSONObject("player").getJSONObject("stats").getJSONObject("wall3")

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

            if (WALLS3.getStr("playername") != "") {
                name = WALLS3.getStr("playername")
            }

            if (WALLS3.getStr("newPackageRank") != "") {
                rank = WALLS3.getStr("newPackageRank")
            }

            return MegaWallsPlayer(name, rank, LocalDate.now().toString(), finalKills, finalAssists, finalDeaths, wins, losses)
        }

        return null
    }

    fun updateMegaWallsPlayerData(name: String) {
        val megaWallsPlayerData = getMegaWallsPlayerData(name)
        if (megaWallsPlayerData != null) {
            val document = Document("\$set", Document("name", megaWallsPlayerData.name)
                .append("\$set", Document("rank", megaWallsPlayerData.rank))
                .append("\$set", Document("date", megaWallsPlayerData.date))
                .append("\$set", Document("finalKills", megaWallsPlayerData.finalKills))
                .append("\$set", Document("finalAssists", megaWallsPlayerData.finalAssists))
                .append("\$set", Document("finalDeaths", megaWallsPlayerData.finalDeaths))
                .append("\$set", Document("wins", megaWallsPlayerData.wins))
                .append("\$set", Document("losses", megaWallsPlayerData.losses)))
        }

        val iterator = MongoUtil.getDocuments("mw", "data").find().iterator()
        while (iterator.hasNext()) {
            val document = iterator.next()
            if (document["name"] == name && document["date"] == LocalDate.now()) {

            }
        }
    }

    fun hasDataByDate(name: String, date: LocalDate): Boolean {
        var collections = MongoUtil.getDocuments("mw", "data")
        var iterator = collections.find().iterator()
        while (iterator.hasNext()) {
            val document = iterator.next()
            return document["name"] == name && document["date"] == date.toString()
        }

        return false
    }

    fun hasData(name: String): Boolean {
        var collections = MongoUtil.getDocuments("mw", "data")
        var iterator = collections.find().iterator()
        while (iterator.hasNext()) {
            val document = iterator.next()
            return document["name"] == name
        }

        return false
    }
}