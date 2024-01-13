package cn.herry.command

import cn.herry.Helper
import cn.herry.config.megawalls.CustomData
import cn.herry.config.megawalls.PlayerData
import cn.herry.config.megawalls.toDataTime
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

    @SubCommand("reset")
    suspend fun UserCommandSender.reset(player: String) {
        if (!MinecraftUtil.hasUser(name)) {
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "查询失败!\n" +
                        "无法找到该玩家！"
            )
            return
        }

        if (PlayerData.data[user.id] != null) {
            if (PlayerData.data[user.id]!!.name.lowercase() != player.lowercase()) {
                val timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond
                val playerData = MegaWallsUtil.getPlayerMegawallsStats(player)!!
                val storeData = CustomData(
                    name = player,
                    time = timestamp,
                    wins = playerData.wins,
                    loses = playerData.loses,
                    kills = playerData.kills,
                    deaths = playerData.deaths,
                    finalKills = playerData.finalKills,
                    finalAssists = playerData.finalAssists,
                    finalDeaths = playerData.finalDeaths
                ).updateData(user.id)

                subject.sendMessage(
                    "=====mw小帮手=====\n" +
                    "游戏名与上一次使用该命令时不同，\n" +
                    "已覆盖当前账号储存的游戏数据。"
                )
            } else {
                PlayerData.data[user.id]!!.reset()
                subject.sendMessage(
                    "=====mw小帮手=====\n" +
                            "${player}历史数据已重置！"
                )
                return
            }
        } else {
            val timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond

            val playerData = MegaWallsUtil.getPlayerMegawallsStats(player)!!
            val storeData = CustomData(
                name = player,
                time = timestamp,
                wins = playerData.wins,
                loses = playerData.loses,
                kills = playerData.kills,
                deaths = playerData.deaths,
                finalKills = playerData.finalKills,
                finalAssists = playerData.finalAssists,
                finalDeaths = playerData.finalDeaths
            ).updateData(user.id)

            val restoredDateTime = timestamp.toDataTime()
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "当前时间: ${restoredDateTime.toString().replace("T", " ")}\n" +
                        "第一次使用，已记录当前数据！"
            )
        }
    }

    @SubCommand("mw")
    suspend fun UserCommandSender.mw(player: String) {
        if (!MinecraftUtil.hasUser(name)) {
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "查询失败!\n" +
                        "无法找到该玩家！"
            )
            return
        }

        if (PlayerData.data[user.id] != null) {
            if (PlayerData.data[user.id]!!.name.lowercase() != player.lowercase()) {
                subject.sendMessage(
                    "======mw小帮手======\n" +
                    "游戏名与上一次使用该命令时不同，检查后再试\n" +
                    "注：一个qq号只能绑定一个游戏账号！"
                )
                return
            }

            val timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond

            val newPD = MegaWallsUtil.getPlayerMegawallsStats(player)!!
            val old = PlayerData.data[user.id]!!

            val changedData = CustomData(
                name = player,
                time = timestamp,
                wins = newPD.wins - old.wins,
                loses = newPD.loses - old.loses,
                kills = newPD.kills - old.kills,
                deaths = newPD.deaths - old.deaths,
                finalKills = newPD.finalKills - old.finalKills,
                finalDeaths = newPD.finalDeaths - old.finalDeaths,
                finalAssists = newPD.finalAssists - old.finalAssists,
            )

            val classPoints = changedData.finalKills + changedData.finalAssists + changedData.wins * 10

            val msg = buildMessageChain {
                +"======mw小帮手======\n".toPlainText()
                +"player: $player\n".toPlainText()
                +"| ${old.time.toDataTime().toString().replace("T", " ")} -> ${timestamp.toDataTime().toString().replace("T", " ")} |\n".toPlainText()
                +"Kills: ${changedData.kills} | Deaths: ${changedData.deaths}]\n".toPlainText()
                +"Final Kills: ${changedData.finalKills} | Final Deaths: ${changedData.finalDeaths}\n".toPlainText()
                +"K/D Ratio: ${"%.2f".format(changedData.kdr())} | FK/D Ratio: ${"%.2f".format(changedData.fkdr())}\n".toPlainText()
                +"Wins: ${changedData.wins} | Loses: ${changedData.loses}\n".toPlainText()
                +"W/L Ratio: ${changedData.wlr()}\n".toPlainText()
                +"Final Assists: ${changedData.finalAssists} | FKA/D Ratio: ${"%.2f".format(changedData.fkadr())}\n".toPlainText()
                +"ClassPoints: $classPoints".toPlainText()
            }
        } else {
            val timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond

            val playerData = MegaWallsUtil.getPlayerMegawallsStats(player)!!
            val storeData = CustomData(
                name = player,
                time = timestamp,
                wins = playerData.wins,
                loses = playerData.loses,
                kills = playerData.kills,
                deaths = playerData.deaths,
                finalKills = playerData.finalKills,
                finalAssists = playerData.finalAssists,
                finalDeaths = playerData.finalDeaths
            ).updateData(user.id)

            val restoredDateTime = timestamp.toDataTime()
            subject.sendMessage(
                "======mw小帮手======\n" +
                    "当前时间: ${restoredDateTime.toString().replace("T", " ")}\n" +
                    "第一次使用，已记录当前数据！"
            )
        }
    }
}