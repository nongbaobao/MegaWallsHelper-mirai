package cn.herry.util.hypixel.game.megawalls

import cn.hutool.json.JSONObject
import kotlin.math.max


class MegaWallsClassStats {

    private var classname: String? = null
    private var chosenSkinClass: String? = null

    // in the mwdata
    private var coins = 0
    private var classnameKills = 0
    private var classnameFinalAssists = 0
    private var classnameDeaths = 0
    private var classnameWins = 0
    private var classnameLosses = 0
    private var classnameFinalKills = 0
    private var classnameFinalDeaths = 0
    private var classnameTimePlayed = 0

    // to compute
    private var kdr = 0f
    private var fkdr = 0f
    private var wlr = 0f
    private var fkadr = 0f
    private var gamesPlayed = 0
    private var fkpergame = 0f
    private var classnameFinalAssistsStandard = 0
    private var classpoints = 0

    // in the mwdata -> classes -> classname
    private var unlocked = false
    private var skillLevelA = 1 // ability
    private var skillLevelB = 1 // passive 1
    private var skillLevelC = 1 // passive 2
    private var skillLevelD = 1 // kit
    private var skillLevelG = 1 // gathering

    private var prestige = 0
    private var enderChestRows = 3

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
        }else {
            classnameFinalKills.toFloat()
        }

        val classesData = megawallsObj.getJSONObject("classes") ?: return
        val classObj = classesData.getJSONObject(classname) ?: return
        unlocked = classObj.getBool("unlocked")
        skillLevelA = max(classObj.getInt("ability"), 1)
        skillLevelB = max(classObj.getInt("skill_level_b"), 1)
        skillLevelC = max(classObj.getInt("skill_level_c"), 1)
        skillLevelD = max(classObj.getInt("skill_level_d"), 1)
        skillLevelG = max(classObj.getInt("skill_level_g"), 1)
        prestige = classObj.getInt("prestige")
        enderChestRows = max(classObj.getInt("enderchest_rows"), 3)

    }

}