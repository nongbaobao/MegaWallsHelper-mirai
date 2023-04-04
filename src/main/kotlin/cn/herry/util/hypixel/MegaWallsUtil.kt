package cn.herry.util.hypixel

import cn.herry.config.Config
import cn.herry.util.other.WebUtil
import cn.herry.util.mongoDB.MongoUtil
import cn.herry.util.mongoDB.PlayerData
import cn.hutool.json.JSONUtil
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


    fun getPlayerDataAndSave(name: String): PlayerData? {
        if (publicApi == "")
            throw IllegalAccessException("There's no apiKey in Config")

        val json = JSONUtil.parseObj(
            WebUtil.getJson(
                "https://api.hypixel.net/player?key=$publicApi&uuid=${
                    MinecraftUtil.getUUID(
                        name
                    )
                }"
            )
        )

        if (json.getBool("success")) {
            val wall3 = json.getJSONObject("player")
                .getJSONObject("stats")
                .getJSONObject("Walls3")

            val finalKills = wall3.getInt("final_kills_standard")
            val finalDeaths = wall3.getInt("final_deaths_standard")
            val finalAssists = wall3.getInt("final_assists_standard")
            val wins = wall3.getInt("wins_standard")
            val losses = wall3.getInt("losses_standard")

            val playerData = PlayerData(LocalDate.now().toString(), name, finalKills, finalDeaths, finalAssists, wins, losses)
            return if (MongoUtil.hasDataToday(name)) {
                MongoUtil.updateData(playerData)
                MongoUtil.getDataByDate(name, LocalDate.now().toString())
            } else if (MongoUtil.isFirstTime(name)) {
                PlayerData(LocalDate.now().toString(), name, finalKills, finalDeaths, finalAssists, wins, losses)
            } else {
                MongoUtil.addDataByFirst(playerData)
                PlayerData(LocalDate.now().toString(), name, finalKills, finalDeaths, finalAssists, wins, losses)
            }
        }

        return null
    }

}