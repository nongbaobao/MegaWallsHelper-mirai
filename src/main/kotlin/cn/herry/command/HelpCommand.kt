package cn.herry.command

import cn.herry.Helper
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.UserCommandSender
import net.mamoe.mirai.message.data.toPlainText

object HelpCommand : CompositeCommand(
    Helper,
    "helper"
) {

    @SubCommand("help")
    suspend fun UserCommandSender.help() {
        val msg = "======总帮助菜单======\n"
            .plus("由于插件过多，所以再次我总结了所有插件的帮助菜单，如果该插件没有帮助菜单，则我来总结\n".toPlainText())
            .plus("Apex英雄查询插件: /apexhelp \n".toPlainText())
            .plus("B站动态/直播检测插件: /bili help\n".toPlainText())
            .plus("斗地主插件: /d me (查看个人信息) /d beg (每日500points)\n".toPlainText())
            .plus("奇怪的图片生成器插件: #ph, #bw, #5k兆, emoji合成(发两个emoji即可),\n #0, #osu, #marble, #flash, #erode\n".toPlainText())
            .plus("崩坏3往世乐土攻略查询插件: (人物名 + 乐土)即可\n".toPlainText())
            .plus("萝莉图获取插件：来只萝莉\n".toPlainText())
            .plus("音乐获取插件: #音乐 + 音乐名\n".toPlainText())
            .plus("petpet插件: 发送pet即可\n".toPlainText())
            .plus("反井字棋插件: /t help\n".toPlainText())
            .plus("谁是卧底插件: 发送\"创建谁是卧底\"后有提示\n".toPlainText())
            .plus("老婆插件: 男同！自己知道怎么用！别天天换老婆！\n".toPlainText())
            .plus("狼人杀插件: 发送\"game-start\"后有提示\n".toPlainText())
            .plus("======总帮助菜单======".toPlainText())

        subject.sendMessage(msg)
    }
}