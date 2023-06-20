package cn.herry.util.hypixel.fks

data class MegaWallsPlayer(
    val name: String = "",
    val rank: String = "",
    val finalKills: Int = 0,
    val finalAssists: Int = 0,
    val finalDeaths: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
)