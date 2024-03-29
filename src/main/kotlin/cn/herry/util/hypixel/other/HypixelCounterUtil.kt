package cn.herry.util.hypixel.other

import cn.herry.config.Config
import cn.herry.util.other.WebUtil
import cn.hutool.json.JSONUtil
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.toPlainText

object HypixelCounterUtil {

    private val publicApi: String = Config.apiKey

    val bsgCounter: MessageChain
        get() {
            if (publicApi == "") {
                return "======mw小帮手======\n".toPlainText() +
                        "查询失败!\n".toPlainText() +
                        "未填写apikey或者apikey无效！\n".toPlainText()
            }

            val json = JSONUtil.parseObj(WebUtil.getJson("https://api.hypixel.net/counts?key=$publicApi"))

            var solo = 0
            var teams = 0

            if (json.getBool("success")) {
                val BSG = json.getJSONObject("games").getJSONObject("SURVIVAL_GAMES")
                if (BSG.getJSONObject("modes") != null) {
                    val modes = BSG.getJSONObject("modes")

                    if (modes.getInt("solo_normal") != null) {
                        solo = modes.getInt("solo_normal")
                    }

                    if (modes.getInt("teams_normal") != null) {
                        teams = modes.getInt("teams_normal")
                    }
                }
            }


            return "=====mw小帮手=====\n".toPlainText() +
                    "| BSG solo: $solo\n".toPlainText() +
                    "| BSG teams: $teams\n".toPlainText()
        }

    val swCounter: MessageChain
        get() {

            if (publicApi == "") {
                return "======mw小帮手======\n".toPlainText() +
                        "查询失败!\n".toPlainText() +
                        "未填写apikey或者apikey无效！\n".toPlainText()
            }

            val json = JSONUtil.parseObj(WebUtil.getJson("https://api.hypixel.net/counts?key=$publicApi"))

            var soloNormal = 0
            var soloInsane = 0
            var teamsNormal = 0
            var teamsInsane = 0

            if (json.getBool("success")) {
                val SKYWARS = json.getJSONObject("games").getJSONObject("SKYWARS")
                if (SKYWARS.getJSONObject("modes") != null) {
                    val modes = SKYWARS.getJSONObject("modes")

                    if (modes.getInt("solo_normal") != null) {
                        soloNormal = modes.getInt("solo_normal")
                    }

                    if (modes.getInt("solo_insane") != null) {
                        soloInsane = modes.getInt("solo_insane")
                    }

                    if (modes.getInt("teams_normal") != null) {
                        teamsNormal = modes.getInt("teams_normal")
                    }

                    if (modes.getInt("teams_insane") != null) {
                        teamsInsane = modes.getInt("teams_insane")
                    }
                }
            }

            return "=====mw小帮手=====\n".toPlainText() +
                    "| SkyWars solo_normal: $soloNormal\n".toPlainText() +
                    "| SkyWars solo_insane: $soloInsane\n".toPlainText() +
                    "| SkyWars teams_normal: $teamsNormal\n".toPlainText() +
                    "| SkyWars teams_insane: $teamsInsane".toPlainText()

        }

    val totalCounter: MessageChain
        get() {
            if (publicApi == "") {
                return "======mw小帮手======\n".toPlainText() +
                        "查询失败!\n".toPlainText() +
                        "未填写apikey或者apikey无效！\n".toPlainText()
            }

            val json = JSONUtil.parseObj(WebUtil.getJson("https://api.hypixel.net/counts?key=$publicApi"))

            var total = 0

            if (json.getBool("success")) {
                total = json.getInt("playerCount")
            }

            return "=====mw小帮手=====\n".toPlainText() +
                    "| Hypixel Total: $total".toPlainText()
        }

    val mwCounter: MessageChain
        get() {

            if (publicApi == "") {
                return "======mw小帮手======\n".toPlainText() +
                        "查询失败!\n".toPlainText() +
                        "未填写apikey或者apikey无效！\n".toPlainText()
            }

            val json = JSONUtil.parseObj(WebUtil.getJson("https://api.hypixel.net/counts?key=$publicApi"))

            var standard = 0
            var faceoff = 0

            if (json.getBool("success")) {
                val WALLS3 = json.getJSONObject("games").getJSONObject("WALLS3")
                if (WALLS3.getJSONObject("modes") != null) {
                    val modes = WALLS3.getJSONObject("modes")

                    if (modes.getInt("standard") != null) {
                        standard = modes.getInt("standard")
                    }

                    if (modes.getInt("face_off") != null) {
                        faceoff = modes.getInt("face_off")
                    }
                }
            }

            return "=====mw小帮手=====\n".toPlainText() +
                    "| Megawalls Standard: ${standard}\n".toPlainText() +
                    "| Megawalls Faceoff: ${faceoff}\n".toPlainText()
        }

    val uhcCounter: MessageChain
        get() {
            if (publicApi == "") {
                return "======mw小帮手======\n".toPlainText() +
                        "查询失败!\n".toPlainText() +
                        "未填写apikey或者apikey无效！\n".toPlainText()
            }

            val json = JSONUtil.parseObj(WebUtil.getJson("https://api.hypixel.net/counts?key=$publicApi"))

            var solo = 0
            var teams = 0

            if (json.getBool("success")) {
                val uhc = json.getJSONObject("games").getJSONObject("UHC")
                if (uhc.getJSONObject("modes") != null) {
                    val modes = uhc.getJSONObject("modes")

                    if (modes.getInt("SOLO") != null) {
                        solo = modes.getInt("SOLO")
                    }

                    if (modes.getInt("TEAMS") != null) {
                        teams = modes.getInt("TEAMS")
                    }
                }
            }

            return "======mw小帮手======\n".toPlainText() +
                    "| UHC Solo: ${solo}\n".toPlainText() +
                    "| UHC Teams: $teams".toPlainText()
        }

}