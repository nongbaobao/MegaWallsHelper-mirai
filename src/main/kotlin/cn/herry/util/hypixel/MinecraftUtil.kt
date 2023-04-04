package cn.herry.util.hypixel

import cn.herry.util.other.WebUtil
import cn.hutool.json.JSONUtil
import okhttp3.OkHttpClient
import okhttp3.Request

object MinecraftUtil {

    fun hasUser(user: String): Boolean {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.mojang.com/users/profiles/minecraft/$user")
            .build()
        val response = client.newCall(request).execute()
        val code = response.code
        response.close()
        return code != 204
    }

    fun getUUID(user: String): String {
        if (hasUser(user)) {
            val json = JSONUtil.parseObj(WebUtil.getJson("https://api.mojang.com/users/profiles/minecraft/$user"))
            return json.getStr("id")
        }

        return ""
    }

}