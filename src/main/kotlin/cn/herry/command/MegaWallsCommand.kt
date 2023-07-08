package cn.herry.command

import cn.herry.Helper
import cn.herry.util.hypixel.game.MegaWallsUtil
import cn.herry.util.mongoDB.MongoUtil
import cn.herry.util.other.MinecraftUtil
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.UserCommandSender
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.toPlainText
import okhttp3.OkHttpClient
import okhttp3.Request
import org.bson.Document
import java.time.LocalDate

object MegaWallsCommand : CompositeCommand(
    Helper,
    "mw"
) {

    @SubCommand("help")
    suspend fun UserCommandSender.help() {
        val msg = "======mw小帮手======\n".toPlainText() +
                "/mw count -- 查询目前mw游玩人数 (主模式和faceoff模式)\n".toPlainText() +
                "/mw mwclass [id] -- 查询玩家mw职业信息\n".toPlainText() +
                "/mw fks [id] [args] -- 查询玩家每日/每星期/每月/每年的FKDR信息".toPlainText() +
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

    @SubCommand("fks")
    suspend fun UserCommandSender.fks(name: String, date: String) {
        if (!MinecraftUtil.hasUser(name)) {
            val msg = "=====mw小帮手=====\n".toPlainText() +
                    "未找到用户名！请检查过后再获取!".toPlainText()

            subject.sendMessage(msg)
        }

        if (MegaWallsUtil.hasDataByDate(name, LocalDate.now())) {
            MegaWallsUtil.updateMegaWallsPlayerData(name)
        }

        if (MegaWallsUtil.hasData(name)) {
            // has data
            when (date) {
                "daily" -> {
                    if (MegaWallsUtil.hasDataByDate(name, LocalDate.now().minusDays(1))) {
                        val today = MegaWallsUtil.getMegaWallsPlayerDataByDate(name, LocalDate.now())!!
                        val yesterday = MegaWallsUtil.getMegaWallsPlayerDataByDate(name, LocalDate.now().minusDays(1))!!

                        // based data
                        val name: String = today.name
                        val rank: String = today.rank
                        val date: String = "${yesterday.date} -> ${today.date}"
                        val finalKills: Int = yesterday.finalKills - today.finalKills
                        val finalAssists: Int = yesterday.finalAssists - today.finalDeaths
                        val finalDeaths: Int = yesterday.finalDeaths - today.finalDeaths
                        val wins: Int = yesterday.wins - today.wins
                        val losses: Int = yesterday.losses - today.losses

                        // 处理后的数据

                    } else {
                        MegaWallsUtil.updateMegaWallsPlayerData(name)
                        subject.sendMessage(
                            "=====小帮手=====\n".toPlainText() +
                                    "未找到每日数据！请等待一天后再试！".toPlainText()
                        )
                    }
                }

                "weekly" -> {
                    if (MegaWallsUtil.hasDataByDate(name, LocalDate.now().minusWeeks(1))) {
                        val today = MegaWallsUtil.getMegaWallsPlayerDataByDate(name, LocalDate.now())
                        val lastWeek = MegaWallsUtil.getMegaWallsPlayerDataByDate(name, LocalDate.now().minusWeeks(1))

                    } else {
                        MegaWallsUtil.updateMegaWallsPlayerData(name)
                        subject.sendMessage(
                            "=====小帮手=====\n".toPlainText() +
                                    "未找到每星期数据！请等待一星期后再试！".toPlainText()
                        )
                    }
                }

                "monthly" -> {
                    if (MegaWallsUtil.hasDataByDate(name, LocalDate.now().minusMonths(1))) {
                        val today = MegaWallsUtil.getMegaWallsPlayerDataByDate(name, LocalDate.now())
                        val lastMonth = MegaWallsUtil.getMegaWallsPlayerDataByDate(name, LocalDate.now().minusMonths(1))

                    } else {
                        MegaWallsUtil.updateMegaWallsPlayerData(name)
                        subject.sendMessage(
                            "=====小帮手=====\n".toPlainText() +
                                    "未找到每星期数据！请等待一个月后再试！".toPlainText()
                        )
                    }
                }

                else -> {
                    subject.sendMessage(
                        "=====mw小帮手=====\n".toPlainText() +
                                "错误的用法！正确使用方法为/mw fks name daily/weekly/monthly".toPlainText()
                    )
                }
            }

        } else {
            // by first
            if (MegaWallsUtil.writeDataByFirst(name)) {
                subject.sendMessage(
                    "=====mw小帮手=====\n".toPlainText() +
                            "第一次使用，已获取今日数据并记录！".toPlainText()
                )
            } else {
                subject.sendMessage(
                    "=====mw小帮手=====\n".toPlainText() +
                            "获取hypixel信息异常！请联系管理员".toPlainText()
                )
            }
        }
    }

}