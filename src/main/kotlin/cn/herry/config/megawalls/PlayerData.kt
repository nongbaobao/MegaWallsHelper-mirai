package cn.herry.config.megawalls

import cn.herry.util.hypixel.game.megawalls.MegaWallsUtil
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object PlayerData : AutoSavePluginData("playerData") {
    var data: MutableMap<Long, CustomData> by value()
}

fun CustomData.updateData(user: Long) {
    PlayerData.data[user] = this
}

fun Long.toDataTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneOffset.UTC)
}

@Serializable
data class CustomData(
    var name: String = "",
    var time: Long = 0,
    var wins: Int = 0,
    var loses: Int = 0,
    var kills: Int = 0,
    var deaths: Int = 0,
    var finalKills: Int = 0,
    var finalAssists: Int = 0,
    var finalDeaths: Int = 0,
) {

    fun kdr(): Float {
        return if (deaths != 0) {
            kills.toFloat() / deaths.toFloat()
        } else {
            kills.toFloat()
        }
    }

    fun fkdr(): Float {
        return if (deaths != 0) {
            finalKills.toFloat() / deaths.toFloat()
        } else {
            finalKills.toFloat()
        }
    }

    fun wlr(): Float {
        return if (wins != 0) {
            wins.toFloat() / loses.toFloat()
        } else {
            wins.toFloat()
        }
    }

    fun fkadr(): Float {
        return if (finalDeaths != 0) {
            (finalKills + finalAssists).toFloat() / finalDeaths.toFloat()
        } else {
            (finalKills + finalAssists).toFloat()
        }
    }

    fun reset() {
        val timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond
        val playerData = MegaWallsUtil.getPlayerMegawallsStats(name)!!
        time = timestamp
        wins = playerData.wins
        loses = playerData.loses
        kills = playerData.kills
        deaths = playerData.deaths
        finalKills = playerData.finalKills
        finalAssists = playerData.finalAssists
        finalDeaths = playerData.finalDeaths
    }
}