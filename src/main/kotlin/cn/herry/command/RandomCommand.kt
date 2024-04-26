package cn.herry.command

import cn.herry.Helper
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.UserCommandSender
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.io.File

object RandomCommand : CompositeCommand(
    Helper,
    "ontay"
) {

    @SubCommand("random")
    suspend fun UserCommandSender.random() {
        val fileNumbers = File("data/ontay").listFiles()!!.size
        val randoms = (1..fileNumbers).random()
        val resource = File("data/ontay/${randoms}.png").toExternalResource()
        subject.sendImage(resource)
    }

}