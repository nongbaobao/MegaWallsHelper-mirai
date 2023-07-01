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

        if (MegaWallsUtil.hasData(name)) {
            // has data

            when (date) {
                "daily" -> {
                    if (MegaWallsUtil.hasDataByDate(name, LocalDate.now().minusDays(1))) {

                    } else {
                        MegaWallsUtil.updateMegaWallsPlayerData(name)
                        subject.sendMessage(
                            "=====小帮手=====\n".toPlainText() +
                                    "未找到每日数据！请等待一天后再试！"
                        )
                    }
                }

                "weekly" -> {

                }

                "monthly" -> {

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
            val megaWallsPlayerData = MegaWallsUtil.getMegaWallsPlayerData(name)
            if (megaWallsPlayerData != null) {
                val document: Document = Document("name", megaWallsPlayerData.name)
                    .append("rank", megaWallsPlayerData.rank)
                    .append("date", megaWallsPlayerData.date)
                    .append("finalKills", megaWallsPlayerData.finalKills)
                    .append("finalAssists", megaWallsPlayerData.finalAssists)
                    .append("finalDeaths", megaWallsPlayerData.finalDeaths)
                    .append("wins", megaWallsPlayerData.wins)
                    .append("losses", megaWallsPlayerData.losses)

                MongoUtil.writeDocumentToCollection("mw", "data", document)
            } else {
                subject.sendMessage(
                    "=====mw小帮手=====\n".toPlainText() +
                            "获取hypixel信息异常！请联系管理员".toPlainText()
                )
            }
        }
    }

}