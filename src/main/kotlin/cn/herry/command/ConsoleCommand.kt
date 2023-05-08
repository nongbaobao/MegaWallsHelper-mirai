package cn.herry.command

import cn.herry.config.Config
import cn.herry.Helper
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit

object ConsoleCommand : CompositeCommand(
    Helper,
    "helper-c"
) {
    @SubCommand("addgroup")
    suspend fun ConsoleCommandSender.addGroup(id: Long) {
        AbstractPermitteeId.AnyMember(id).permit(HelpCommand.permission)
        AbstractPermitteeId.AnyMember(id).permit(MegaWallsCommand.permission)
        AbstractPermitteeId.AnyMember(id).permit(ExcapesCommand.permission)
        AbstractPermitteeId.AnyMember(id).permit(UHCCommand.permission)
        sendMessage("OK")
    }

    @SubCommand("addfriend")
    suspend fun ConsoleCommandSender.addFriend(id: Long) {
        Config.friends.add(id)
        AbstractPermitteeId.AnyMember(id).permit(HelpCommand.permission)
        AbstractPermitteeId.AnyMember(id).permit(ExcapesCommand.permission)
        AbstractPermitteeId.AnyMember(id).permit(MegaWallsCommand.permission)
        sendMessage("OK")
    }

}