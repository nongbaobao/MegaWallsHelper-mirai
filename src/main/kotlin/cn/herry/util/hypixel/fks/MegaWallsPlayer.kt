package cn.herry.util.hypixel.fks

data class MegaWallsPlayer(
    val name: String = "", // player - playername
    val rank: String = "", // player - newPackageRank
    val date: String = "", // date
    val finalKills: Int = 0, // player - stats - walls3 - final_kills_standard
    val finalAssists: Int = 0, // player - stats - walls3 - final_assists_standard
    val finalDeaths: Int = 0, // player - stats - walls3 - final_deaths_standard
    val wins: Int = 0, // player - stats - walls3 - wins_standard
    val losses: Int = 0, // player - stats - walls3 - losses_standard
)