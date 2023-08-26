package cn.herry.command

import cn.herry.Helper
import cn.herry.command.HypixelCounterCommand.bsg
import cn.herry.util.hypixel.other.HypixelCounterUtil
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.UserCommandSender

object HypixelCounterCommand : CompositeCommand(
    Helper,
    "count"
) {
    @SubCommand("mw")
    suspend fun UserCommandSender.mw() {
        subject.sendMessage(HypixelCounterUtil.mwCounter)
    }

    @SubCommand("uhc")
    suspend fun UserCommandSender.uhc() {
        subject.sendMessage(HypixelCounterUtil.uhcCounter)
    }

    @SubCommand("total")
    suspend fun UserCommandSender.total() {
        subject.sendMessage(HypixelCounterUtil.totalCounter)
    }

    @SubCommand("sw")
    suspend fun UserCommandSender.sw() {
        subject.sendMessage(HypixelCounterUtil.swCounter)
    }

    @SubCommand("bsg")
    suspend fun UserCommandSender.bsg() {
        subject.sendMessage(HypixelCounterUtil.bsgCounter)
    }


}