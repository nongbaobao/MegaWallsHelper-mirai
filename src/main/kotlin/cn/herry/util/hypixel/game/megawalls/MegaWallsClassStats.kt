package cn.herry.util.hypixel.game.megawalls

import cn.hutool.json.JSONObject
import kotlin.math.max


class MegaWallsClassStats {

    var classname: String? = null
    var chosenSkinClass: String? = null

    // in the mwdata
    var coins = 0
    var classnameKills = 0
    var classnameFinalAssists = 0
    var classnameDeaths = 0
    var classnameWins = 0
    var classnameLosses = 0
    var classnameFinalKills = 0
    var classnameFinalDeaths = 0
    var classnameTimePlayed = 0

    // to compute
    var kdr = 0f
    var fkdr = 0f
    var wlr = 0f
    var fkadr = 0f
    var gamesPlayed = 0
    var fkpergame = 0f
    var classnameFinalAssistsStandard = 0
    var classpoints = 0

    // in the mwdata -> classes -> classname
    var unlocked = false
    var skillLevelA = 1 // ability
    var skillLevelB = 1 // passive 1
    var skillLevelC = 1 // passive 2
    var skillLevelD = 1 // kit
    var skillLevelG = 1 // gathering

    var prestige = 0
    var enderChestRows = 3

    constructor(playerData: JSONObject?, classnameIn: String) {
        if (playerData == null) {
            return
        }

        val megawallsObj = playerData.getJSONObject("player")
            .getJSONObject("stats")
            .getJSONObject("Walls3")

        classname = classnameIn.lowercase()
        chosenSkinClass = megawallsObj.getStr("chosen_skin_$classnameIn")

        coins = megawallsObj.getInt("coins")

        classnameKills = megawallsObj.getInt("${classname}_kills")
        classnameFinalAssists = megawallsObj.getInt("${classname}_final_assists")
        classnameDeaths = megawallsObj.getInt("${classname}_deaths")
        classnameWins = megawallsObj.getInt("${classname}_wins")
        classnameLosses = megawallsObj.getInt("${classname}_losses")
        classnameFinalKills = megawallsObj.getInt("${classname}_final_kills")
        classnameFinalDeaths = megawallsObj.getInt("${classname}_final_deaths")
        classnameTimePlayed = megawallsObj.getInt("${classname}_time_played")

        kdr = if (classnameDeaths != 0) {
            classnameKills.toFloat() / classnameDeaths.toFloat()
        } else {
            classnameKills.toFloat()
        }

        fkdr = if (classnameFinalDeaths != 0) {
            classnameFinalKills.toFloat() / classnameFinalDeaths.toFloat()
        } else {
            classnameFinalKills.toFloat()
        }

        wlr = if (classnameLosses != 0) {
            classnameWins.toFloat() / classnameLosses.toFloat()
        } else {
            classnameWins.toFloat()
        }

        fkadr = if (classnameFinalDeaths != 0) {
            (classnameFinalKills + classnameFinalAssists).toFloat() / classnameFinalDeaths.toFloat()
        } else {
            (classnameFinalKills + classnameFinalAssists).toFloat()
        }

        val classname_final_kills_standard = megawallsObj.getInt("${classname}_final_kills_standard")
        classnameFinalAssistsStandard = megawallsObj.getInt("${classname}_final_assists_standard")
        val classname_wins_standard = megawallsObj.getInt("${classname}_wins_standard")
        classpoints = classname_final_kills_standard + classnameFinalAssistsStandard + classname_wins_standard * 10

        gamesPlayed = classnameWins + classnameLosses
        fkpergame = if (gamesPlayed != 0) {
            classnameFinalKills.toFloat() / gamesPlayed.toFloat()
        } else {
            classnameFinalKills.toFloat()
        }

        val classesData = megawallsObj.getJSONObject("classes") ?: return
        val classObj = classesData.getJSONObject(classname) ?: return
        unlocked = classObj.getBool("unlocked")
        skillLevelA = max(classObj.getInt("skill_level_a")?: 0, 1)
        skillLevelB = max(classObj.getInt("skill_level_b")?: 0, 1)
        skillLevelC = max(classObj.getInt("skill_level_c")?: 0, 1)
        skillLevelD = max(classObj.getInt("skill_level_d")?: 0, 1)
        skillLevelG = max(classObj.getInt("skill_level_g")?: 0, 1)
        prestige = classObj.getInt("prestige")?: 0
        enderChestRows = max(classObj.getInt("enderchest_rows")?: 1, 3)

    }

}