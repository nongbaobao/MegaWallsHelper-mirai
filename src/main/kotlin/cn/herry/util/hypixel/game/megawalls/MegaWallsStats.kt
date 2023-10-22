package cn.herry.util.hypixel.game.megawalls

import cn.hutool.json.JSONObject
import lombok.Getter


@Getter
class MegaWallsStats {

    var classpointsMap = LinkedHashMap<String, Array<Int>>()
    var chosen_class: String? = null
    var chosen_skin_class: String? = null
    var coins = 0
    var wins = 0
    var losses = 0
    var kills = 0
    var deaths = 0
    var final_kills = 0
    var final_assists = 0
    var final_deaths = 0
    var kdr = 0f
    var fkdr = 0f
    var wlr = 0f
    var fkadr = 0f
    var total_classpoints = 0

    constructor(playerData: JSONObject) {
        val megawallsObj = playerData.getJSONObject("player")
            .getJSONObject("stats")
            .getJSONObject("walls3")

        chosen_class = megawallsObj.getStr("chosen_class")
        chosen_skin_class = megawallsObj.getStr("chosen_skin_$chosen_class")
        coins = megawallsObj.getInt("coins")
        wins = megawallsObj.getInt("wins")
        losses = megawallsObj.getInt("losses")
        kills = megawallsObj.getInt("kills")
        deaths = megawallsObj.getInt("deaths")
        final_kills = megawallsObj.getInt("final_kills")
        final_assists = megawallsObj.getInt("final_assists")
        final_deaths = megawallsObj.getInt("final_deaths")

        kdr = if (deaths != 0) {
            kills.toFloat() / deaths.toFloat()
        } else {
            kills.toFloat()
        }

        fkdr = if (final_deaths != 0) {
            final_kills.toFloat() / final_deaths.toFloat()
        } else {
            final_kills.toFloat()
        }

        wlr = if (losses != 0) {
            wins.toFloat() / losses.toFloat()
        } else {
            wins.toFloat()
        }

        fkadr = if (final_deaths != 0) {
            (final_kills + final_assists).toFloat() / final_deaths.toFloat()
        } else {
            (final_kills + final_assists).toFloat()
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

                total_classpoints += classpoints
                classpointsMap[className] = arrayOf(prestige, classpoints)
            }
        }
    }


}