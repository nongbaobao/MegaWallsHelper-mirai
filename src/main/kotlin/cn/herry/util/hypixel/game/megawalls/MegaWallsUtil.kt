package cn.herry.util.hypixel.game.megawalls

import cn.herry.config.Config
import cn.herry.util.other.MinecraftUtil
import cn.herry.util.other.WebUtil
import cn.hutool.json.JSONUtil

object MegaWallsUtil {


    private val publicApi: String = Config.apiKey

    fun intToRoman(number: Int): String {
        return when (number) {
            1 -> "I"
            2 -> "II"
            3 -> "III"
            4 -> "IV"
            5 -> "V"
            else -> number.toString()
        }
    }

    fun getPlayerMegawallsStats(name: String): MegaWallsStats? {
        val mainObj = JSONUtil.parseObj(WebUtil.getJson("https://api.hypixel.net/player?key=$publicApi&uuid=${MinecraftUtil.getUUID(name)}"))
        if (mainObj.getBool("success")) {
            return MegaWallsStats(mainObj)
        }

        return null
    }

    fun getPlayerMegawallsClassStats(name: String, classss: String): MegaWallsClassStats? {
        val mainObj = JSONUtil.parseObj(WebUtil.getJson("https://api.hypixel.net/player?key=$publicApi&uuid=${MinecraftUtil.getUUID(name)}"))
        if (mainObj.getBool("success")) {
            return MegaWallsClassStats(mainObj, classss)
        }

        return null
    }

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
}