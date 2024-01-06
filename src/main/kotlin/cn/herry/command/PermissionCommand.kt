package cn.herry.command

import cn.herry.config.Config
import cn.herry.Helper
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.command.UserCommandSender
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit

object PermissionCommand : CompositeCommand(
    Helper,
    "helper-c"
) {
    @SubCommand("addgroup")
    suspend fun ConsoleCommandSender.addGroup(id: Long) {
        AbstractPermitteeId.AnyMember(id).permit(MegaWallsCommand.permission)
        AbstractPermitteeId.AnyMember(id).permit(ExcapesCommand.permission)
        AbstractPermitteeId.AnyMember(id).permit(UHCCommand.permission)
        AbstractPermitteeId.AnyMember(id).permit(HypixelCounterCommand.permission)
        AbstractPermitteeId.AnyMember(id).permit(SessionsCommand.permission)
        sendMessage("OK")
    }

    @SubCommand("addfriend")
    suspend fun ConsoleCommandSender.addFriend(id: Long) {
        Config.friends.add(id)
        AbstractPermitteeId.AnyMember(id).permit(ExcapesCommand.permission)
        AbstractPermitteeId.AnyMember(id).permit(MegaWallsCommand.permission)
        sendMessage("OK")
    }

    @SubCommand("addgroup")
    suspend fun UserCommandSender.addGroup(id: Long) {
        if (user.id == 2805733637) {
            AbstractPermitteeId.AnyMember(id).permit(MegaWallsCommand.permission)
            AbstractPermitteeId.AnyMember(id).permit(ExcapesCommand.permission)
            AbstractPermitteeId.AnyMember(id).permit(UHCCommand.permission)
            AbstractPermitteeId.AnyMember(id).permit(HypixelCounterCommand.permission)
            AbstractPermitteeId.AnyMember(id).permit(SessionsCommand.permission)
            sendMessage("OK")
        }
    }




}