package jakdb.data.mysql

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
    debug("MySQL - start creating `jadb_users`")
    sendExecute(users)
    debug("MySQL - start creating `jadb_guilds`")
    sendExecute(guilds)
    debug("MySQL - start creating `jadb_timers`")
    sendExecute(timers)
}

@Synchronized
fun usersCount(): Long {
    val count = "SELECT COUNT(`uuid`) FROM `jadb_users`;"
    val rs = sendQuery(count)
    try {
        if (rs!!.next()) {
            debug("ResultSet has next!")
            val `in` = rs.getInt("COUNT(`uuid`)")
            debug("Users count: $`in`")
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
    debug("TimedUser $discordId was added to MySQL!")
}

@Synchronized
fun isTimerExists(discordId: Long, type: Int): Boolean {
    val ise = "SELECT * FROM `jadb_guilds` WHERE `discordId`=$discordId AND `type`=$type;"
    val res: ResultSet? = sendQuery(ise)
    if(res != null) {
        if(res.next()) {
            res.close()
            return true
        }
    }
    debug("TimedUser $discordId isn't exists!")
    return false
}