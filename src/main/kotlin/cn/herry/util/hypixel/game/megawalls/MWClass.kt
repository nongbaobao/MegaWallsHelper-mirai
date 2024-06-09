package cn.herry.util.hypixel.game.megawalls

enum class MWClass(val className: String, val tagLowerCase: String) {

    ARCANIST("Arcanist", "arc"),
    ASSASSIN("Assassin", "asn"),
    AUTOMATON("Automaton", "atn"),
    BLAZE("Blaze", "bla"),
    COW("Cow", "cow"),
    CREEPER("Creeper", "cre"),
    DREADLORD("Dreadlord", "dre"),
    ENDERMAN("Enderman", "end"),
    GOLEM("Golem", "gol"),
    HEROBRINE("Herobrine", "hbr"),
    HUNTER("Hunter", "hun"),
    MOLEMAN("Moleman", "mol"),
    PHOENIX("Phoenix", "phx"),
    PIGMAN("Pigman", "pig"),
    PIRATE("Pirate", "pir"),
    RENEGADE("Renegade", "ren"),
    SHAMAN("Shaman", "sha"),
    SHARK("Shark", "srk"),
    SKELETON("Skeleton", "ske"),
    SNOWMAN("Snowman", "sno"),
    SPIDER("Spider", "spi"),
    SQUID("Squid", "squ"),
    WEREWOLF("Werewolf", "wer"),
    ANGEL("Angel", "ang"),
    SHEEP("Sheep", "shp"),
    DRAGON("Dragon", "drg"),
    ZOMBIE("Zombie", "zom");

    companion object {
        fun fromTagOrName(nameIn: String): MWClass? {
            for (mwClass in values()) {
                if (nameIn.equals(mwClass.tagLowerCase, ignoreCase = true) || nameIn.equals(
                        mwClass.className,
                        ignoreCase = true
                    )
                ) {
                    return mwClass
                }
            }
            return null
        }
    }

}