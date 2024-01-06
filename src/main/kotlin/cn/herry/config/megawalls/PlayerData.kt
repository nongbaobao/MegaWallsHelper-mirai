package cn.herry.config.megawalls

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import java.time.LocalDateTime
import java.time.ZoneOffset

object PlayerData : AutoSavePluginData("playerData") {
    var data: MutableMap<Long, CustomData> by value()
}

fun CustomData.updateData(user: Long) {
    PlayerData.data[user] = this
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

    fun fdr(): Float {
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

    fun resetAll() {
        this.wins = 0
        this.loses = 0
        this.kills = 0
        this.deaths = 0
        this.finalKills = 0
        this.finalAssists = 0
        this.finalDeaths = 0
        time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    }
}