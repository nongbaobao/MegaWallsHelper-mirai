package cn.herry.command

import cn.herry.Helper
import cn.herry.util.other.MinecraftUtil
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.UserCommandSender

object SessionsCommand : CompositeCommand (
    Helper,
    "sessions"
){

    @SubCommand("mw")
    fun UserCommandSender.mw(player: String) {
        if (!MinecraftUtil.hasUser(player)) {
            return
        }

        val uuid = MinecraftUtil.getUUID(player)
    }
}