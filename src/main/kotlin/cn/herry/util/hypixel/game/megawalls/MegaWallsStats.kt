package cn.herry.util.hypixel.game.megawalls

import cn.hutool.json.JSONObject
import kotlinx.serialization.Serializable


@Serializable
class MegaWallsStats {

    var classpointsMap = LinkedHashMap<String, Array<Int>>()
    var chosenClass: String? = null
    var chosenSkinClass: String? = null
    var coins = 0
    var wins = 0
    var losses = 0
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

    constructor(playerData: JSONObject) {
        val megawallsObj = playerData.getJSONObject("player")
            .getJSONObject("stats")
            .getJSONObject("walls3")

        chosenClass = megawallsObj.getStr("chosen_class")
        chosenSkinClass = megawallsObj.getStr("chosen_skin_$chosenClass")
        coins = megawallsObj.getInt("coins")
        wins = megawallsObj.getInt("wins")
        losses = megawallsObj.getInt("losses")
        kills = megawallsObj.getInt("kills")
        deaths = megawallsObj.getInt("deaths")
        finalKills = megawallsObj.getInt("final_kills")
        finalAssists = megawallsObj.getInt("final_assists")
        finalDeaths = megawallsObj.getInt("final_deaths")

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

        wlr = if (losses != 0) {
            wins.toFloat() / losses.toFloat()
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
                val classObj = classesdata.getJSONObject(className)
                val prestige = classObj.getInt("prestige")
                val classpoints = megawallsObj.getInt("${className}_final_kills_standard") +
                        megawallsObj.getInt("${className}_final_assists_standard") +
                        megawallsObj.getInt("${className}_wins_standard") * 10

                totalClasspoints += classpoints
                classpointsMap[className] = arrayOf(prestige, classpoints)
            }
        }
    }


}