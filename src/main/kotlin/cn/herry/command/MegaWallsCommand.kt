package cn.herry.command

import cn.herry.Helper
import cn.herry.util.hypixel.game.megawalls.MWClass
import cn.herry.util.hypixel.game.megawalls.MegaWallsUtil
import cn.herry.util.other.MinecraftUtil
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.UserCommandSender
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.message.data.toPlainText
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.min


object MegaWallsCommand : CompositeCommand(
    Helper,
    "mw"
) {

    @SubCommand("help")
    suspend fun UserCommandSender.help() {
        val msg = buildMessageChain {
            +"======mw小帮手======\n".toPlainText()
            +"注: [] 为必填参数 <> 为可选参数\n".toPlainText()
            +"/mw count -- 查询目前mw游玩人数 (主模式和faceoff模式)\n".toPlainText()
            +"/mw mwclass [id] -- 查询玩家mw职业信息\n".toPlainText()
            +"/mw cp [id] [old/new] -- 查询玩家mw职业分数信息\n".toPlainText()
            +"/mw stats [id] <class> -- 查询玩家mw(指定职业)数据".toPlainText()
        }

        subject.sendMessage(msg)
    }

    @SubCommand("class")
    suspend fun UserCommandSender.mwclass(name: String) {
        if (MinecraftUtil.hasUser(name)) {
            // 获取图片
            val client = OkHttpClient()
            val request = Request.Builder().get()
                .url("https://gen.plancke.io/mw/$name/2.png")
                .build()
            val response = client.newCall(request).execute()
            val inputStream = response.body!!.byteStream()

            val uploadImage: Image = subject.uploadImage(inputStream)
            val msg: Message = "player: $name \n".toPlainText() + uploadImage
            subject.sendMessage(msg)
        } else {
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "查询失败!\n" +
                        "无法找到该玩家！"
            )
            return
        }
    }

    @SubCommand("count")
    suspend fun UserCommandSender.count() {
        val mwCount: IntArray? = MegaWallsUtil.mwCounter

        if (mwCount == null) {
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "查询失败!\n" +
                        "未填写apikey或者apikey无效！\n" +
                        "======mw小帮手======"
            )
        }

        val msg = "======mw小帮手======\n".toPlainText() +
                "| Megawalls Standard: ${mwCount!![0]}\n".toPlainText() +
                "| Megawalls Faceoff: ${mwCount[1]}\n".toPlainText()

        subject.sendMessage(msg)
    }

    @SubCommand("cp")
    suspend fun UserCommandSender.cp(name: String, old: Boolean = false) {
        if (!MinecraftUtil.hasUser(name)) {
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "查询失败!\n" +
                        "无法找到该玩家！"
            )
            return
        }

        val playerData = MegaWallsUtil.getPlayerMegawallsStats(name, old)!!

        var cpMissing = MWClass.values().size * 2000
        var coinsMissing: Int = MWClass.values().size * 2000000 - playerData.coins
        for ((_, value1) in playerData.classpointsMap.entries) {
            cpMissing -= min(value1[1], 2000)
            val prestige = value1[0]
            if (prestige == 4) {
                coinsMissing -= 2000000
            } else if (prestige == 3) {
                coinsMissing -= 1250000
            } else if (prestige == 2) {
                coinsMissing -= 750000
            } else if (prestige == 1) {
                coinsMissing -= 250000
            }
        }
        coinsMissing = 0.coerceAtLeast(coinsMissing)

        val msg = buildMessageChain {
            +"=====mw小帮手=====\n".toPlainText()
            +"player: $name\n".toPlainText()
            for (value in MWClass.values()) {
                val className = value.name
                val realName = value.className

                if (playerData.classpointsMap[className.lowercase()] == null) {
                    continue
                }

                val prestige = when (playerData.classpointsMap[className.lowercase()]!![0]) {
                    1 -> "[PI]"
                    2 -> "[PII]"
                    3 -> "[PIII]"
                    4 -> "[PIV]"
                    5 -> "[PV]"
                    else -> ""
                }

                val cp = playerData.classpointsMap[className.lowercase()]!![1]

                +"$realName $prestige: $cp\n".toPlainText()
            }
            +"Total: ${playerData.totalClasspoints}\n".toPlainText()
            +"Missing: $cpMissing, $coinsMissing".toPlainText()
        }

        subject.sendMessage(msg)
    }

    @SubCommand("stats")
    suspend fun UserCommandSender.stats(name: String, classs: String = "all", old: Boolean = false) {
        if (!MinecraftUtil.hasUser(name)) {
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "查询失败!\n" +
                        "未填写apikey无效或没有此玩家！\n" +
                        "======mw小帮手======"
            )
            return
        }

        if (classs != "all") {
            val realClass = MWClass.fromTagOrName(classs)
            if (realClass == null) {
                subject.sendMessage(
                    "======mw小帮手======\n" +
                            "查询失败!未找到此职业!\n"
                )
                return
            }

            val classData = MegaWallsUtil.getPlayerMegawallsClassStats(name, realClass.className)
            if (classData != null) {
                val msg = buildMessageChain {
                    +"=====mw小帮手=====\n".toPlainText()
                    +"player: $name -- ${realClass.className} Stats\n".toPlainText()
                    +"Kills: ${classData.classnameKills} | Deaths: ${classData.classnameDeaths} \n".toPlainText()
                    +"Final Kills: ${classData.classnameFinalKills} | Final Deaths: ${classData.classnameFinalDeaths}\n".toPlainText()
                    +"K/D Ratio: ${"%.2f".format(classData.kdr)} | FK/D Ratio: ${"%.2f".format(classData.fkdr)}\n".toPlainText()
                    +"Wins: ${classData.classnameWins} | Losses: ${classData.classnameLosses}\n".toPlainText()
                    +"Final Assists: ${classData.classnameFinalAssists} | FKA/D Ratio: ${"%.2f".format(classData.fkadr)}\n".toPlainText()
                    +"Games Played: ${classData.gamesPlayed} | FK/game: ${"%.2f".format(classData.fkpergame)}\n".toPlainText()
                    +"Classpoints: ${classData.classpoints} | PlayTime: ${classData.classnameTimePlayed}\n".toPlainText()
                    +"Kit: ${MegaWallsUtil.intToRoman(classData.skillLevelA)}  ${MegaWallsUtil.intToRoman(classData.skillLevelB)}  ${MegaWallsUtil.intToRoman(classData.skillLevelC)}  ${MegaWallsUtil.intToRoman(classData.skillLevelD)}  ${MegaWallsUtil.intToRoman(classData.skillLevelG)}\n".toPlainText()
                    +"Prestige: ${MegaWallsUtil.intToRoman(classData.prestige)} Echest rows: ${classData.enderChestRows}\n".toPlainText()
                    +"Selected skin: ${classData.chosenSkinClass}".toPlainText()
                }

                subject.sendMessage(msg)
            }
        } else {
            val playerData = MegaWallsUtil.getPlayerMegawallsStats(name, old)
            if (playerData != null) {
                val msg = buildMessageChain {
                    +"=====mw小帮手=====\n".toPlainText()
                    +"player: $name\n".toPlainText()
                    +"coins: ${playerData.coins}\n".toPlainText()
                    +"kills: ${playerData.kills} | deaths: ${playerData.deaths}\n".toPlainText()
                    +"Final Kills: ${playerData.finalKills} | Final Deaths: ${playerData.finalDeaths}\n".toPlainText()
                    +"K/D Ratio: ${"%.2f".format(playerData.kdr)} | FK/D Ratio: ${"%.2f".format(playerData.fkdr)}\n".toPlainText()
                    +"Wins: ${playerData.wins} | Losses: ${playerData.loses}\n".toPlainText()
                    +"Final Assists: ${playerData.finalAssists} | FKA/D Ratio: ${"%.2f".format(playerData.fkadr)}".toPlainText()
                }

                subject.sendMessage(msg)
            } else {
                subject.sendMessage(
                    "======mw小帮手======\n" +
                            "查询失败!\n" +
                            "出现未知错误！\n" +
                            "======mw小帮手======"
                )
            }
        }
    }



}