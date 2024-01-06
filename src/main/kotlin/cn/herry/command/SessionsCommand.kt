package cn.herry.command

import cn.herry.Helper
import cn.herry.config.megawalls.CustomData
import cn.herry.config.megawalls.PlayerData
import cn.herry.config.megawalls.updateData
import cn.herry.util.hypixel.game.megawalls.MegaWallsUtil
import cn.herry.util.other.MinecraftUtil
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.UserCommandSender
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.message.data.toPlainText
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object SessionsCommand : CompositeCommand (
    Helper,
    "sessions"
){

    @SubCommand("mw")
    suspend fun UserCommandSender.mw(player: String) {
        if (!MinecraftUtil.hasUser(name)) {
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "查询失败!\n" +
                        "未填写apikey或者apikey无效！\n" +
                        "======mw小帮手======"
            )
            return
        }

        if (PlayerData.data[user.id] != null) {
            val timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond

            val latestPlayerData = MegaWallsUtil.getPlayerMegawallsStats(player)!!
            val playerData = PlayerData.data[user.id]
        } else {
            val timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond

            val playerData = MegaWallsUtil.getPlayerMegawallsStats(player)!!
            val storeData = CustomData(name = player,
                time = timestamp,
                wins = playerData.wins,
                loses = playerData.loses,
                kills = playerData.kills,
                deaths = playerData.deaths,
                finalKills = playerData.finalKills,
                finalAssists = playerData.finalAssists,
                finalDeaths = playerData.finalDeaths
            ).updateData(user.id)

            val restoredDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC)
            subject.sendMessage(
                "======mw小帮手======\n" +
                    "当前时间: ${restoredDateTime.toString().replace("T", " ")}\n" +
                    "第一次使用，已记录当前数据！"
            )
        }
    }
}