package cn.herry

import cn.herry.command.*
import cn.herry.config.Config
import cn.herry.util.hypixel.MegaWallsUtil
import cn.herry.util.mongoDB.MongoUtil
import cn.hutool.cron.CronUtil
import cn.hutool.cron.task.Task
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object Helper : KotlinPlugin(
    JvmPluginDescription(
        id = "cn.herry.Helper",
        name = "ConsoleHelper",
        version = "0.1.0",
    ) {
        author("herry")
    }
) {
    override fun onEnable() {
        logger.info { "Plugin Loaded" }

        Config.reload()

        HelpCommand.register()
        ConsoleCommand.register()
        MegaWallsCommand.register()
        ExcapesCommand.register()

        CronUtil.schedule("0 0 12 * * ?", Task {
            val users = MongoUtil.getUsedUser().iterator()
            users.forEach {
                MegaWallsUtil.getPlayerDataAndSave(it["name"] as String)
            }

            logger.info("已获取今日玩家的数据！")
        })
        CronUtil.start()
    }

    override fun onDisable() {
        MongoUtil.client.close()
    }
}

