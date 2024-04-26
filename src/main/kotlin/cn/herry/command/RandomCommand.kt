package cn.herry.command

import cn.herry.Helper
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.UserCommandSender
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.io.File

object RandomCommand : CompositeCommand(
    Helper,
    "random"
) {

    @SubCommand("ontay")
    suspend fun UserCommandSender.ontay() {
        val fileNumbers = File("data/ontay").listFiles()!!.size
        val randoms = (1..fileNumbers).random()
        val resource = File("data/ontay/${randoms}.png").toExternalResource()
        subject.sendImage(resource)
    }

    @SubCommand("yeti")
    suspend fun UserCommandSender.yeti() {
        val fileNumbers = File("data/yeti").listFiles()!!.size
        val randoms = (1..fileNumbers).random()
        val resource = File("data/yeti/${randoms}.png").toExternalResource()
        subject.sendImage(resource)
    }


}