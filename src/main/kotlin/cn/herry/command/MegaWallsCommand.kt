package cn.herry.command

import cn.herry.Helper
import cn.herry.util.hypixel.MegaWallsUtil
import cn.herry.util.hypixel.MinecraftUtil
import cn.herry.util.mongoDB.MongoUtil
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.UserCommandSender
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.toPlainText
import okhttp3.OkHttpClient
import okhttp3.Request
import java.math.RoundingMode
import java.text.DecimalFormat
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
                "| Megawalls Faceoff: ${mwCount[1]}\n\n".toPlainText()

        subject.sendMessage(msg)
    }

    @SubCommand("fks")
    suspend fun UserCommandSender.fks(name: String, args: String) {
        if (MinecraftUtil.hasUser(name)) {
            val playerData = MongoUtil.getDataByDate(name, LocalDate.now().toString())
            if (MongoUtil.isFirstTime(name)) {
                if (!MongoUtil.hasDaily(name)) {
                        subject.sendMessage("=====mw小帮手======\n".toPlainText() +
                            "您可能是第一次使用！\n请等待重置时间后再获取您的daily/monthly/yearly数据!")
                }

                if (playerData != null) {
                    when (args) {
                        // 每日
                        "daily" -> {
                            if (MongoUtil.hasDaily(name)) {
                                val yesterdayData =
                                    MongoUtil.getDataByDate(name, LocalDate.now().minusDays(1).toString())

                                // base data
                                val finalKills = playerData.finalKills - yesterdayData!!.finalKills
                                val finalDeaths = playerData.finalDeaths - yesterdayData.finalDeaths
                                val wins = playerData.wins - yesterdayData.wins
                                val losses = playerData.losses - yesterdayData.losses

                                // fkd / wl
                                val fkd: String = if (finalKills == 0 && finalDeaths == 0) {
                                    "0"
                                } else if (finalKills != 0 && finalDeaths == 0) {
                                    finalKills.toString()
                                } else {
                                    getFloatNoMoreThan3Digits((finalKills / finalDeaths).toDouble())
                                }

                                val wl: String = if ((wins == 0 && losses == 0)) {
                                    "0"
                                } else if (wins != 0 && losses == 0) {
                                    wins.toString()
                                } else {
                                    getFloatNoMoreThan3Digits((wins / losses).toDouble())
                                }

                                val msg = "=====mw小帮手=====\n".toPlainText() +
                                        "玩家: $name\n".toPlainText() +
                                        "| 日期: ${
                                            LocalDate.now().minusDays(1)
                                        } -> ${LocalDate.now()}\n".toPlainText() +
                                        "| 终杀/终死: $finalKills / $finalDeaths\n".toPlainText() +
                                        "| FKDR: $fkd\n".toPlainText() +
                                        "| 胜场/败场: $wins / $losses\n".toPlainText() +
                                        "| W/L: $wl".toPlainText()

                                subject.sendMessage(msg)
                            } else {
                                subject.sendMessage(
                                    "=====mw小帮手=====\n".toPlainText() +
                                            "未检测到您昨日的数据;w; \n如果为第一次使用，请等待一天后再获取".toPlainText()
                                )
                            }
                        }

                        // 每周
                        "weekly" -> {
                            if (MongoUtil.hasWeekly(name)) {
                                val yesterdayData =
                                    MongoUtil.getDataByDate(name, LocalDate.now().minusWeeks(1).toString())

                                // base data
                                val finalKills = playerData.finalKills - yesterdayData!!.finalKills
                                val finalDeaths = playerData.finalDeaths - yesterdayData.finalDeaths
                                val wins = playerData.wins - yesterdayData.wins
                                val losses = playerData.losses - yesterdayData.losses

                                // fkd / wl
                                val fkd: String = if (finalKills == 0 && finalDeaths == 0) {
                                    "0"
                                } else if (finalKills != 0 && finalDeaths == 0) {
                                    finalKills.toString()
                                } else {
                                    getFloatNoMoreThan3Digits((finalKills / finalDeaths).toDouble())
                                }

                                val wl: String = if ((wins == 0 && losses == 0)) {
                                    "0"
                                } else if (wins != 0 && losses == 0) {
                                    wins.toString()
                                } else {
                                    getFloatNoMoreThan3Digits((wins / losses).toDouble())
                                }

                                val msg = "=====mw小帮手=====\n".toPlainText() +
                                        "玩家: $name\n".toPlainText() +
                                        "| 日期: ${
                                            LocalDate.now().minusWeeks(1)
                                        } -> ${LocalDate.now()}\n".toPlainText() +
                                        "| 终杀/终死: $finalKills / $finalDeaths\n".toPlainText() +
                                        "| FKDR: $fkd\n".toPlainText() +
                                        "| 胜场/败场: $wins / $losses\n".toPlainText() +
                                        "| W/L: $wl".toPlainText()

                                subject.sendMessage(msg)
                            } else {
                                subject.sendMessage(
                                    "=====mw小帮手=====\n".toPlainText() +
                                            "未检测到您上周的数据;w; \n如果为第一次使用，请等待一周后再获取".toPlainText()
                                )
                            }
                        }

                        // 每月
                        "monthly" -> {
                            if (MongoUtil.hasMonthly(name)) {
                                val monthAgoData =
                                    MongoUtil.getDataByDate(name, LocalDate.now().minusMonths(1).toString())

                                // base data
                                val finalKills = playerData.finalKills - monthAgoData!!.finalKills
                                val finalDeaths = playerData.finalDeaths - monthAgoData.finalDeaths
                                val wins = playerData.wins - monthAgoData.wins
                                val losses = playerData.losses - monthAgoData.losses

                                // fkd / wl
                                val fkd: String = if (finalKills == 0 && finalDeaths == 0) {
                                    "0"
                                } else if (finalKills != 0 && finalDeaths == 0) {
                                    finalKills.toString()
                                } else {
                                    getFloatNoMoreThan3Digits((finalKills / finalDeaths).toDouble())
                                }

                                val wl: String = if ((wins == 0 && losses == 0)) {
                                    "0"
                                } else if (wins != 0 && losses == 0) {
                                    wins.toString()
                                } else {
                                    getFloatNoMoreThan3Digits((wins / losses).toDouble())
                                }

                                val msg = "=====mw小帮手=====\n".toPlainText() +
                                        "玩家: $name\n".toPlainText() +
                                        "| 日期: ${
                                            LocalDate.now().minusMonths(1)
                                        } -> ${LocalDate.now()}\n".toPlainText() +
                                        "| 终杀/终死: $finalKills / $finalDeaths\n".toPlainText() +
                                        "| FKDR: $fkd\n".toPlainText() +
                                        "| 胜场/败场: $wins / $losses".toPlainText() +
                                        "| W/L: $wl".toPlainText()

                                subject.sendMessage(msg)

                            } else {
                                subject.sendMessage(
                                    "=====mw小帮手=====\n".toPlainText() +
                                            "未检测到您上个月的数据;w; \n如果为第一次使用，请等待一个月后再获取".toPlainText()
                                )
                            }
                        }

                        // 每年
                        "yearly" -> {
                            if (MongoUtil.hasYearly(name)) {
                                val yearAgoDate =
                                    MongoUtil.getDataByDate(name, LocalDate.now().minusYears(1).toString())

                                // base data
                                val finalKills = playerData.finalKills - yearAgoDate!!.finalKills
                                val finalDeaths = playerData.finalDeaths - yearAgoDate.finalDeaths
                                val wins = playerData.wins - yearAgoDate.wins
                                val losses = playerData.losses - yearAgoDate.losses

                                // fkd / wl
                                val fkd: String = if (finalKills == 0 && finalDeaths == 0) {
                                    "0"
                                } else if (finalKills != 0 && finalDeaths == 0) {
                                    finalKills.toString()
                                } else {
                                    getFloatNoMoreThan3Digits((finalKills / finalDeaths).toDouble())
                                }

                                val wl: String = if ((wins == 0 && losses == 0)) {
                                    "0"
                                } else if (wins != 0 && losses == 0) {
                                    wins.toString()
                                } else {
                                    getFloatNoMoreThan3Digits((wins / losses).toDouble())
                                }

                                val msg = "=====mw小帮手=====\n".toPlainText() +
                                        "玩家: $name\n".toPlainText() +
                                        "| 日期: ${
                                            LocalDate.now().minusDays(1)
                                        } -> ${LocalDate.now()}\n".toPlainText() +
                                        "| 终杀/终死: $finalKills / $finalDeaths\n".toPlainText() +
                                        "| FKDR: $fkd\n".toPlainText() +
                                        "| 胜场/败场: $wins / $losses".toPlainText() +
                                        "| W/L: $wl".toPlainText()

                                subject.sendMessage(msg)

                            } else {
                                subject.sendMessage(
                                    "=====mw小帮手=====\n".toPlainText() +
                                            "未检测到您上一年的数据;w; \n如果为第一次使用，请等待一年后再获取".toPlainText()
                                )
                            }
                        }
                        else -> {
                            subject.sendMessage("=====mw小帮手=====\n".toPlainText() +
                                    "错误的参数！最后一项只能为daily/weekly/monthly/yearly".toPlainText())
                        }
                    }
                }
            } else {
                MegaWallsUtil.getPlayerDataAndSave(name)
                val msg = "=====mw小帮手=====\n".toPlainText() +
                        "检测到这是第一次使用！已默认获取${name}的数据并保存，重置时间为1天！"

                subject.sendMessage(msg)
            }
        } else {
            val msg = "=====mw小帮手=====\n".toPlainText() +
                    "未找到用户名！请检查过后再获取!".toPlainText()

            subject.sendMessage(msg)
        }
    }

    private fun getFloatNoMoreThan3Digits(number: Double): String {
        val format = DecimalFormat("#.###")
        format.roundingMode = RoundingMode.FLOOR
        return format.format(number)
    }


}