package cn.herry.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
    val apiKey: String by value()
    val friends: MutableList<Long> by value()
}