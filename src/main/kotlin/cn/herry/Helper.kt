package cn.herry

import cn.herry.command.*
import cn.herry.config.Config
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

        // HelpCommand.register()
        ConsoleCommand.register()
        MegaWallsCommand.register()
        ExcapesCommand.register()
        UHCCommand.register()
    }

    override fun onDisable() {}
}

