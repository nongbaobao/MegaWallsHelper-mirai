package cn.herry.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object MongoDBConfig : AutoSavePluginConfig("MongoDBConfig") {
    val serverIp: String by value()
    val serverPort: String by value()
    val username: String by value()
    val password: String by value()
}