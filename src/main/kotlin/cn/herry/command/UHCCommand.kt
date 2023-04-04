package cn.herry.command

import cn.herry.Helper
import cn.herry.util.hypixel.UHCUtil
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.UserCommandSender
import net.mamoe.mirai.message.data.toPlainText

object UHCCommand : CompositeCommand(
    Helper,
    "uhc"
) {

    @SubCommand("count")
    suspend fun UserCommandSender.getCount() {

        val uhcCount: IntArray? = UHCUtil.uhcCounter

        if (uhcCount == null) {
            subject.sendMessage(
                "======mw小帮手======\n" +
                        "查询失败!\n" +
                        "未填写apikey或者apikey无效！\n" +
                        "======mw小帮手======"
            )
        }

        val msg = "======mw小帮手======\n".toPlainText() +
                "| UHC Solo: ${uhcCount!![0]}\n".toPlainText() +
                "| UHC Teams: ${uhcCount[1]}".toPlainText()

        subject.sendMessage(msg)
    }


}