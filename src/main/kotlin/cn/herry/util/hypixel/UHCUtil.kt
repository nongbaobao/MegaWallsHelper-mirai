package cn.herry.util.hypixel

import cn.herry.config.Config
import cn.herry.util.other.WebUtil
import cn.hutool.json.JSONUtil

object UHCUtil {

    private val publicApi: String = Config.apiKey

    val uhcCounter: IntArray?
        get() {
            if (publicApi == "") {
                return null
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

                    return intArrayOf(solo, teams)
                }
            }

            return intArrayOf(solo, teams)
        }

}