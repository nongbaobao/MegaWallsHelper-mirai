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
        val msg = "======mw小帮手======\n".toPlainText() +
                "/mw count -- 查询目前mw游玩人数 (主模式和faceoff模式)\n".toPlainText() +
                "/mw mwclass [id] -- 查询玩家mw职业信息\n".toPlainText() +
                "======mw小帮手======".toPlainText()

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
            val msg = "=====mw小帮手=====\n".toPlainText() +
                    "未找到用户名！请检查过后再获取!".toPlainText()

            subject.sendMessage(msg)
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
    suspend fun UserCommandSender.cp(name: String) {
        if (!MinecraftUtil.hasUser(name)) {
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "查询失败!\n" +
                        "未填写apikey或者apikey无效！\n" +
                        "======mw小帮手======"
            )
            return
        }

        val playerdata = MegaWallsUtil.getPlayerMegawallsStats(name)!!

        var cpMissing = MWClass.values().size * 2000
        var coinsMissing: Int = MWClass.values().size * 2000000 - playerdata.coins
        for ((_, value1) in playerdata.classpointsMap.entries) {
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
        coinsMissing = Math.max(0, coinsMissing)

        val msg = buildMessageChain {
            +"=====mw小帮手=====\n".toPlainText()
            +"player: $name\n".toPlainText()
            for (value in MWClass.values()) {
                val className = value.name
                val realName = value.className

                if (playerdata.classpointsMap[className.lowercase()] == null) {
                    continue
                }

                val prestige = when (playerdata.classpointsMap[className.lowercase()]!![0]) {
                    1 -> "PI"
                    2 -> "PII"
                    3 -> "PIII"
                    4 -> "PIV"
                    else -> ""
                }

                val cp = playerdata.classpointsMap[className.lowercase()]!![1]

                +"$realName $prestige: $cp\n".toPlainText()
            }
            +"Total: ${playerdata.totalClasspoints}\n".toPlainText()
            +"Missing: $cpMissing, $coinsMissing".toPlainText()
        }

        subject.sendMessage(msg)
    }

    @SubCommand("cstats")
    suspend fun UserCommandSender.cStats(name: String, classs: String) {
        if (!MinecraftUtil.hasUser(name)) {
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "查询失败!\n" +
                        "未填写apikey无效或没有此玩家！\n" +
                        "======mw小帮手======"
            )
            return
        }

        val real_class = MWClass.fromTagOrName(classs)
        if (real_class == null) {
            subject.sendMessage(
                "======mw小帮手======\n" +
                "查询失败!未找到此职业!\n"
            )
            return
        }

        val classData = MegaWallsUtil.getPlayerMegawallsClassStats(name, classs)
        if (classData != null) {

        }
    }


    @SubCommand("stats")
    suspend fun UserCommandSender.stats(name: String) {
        if (!MinecraftUtil.hasUser(name)) {
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "查询失败!\n" +
                        "未填写apikey无效或没有此玩家！\n" +
                        "======mw小帮手======"
            )
            return
        }

        val playerdata = MegaWallsUtil.getPlayerMegawallsStats(name)
        if (playerdata != null) {
            val msg = buildMessageChain {
                +"=====mw小帮手=====\n".toPlainText()
                +"player: $name\n".toPlainText()
                +"coins: ${playerdata.coins}\n".toPlainText()
                +"kills: ${playerdata.kills} | deaths: ${playerdata.deaths}\n".toPlainText()
                +"Final Kills: ${playerdata.finalKills} | Final Deaths: ${playerdata.finalDeaths}\n".toPlainText()
                +"K/D Ratio: ${"%.2f".format(playerdata.kdr)} | FK/D Ratio: ${"%.2f".format(playerdata.fkdr)}\n".toPlainText()
                +"Wins: ${playerdata.wins} | Losses: ${playerdata.losses}\n".toPlainText()
                +"Final Assists: ${playerdata.finalAssists} | FKA/D Ratio: ${"%.2f".format(playerdata.fkadr)}".toPlainText()
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