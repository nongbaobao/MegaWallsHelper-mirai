package cn.herry.util.hypixel

import cn.herry.config.Config
import cn.herry.util.other.WebUtil
import cn.hutool.json.JSONUtil
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.toPlainText

object HypixelCounterUtil {

    private val publicApi: String = Config.apiKey

    val bwCounter: MessageChain
        get() {

            if (publicApi == "") {
                return "======mw小帮手======\n".toPlainText() +
                        "查询失败!\n".toPlainText() +
                        "未填写apikey或者apikey无效！\n".toPlainText()
            }

            val json = JSONUtil.parseObj(WebUtil.getJson("https://api.hypixel.net/counts?key=$publicApi"))

            var bedWarsTwoFour = 0
            var bedWarsFourThree = 0
            var teamsNormal = 0

            if (json.getBool("success")) {
                val SKYWARS = json.getJSONObject("games").getJSONObject("SKYWARS")
                if (SKYWARS.getJSONObject("modes") != null) {
                    val modes = SKYWARS.getJSONObject("modes")


                }
            }

            return "".toPlainText() + ""

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

            if (json.getBool("success")) {
                val SKYWARS = json.getJSONObject("games").getJSONObject("SKYWARS")
                if (SKYWARS.getJSONObject("modes") != null) {
                    val modes = SKYWARS.getJSONObject("modes")

                    if (modes.getInt("solo_normal") != null) {
                        soloNormal = modes.getInt("standard")
                    }

                    if (modes.getInt("solo_insane") != null) {
                        soloInsane = modes.getInt("face_off")
                    }

                    if (modes.getInt("teams_normal") != null) {
                        teamsNormal = modes.getInt("teams_normal")
                    }
                }
            }

            return "=====hyp counter=====\n".toPlainText() +
                    "| SkyWars solo_normal: $soloNormal\n".toPlainText() +
                    "| SkyWars solo_insane: $soloInsane\n".toPlainText() +
                    "| SkyWars teams_normal: $teamsNormal".toPlainText()

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

            return "=====hyp counter=====\n".toPlainText() +
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

            return "=====hyp counter=====\n".toPlainText() +
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

            val json = JSONUtil.parseObj(WebUtil.getJson("https://api.hypixel.net/counts?key=${publicApi}"))

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