package jakdb.data.mysql

import jakdb.data.items.JAKDBGuild
import jakdb.data.items.JAKDBUser
import jakdb.utils.debug
import java.sql.SQLException
import jakdb.utils.*
import java.sql.ResultSet

@Synchronized
fun createTables() {
    val users = "CREATE TABLE IF NOT EXISTS `jadb_users` (" +
            "`uuid` VARCHAR(50) NULL DEFAULT UUID()," +
            "`discordId` BIGINT NULL DEFAULT NULL," +
            "`global_COINS` BIGINT NULL DEFAULT '0'," +
            "`gold` BIGINT NULL DEFAULT '0'," +
            "`global_XP` BIGINT NULL DEFAULT '0'," +
            "`global_LVL` BIGINT NULL DEFAULT '0'," +
            "`rank` INT NULL DEFAULT '0'," +
            "`isBanned` TINYINT NULL DEFAULT '0'," +
            "`banReason` VARCHAR(50) NULL DEFAULT 'Not banned'," +
            "`banTime` TIMESTAMP NULL DEFAULT NOW()," +
            "`regTime` TIMESTAMP NULL DEFAULT NOW()," +
            "UNIQUE INDEX `uuid` (`uuid`)" +
            ") COLLATE='utf8mb4_unicode_ci';"
    val guilds = "CREATE TABLE IF NOT EXISTS `jadb_guilds` (" +
            "`uuid` VARCHAR(50) NULL DEFAULT UUID()," +
            "`discordId` BIGINT NULL DEFAULT NULL," +
            "`isBanned` TINYINT NULL DEFAULT '0'," +
            "`banReason` VARCHAR(50) NULL DEFAULT 'Not banned'," +
            "`banTime` TIMESTAMP NULL DEFAULT NOW()," +
            "`regTime` TIMESTAMP NULL DEFAULT NOW()," +
            "`lang_ID` INT(11) NULL DEFAULT 0," +
            "UNIQUE INDEX `uuid` (`uuid`)" +
            ") COLLATE='utf8mb4_unicode_ci';"
    val timers = "CREATE TABLE IF NOT EXISTS `jadb_timers` (" +
            "`uuid` VARCHAR(50) NULL DEFAULT UUID()," +
            "`discordId` BIGINT NULL DEFAULT NULL," +
            "`type` INT(11) NULL DEFAULT 0," +
            "`timeLeft` BIGINT NULL DEFAULT '0'" +
            ") COLLATE='utf8mb4_unicode_ci';"
    val userSettings = "CREATE TABLE IF NOT EXISTS `jadb_us_set` (" +
            "`uuid` VARCHAR(50) NULL DEFAULT UUID()," +
            "`discordId` BIGINT NULL DEFAULT NULL," +
            "`profMsg` TEXT(200) NULL DEFAULT NULL," +
            "`profLink` VARCHAR(50) NULL DEFAULT NULL," +
            "`debug` TINYINT NULL DEFAULT '0'," +
            "UNIQUE INDEX `uuid` (`uuid`)" +
            ") COLLATE='utf8mb4_unicode_ci';"
    val guildSettings = "CREATE TABLE IF NOT EXISTS `jadb_guild_set` (" +
            "`discordId` BIGINT NULL DEFAULT NULL," +
            "`helloChannel` BIGINT NULL DEFAULT '0'," +
            "`helloMessage` TEXT(200) NULL DEFAULT NULL," +
            "`byeChannel` BIGINT NULL DEFAULT '0'," +
            "`byeMessage` TEXT(200) NULL DEFAULT NULL," +
            "`suggestChannel` BIGINT NULL DEFAULT '0'," +
            "`maxWarns` INT NULL DEFAULT '10'," +
            "`warnsPunish` TEXT(200) NULL DEFAULT '0:no'," +
            "`logChannel` BIGINT NULL DEFAULT '0'," +
            "`cmdPrefix` VARCHAR(50) NULL DEFAULT '!'," +
            "`muteRoleId` BIGINT NULL DEFAULT '0'" +
            ") COLLATE='utf8mb4_unicode_ci';"
    val quotes = "CREATE TABLE IF NOT EXISTS `jadb_quotes` (" +
            "`uuid` VARCHAR(50) NULL DEFAULT UUID()," +
            "`authorId` BIGINT NULL DEFAULT NULL," +
            "`name` VARCHAR(50) NULL DEFAULT NULL," +
            "`guildId` BIGINT NULL DEFAULT NULL," +
            "`quote` TEXT(200) NULL DEFAULT NULL" +
            ") COLLATE='utf8mb4_unicode_ci';"


    debug("MySQL - start creating `jadb_users`")
    sendExecute(users)
    debug("MySQL - start creating `jadb_guilds`")
    sendExecute(guilds)
    debug("MySQL - start creating `jadb_timers`")
    sendExecute(timers)
    debug("MySQL - start creating `jadb_us_set`")
    sendExecute(userSettings)
    debug("MySQL - start creating `jadb_guild_set`")
    sendExecute(guildSettings)
    debug("MySQL - start creating `jadb_quotes`")
    sendExecute(quotes)
}

@Synchronized
fun usersCount(): Long {
    val count = "SELECT COUNT(`uuid`) FROM `jadb_users`;"
    val rs = sendQuery(count)
    try {
        if (rs!!.next()) {
            val `in` = rs.getInt("COUNT(`uuid`)")
            rs.close()
            return `in`.toLong()
        }
    } catch (e: SQLException) {
        error(e)
    }

    return -1
}

@Synchronized
fun removeOneTime() {
    val timer = "UPDATE `jadb_timers` SET `timeLeft`=`timeLeft`-1 WHERE `timeLeft`>0;"
    val del = "DELETE FROM `jadb_timers` WHERE `timeLeft`=0;"
    sendExecute(del)
    sendExecute(timer)
}

@Synchronized
fun addUser(discordId: Long) {
    val add = "INSERT INTO `jadb_users` (`discordId`) VALUES ('$discordId');"
    sendExecute(add)
    debug("User $discordId was added to MySQL!")
}

@Synchronized
fun getUser(discordId: Long): JAKDBUser? {
    //val getUser = "SELECT COUNT(`discordId`) WHERE `discordId`=$discordId;"
    val getUser = "SELECT * FROM `jadb_users` WHERE `discordId`=$discordId;"
    val res: ResultSet? = sendQuery(getUser)
    try {
        if (res != null) {
            if (res.next()) {
                val user = JAKDBUser(res)
                res.close()
                return user
            }
        }
    } catch (e: SQLException) {
        error(e)
    }
    return null
}

@Synchronized
fun isUserExists(discordId: Long): Boolean {
    val ise = "SELECT COUNT(`discordId`) FROM `jadb_users` WHERE `discordId`=$discordId;"
    val res: ResultSet? = sendQuery(ise)
    if (res != null) {
        if(res.next()) {
            val count = res.getInt("COUNT(`discordId`)")
            if(count == 1) {
                res.close()
                return true
            }
        }
    }
    debug("User $discordId isn't exists!")
    return false
}

@Synchronized
fun addGuild(discordId: Long) {
    val add = "INSERT INTO `jadb_guilds` (`discordId`) VALUES ('$discordId');"
    sendExecute(add)
    debug("Guild $discordId was added to MySQL!")
}

@Synchronized
fun getGuild(discordId: Long): JAKDBGuild? {
    val getUser = "SELECT * FROM `jadb_guilds` WHERE `discordId`=$discordId;"
    val res: ResultSet? = sendQuery(getUser)
    try {
        if (res != null) {
            if (res.next()) {
                val guild = JAKDBGuild(res)
                res.close()
                return guild
            }
        }
    } catch (e: SQLException) {
        error(e)
    }
    return null
}

@Synchronized
fun getGuildLang(discordId: Long): Int {
    val getLang = "SELECT `lang_ID` FROM `jadb_guilds` WHERE `discordId`=$discordId;"
    val res: ResultSet? = sendQuery(getLang)
    try {
        if(res != null) {
            if(res.next()) {
                val lang = res.getInt("lang_ID")
                res.close()
                return lang
            }
        }
    } catch (e: SQLException) {
        error(e)
    }
    return 0
}

@Synchronized
fun isGuildExists(discordId: Long): Boolean {
    val ise = "SELECT * FROM `jadb_guilds` WHERE `discordId`=$discordId;"
    val res: ResultSet? = sendQuery(ise)
    if(res != null) {
        if(res.next()) {
            res.close()
            return true
        }
    }
    debug("Guild $discordId isn't exists!")
    return false
}

@Synchronized
fun addTimer(discordId: Long, type: Int, time: Long) {
    val add = "INSERT INTO `jadb_timers` (`discordId`,`type`,`timeLeft`) VALUES ('$discordId','$type','$time');"
    sendExecute(add)
    debug("TimedUser $discordId - $type - $time was added to MySQL!")
}

/**
 * Timer types:
 * 0 - XP
 * 1 - COMMANDS
 * 2 - DAILY REWARDS
 * ...soon
 */
@Synchronized
fun isTimerExists(discordId: Long, type: Int): Boolean {
    val ise = "SELECT * FROM `jadb_timers` WHERE `discordId`=$discordId AND `type`=$type;"
    val res: ResultSet? = sendQuery(ise)
    if(res != null) {
        if(res.next()) {
            res.close()
            return true
        }
    }
    return false
}

@Synchronized
fun levelUp(discordId: Long, reward: Long) {
    val lvl = "UPDATE `jadb_users` SET `global_LVL`=`global_LVL`+1, `global_XP`=0, `global_COINS`=`global_COINS`+$reward WHERE `discordId`=$discordId;"
    sendExecute(lvl)
}

@Synchronized
fun addXP(discordId: Long, xp: Long) {
    val add = "UPDATE `jadb_users` SET `global_XP`=`global_XP`+$xp WHERE `discordId`=$discordId;"
    sendExecute(add)
}

@Synchronized
fun removeCoins(discordId: Long, coins: Long) {
    val add = "UPDATE `jadb_users` SET `global_COINS`=`global_COINS`-$coins WHERE `discordId`=$discordId;"
    sendExecute(add)
}

@Synchronized
fun addCoins(discordId: Long, coins: Long) {
    val add = "UPDATE `jadb_users` SET `global_COINS`=`global_COINS`+$coins WHERE `discordId`=$discordId;"
    sendExecute(add)
}

@Synchronized
fun getCoins(discordId: Long): Long {
    val coins = "SELECT `global_COINS` FROM `jadb_users` WHERE `discordId`=$discordId;"
    val res: ResultSet? = sendQuery(coins)
    if(res != null) {
        if(res.next()) {
            val amount = res.getLong("global_COINS")
            res.close()
            return amount
        }
    }
    return 0
}

@Synchronized
fun getTimeCd(discordId: Long, type: Int): Long {
    val time = "SELECT `timeLeft` FROM `jadb_timers` WHERE `discordId`=$discordId AND `type`=$type;"
    val res: ResultSet? = sendQuery(time)
    if(res != null) {
        if(res.next()) {
            val time = res.getLong("timeLeft")
            res.close()
            return time
        }
    }
    return 0
}

@Synchronized
fun isUsSetExists(id: Long): Boolean {
    val ise = "SELECT COUNT(`discordId`) FROM `jadb_us_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(ise)
    if (res != null) {
        if(res.next()) {
            val count = res.getInt("COUNT(`discordId`)")
            res.close()
            if(count == 1) {
                return true
            }
        }
    }
    debug("UserSettings $id isn't exists!")
    return false
}

@Synchronized
fun addUsSet(id: Long) {
    val add = "INSERT INTO `jadb_us_set` (`discordId`) VALUES ('$id');"
    sendExecute(add)
    debug("UserSettings $id added to MySQL!")
}

@Synchronized
fun getProfMsg(id: Long): String {
    val get = "SELECT `profMsg` FROM `jadb_us_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    try {
        if (res != null) {
            if (res.next()) {
                val msg = res.getString("profMsg")
                res.close()
                return msg
            }
        }
    } catch (e: NullPointerException) { }
    return ""
}

@Synchronized
fun getProfLink(id: Long): String {
    val get = "SELECT `profLink` FROM `jadb_us_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    try {
        if (res != null) {
            if (res.next()) {
                val link = res.getString("profLink")
                res.close()
                return link
            }
        }
    } catch (e: NullPointerException) { }
    return ""
}

@Synchronized
fun getUsDebug(id: Long): Boolean {
    val get = "SELECT `debug` FROM `jadb_us_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val debug = res.getInt("debug")
            res.close()
            return debug == 1
        }
    }
    return false
}

@Synchronized
fun setUsDebug(id: Long, debug: Boolean) {
    var deb = 0
    if(debug) deb = 1
    val set = "UPDATE `jadb_us_set` SET `debug`=$deb WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun setUsMsg(id: Long, msg: String) {
    val set = "UPDATE `jadb_us_set` SET `profMsg`='$msg' WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun setUsLink(id: Long, link: String) {
    val set = "UPDATE `jadb_us_set` SET `profLink`='$link' WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun isGuildSetExists(id: Long): Boolean {
    val ise = "SELECT COUNT(`discordId`) FROM `jadb_guild_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(ise)
    if (res != null) {
        if(res.next()) {
            val count = res.getInt("COUNT(`discordId`)")
            res.close()
            if(count == 1) {
                return true
            }
        }
    }
    debug("GuildSettings $id isn't exists!")
    return false
}

@Synchronized
fun addGuildSettings(id: Long) {
    val add = "INSERT INTO `jadb_guild_set` (`discordId`) VALUES ('$id');"
    sendExecute(add)
    debug("GuildSettings $id added to MySQL!")
}

@Synchronized
fun getHelloChannel(id: Long): Long {
    val get = "SELECT `helloChannel` FROM `jadb_guild_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val debug = res.getLong("helloChannel")
            res.close()
            return debug
        }
    }
    return 0
}

@Synchronized
fun getHelloMessage(id: Long): String {
    val get = "SELECT `helloMessage` FROM `jadb_guild_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val debug = res.getString("helloMessage")
            res.close()
            return debug
        }
    }
    return "."
}

@Synchronized
fun getByeChannel(id: Long): Long {
    val get = "SELECT `byeChannel` FROM `jadb_guild_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val debug = res.getLong("byeChannel")
            res.close()
            return debug
        }
    }
    return 0
}

@Synchronized
fun getByeMessage(id: Long): String {
    val get = "SELECT `byeMessage` FROM `jadb_guild_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val debug = res.getString("byeMessage")
            res.close()
            return debug
        }
    }
    return "."
}

@Synchronized
fun getSuggestChannel(id: Long): Long {
    val get = "SELECT `suggestChannel` FROM `jadb_guild_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val debug = res.getLong("suggestChannel")
            res.close()
            return debug
        }
    }
    return 0
}

@Synchronized
fun getMaxWarns(id: Long): Int {
    val get = "SELECT `maxWarns` FROM `jadb_guild_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val debug = res.getInt("maxWarns")
            res.close()
            return debug
        }
    }
    return 0
}

@Synchronized
fun getWarnsPunish(id: Long): String {
    val get = "SELECT `warnsPunish` FROM `jadb_guild_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val debug = res.getString("warnsPunish")
            res.close()
            return debug
        }
    }
    return "0:no"
}

@Synchronized
fun getLogChannel(id: Long): Long {
    val get = "SELECT `logChannel` FROM `jadb_guild_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val debug = res.getLong("logChannel")
            res.close()
            return debug
        }
    }
    return 0
}

@Synchronized
fun getCmdPrefix(id: Long): String {
    val get = "SELECT `cmdPrefix` FROM `jadb_guild_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val prefix = res.getString("cmdPrefix")
            res.close()
            return prefix
        }
    }
    return "!"
}

@Synchronized
fun getMuteRoleId(id: Long): Long {
    val get = "SELECT `muteRoleId` FROM `jadb_guild_set` WHERE `discordId`=$id;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val role = res.getLong("muteRoleId")
            res.close()
            return role
        }
    }
    return 0
}

@Synchronized
fun setCmdPrefix(id: Long, prefix: String) {
    val set = "UPDATE `jadb_guild_set` SET `cmdPrefix`='$prefix' WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun setMuteRoleId(id: Long, role: Long) {
    val set = "UPDATE `jadb_guild_set` SET `muteRoleId`=$role WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun setHelloChannel(id: Long, channel: Long) {
    val set = "UPDATE `jadb_guild_set` SET `helloChannel`=$channel WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun setHelloMessage(id: Long, msg: String) {
    val set = "UPDATE `jadb_guild_set` SET `helloMessage`='$msg' WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun setByeChannel(id: Long, channel: Long) {
    val set = "UPDATE `jadb_guild_set` SET `byeChannel`=$channel WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun setByeMessage(id: Long, msg: String) {
    val set = "UPDATE `jadb_guild_set` SET `byeMessage`='$msg' WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun setSuggestChannel(id: Long, channel: Long) {
    val set = "UPDATE `jadb_guild_set` SET `suggestChannel`=$channel WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun setMaxWarns(id: Long, max: Int) {
    val set = "UPDATE `jadb_guild_set` SET `maxWarns`=$max WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun setLogChannel(id: Long, channel: Long) {
    val set = "UPDATE `jadb_guild_set` SET `logChannel`=$channel WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun setWarnsPunish(id: Long, str: String) {
    val set = "UPDATE `jadb_guild_set` SET `warnsPunish`='$str' WHERE `discordId`=$id;"
    sendExecute(set)
}

@Synchronized
fun addNewQuote(author: Long, guild: Long, name: String, quote: String) {
    val add = "INSERT INTO `jadb_quotes` (`authorId`,`name`,`guildId`,`quote`) VALUES ('$author','$name','$guild','$quote');"
    sendExecute(add)
}

@Synchronized
fun removeQuoteByName(guild: Long, name: String) {
    val remove = "DELETE FROM `jadb_quotes` WHERE `guildId`='$guild' AND `name`='$name';"
    sendExecute(remove)
}

@Synchronized
fun editQuoteByName(guild: Long, name: String, quote: String) {
    val update = "UPDATE `jadb_quotes` SET `quote`='$quote' WHERE `guildId`='$guild' AND `name`='$name';"
    sendExecute(update)
}

@Synchronized
fun getAllQuotesInGuild(guild: Long): ArrayList<String> {
    val get = "SELECT * FROM `jadb_quotes` WHERE `guildId`='$guild';"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val name = res.getString("name")
            val uuid = res.getString("uuid")
            val author = res.getLong("authorId")

            val quotes = ArrayList<String>()
            quotes.add("$uuid:$name:$author")

            res.close()
            return quotes
        }
    }
    return arrayListOf()
}

@Synchronized
fun getQuoteByName(guild: Long, name: String): String {
    val get = "SELECT `quote` FROM `jadb_quotes` WHERE `guildId`='$guild' AND `name`='$name';"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val quote = res.getString("quote")
            res.close()
            return quote
        }
    }
    return "Quote not found"
}

@Synchronized
fun isQuoteExistsByName(guild: Long, name: String): Boolean {
    val ise = "SELECT COUNT(`quote`) FROM `jadb_quotes` WHERE `guildId`='$guild' AND `name`='$name';"
    val res: ResultSet? = sendQuery(ise)
    if (res != null) {
        if(res.next()) {
            val count = res.getInt("COUNT(`quote`)")
            if(count == 1) {
                res.close()
                return true
            }
        }
    }
    return false
}

@Synchronized
fun getWarnsForUser(guild: Long, user: Long): Int {
    val get = "SELECT `warns` FROM `jadb_us_g$guild` WHERE `discordId`=$user;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val warns = res.getInt("warns")
            res.close()
            return warns
        }
    }
    return 0
}

@Synchronized
fun addWarnForUser(guild: Long, user: Long) {
    val add = "UPDATE `jadb_us_g$guild` SET `warns`=`warns`+1 WHERE `discordId`=$user;"
    sendExecute(add)
}

@Synchronized
fun removeWarnForUser(guild: Long, user: Long) {
    val remove = "UPDATE `jadb_us_g$guild` SET `warns`=`warns`-1 WHERE `discordId`=$user;"
    sendExecute(remove)
}

@Synchronized
fun setWarnReasonsForUser(guild: Long, user: Long, reasons: String) {
    val set = "UPDATE `jadb_us_g$guild` SET `warnsReasons`='$reasons' WHERE `discordId`=$user;"
    sendExecute(set)
}

@Synchronized
fun getWarnReasonsForUser(guild: Long, user: Long): String {
    val get = "SELECT `warnsReasons` FROM `jadb_us_g$guild` WHERE `discordId`=$user;"
    val res: ResultSet? = sendQuery(get)
    if(res != null) {
        if(res.next()) {
            val reasons = res.getString("warnsReasons")
            res.close()
            return reasons
        }
    }
    return "{\"no reasons\"}"
}
