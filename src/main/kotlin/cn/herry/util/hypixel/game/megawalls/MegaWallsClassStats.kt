package cn.herry.util.hypixel.game.megawalls

import cn.hutool.json.JSONObject
import kotlin.math.max


class MegaWallsClassStats(playerData: JSONObject?, classnameIn: String) {

    private var classname: String? = null
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

    init {
        if (playerData == null) {
            throw NullPointerException("playerData is null")
        }
        val megawallsObj = playerData.getJSONObject("player")
            .getJSONObject("stats")
            .getJSONObject("Walls3")
        classname = classnameIn.lowercase()

        chosenSkinClass = megawallsObj.getStr("chosen_skin_$classnameIn")
        coins = megawallsObj.getInt("coins")?: 0

        classnameKills = megawallsObj.getInt("${classname}_kills")?: 0
        classnameFinalAssists = megawallsObj.getInt("${classname}_final_assists")?: 0
        classnameDeaths = megawallsObj.getInt("${classname}_deaths")?: 0
        classnameWins = megawallsObj.getInt("${classname}_wins")?: 0
        classnameLosses = megawallsObj.getInt("${classname}_losses")?: 0
        classnameFinalKills = megawallsObj.getInt("${classname}_final_kills")?: 0
        classnameFinalDeaths = megawallsObj.getInt("${classname}_final_deaths")?: 0
        classnameTimePlayed = megawallsObj.getInt("${classname}_time_played")?: 0
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
        classpoints = megawallsObj.getInt("${classname}_class_points")
        gamesPlayed = classnameWins + classnameLosses
        fkpergame = if (gamesPlayed != 0) {
            classnameFinalKills.toFloat() / gamesPlayed.toFloat()
        } else {
            classnameFinalKills.toFloat()
        }
        val classesData = megawallsObj.getJSONObject("classes") ?: throw NullPointerException("playerData is null")
        val classObj = classesData.getJSONObject(classname) ?: throw NullPointerException("playerData is null")
        unlocked = classObj.getBool("unlocked")?: false
        skillLevelA = max(classObj.getInt("skill_level_a")?: 0, 1)
        skillLevelB = max(classObj.getInt("skill_level_b")?: 0, 1)
        skillLevelC = max(classObj.getInt("skill_level_c")?: 0, 1)
        skillLevelD = max(classObj.getInt("skill_level_d")?: 0, 1)
        skillLevelG = max(classObj.getInt("skill_level_g")?: 0, 1)
        prestige = classObj.getInt("prestige")?: 0
        enderChestRows = max(classObj.getInt("enderchest_rows")?: 1, 3)
    }

}