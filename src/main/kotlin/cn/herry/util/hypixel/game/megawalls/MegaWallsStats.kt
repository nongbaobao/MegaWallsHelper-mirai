package cn.herry.util.hypixel.game.megawalls

import cn.hutool.json.JSONObject

class MegaWallsStats(playerData: JSONObject) {

    var classpointsMap = LinkedHashMap<String, Array<Int>>()
    var coins = 0
    var wins = 0
    var loses = 0
    var kills = 0
    var deaths = 0
    var finalKills = 0
    var finalAssists = 0
    var finalDeaths = 0
    var kdr = 0f
    var fkdr = 0f
    var wlr = 0f
    var fkadr = 0f
    var totalClasspoints = 0

    init {
        val megawallsObj = playerData.getJSONObject("player")
            .getJSONObject("stats")
            .getJSONObject("Walls3")

        coins = megawallsObj.getInt("coins")?: 0
        wins = megawallsObj.getInt("wins")?: 0
        loses = megawallsObj.getInt("losses")?: 0
        kills = megawallsObj.getInt("kills")?: 0
        deaths = megawallsObj.getInt("deaths")?: 0
        finalKills = megawallsObj.getInt("final_kills")?: 0
        finalAssists = megawallsObj.getInt("final_assists")?: 0
        finalDeaths = megawallsObj.getInt("final_deaths")?: 0

        kdr = if (deaths != 0) {
            kills.toFloat() / deaths.toFloat()
        } else {
            kills.toFloat()
        }

        fkdr = if (finalDeaths != 0) {
            finalKills.toFloat() / finalDeaths.toFloat()
        } else {
            finalKills.toFloat()
        }

        wlr = if (loses != 0) {
            wins.toFloat() / loses.toFloat()
        } else {
            wins.toFloat()
        }

        fkadr = if (finalDeaths != 0) {
            (finalKills + finalAssists).toFloat() / finalDeaths.toFloat()
        } else {
            (finalKills + finalAssists).toFloat()
        }

        val classesdata = megawallsObj.getJSONObject("classes")
        if (classesdata != null) {
            for (mwclass in MWClass.values()) {
                val className = mwclass.name.lowercase()
                val classObj = if (classesdata.getJSONObject(className) != null) {
                    classesdata.getJSONObject(className)
                } else {
                    continue
                }
                val prestige = if (classObj.getInt("prestige") != null) {
                    classObj.getInt("prestige")
                } else {
                    0
                }
                val classpoints = (megawallsObj.getInt("${className}_final_kills_standard") ?: 0) +
                        (megawallsObj.getInt("${className}_final_assists_standard") ?: 0) +
                        (megawallsObj.getInt("${className}_wins_standard") ?: 0) * 10

                totalClasspoints += classpoints
                classpointsMap[className] = arrayOf(prestige, classpoints)
            }
        }
    }


}