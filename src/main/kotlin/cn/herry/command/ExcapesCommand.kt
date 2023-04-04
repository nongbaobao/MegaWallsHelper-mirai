package cn.herry.command

import cn.herry.Helper
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.UserCommandSender
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.io.File

object ExcapesCommand : CompositeCommand(
    Helper,
    "ex"
) {

    @SubCommand("random")
    suspend fun UserCommandSender.random() {
        val fileNumbers = File("data/yeti").listFiles()!!.size
        val randoms = (1..fileNumbers).random()
        val resource = File("data/yeti/${randoms}.png").toExternalResource()
        subject.sendImage(resource)
    }

    @SubCommand("pic")
    suspend fun UserCommandSender.pictrue(num: Int) {
        val fileNumbers = File("data/yeti").listFiles()!!.size

        if (num > fileNumbers) {
            subject.sendMessage("未找到此编号的图片！")
            return
        }

        val resource = File("data/yeti/${num}.png").toExternalResource()
        subject.sendImage(resource)
    }

}