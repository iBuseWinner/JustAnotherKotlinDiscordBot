package jakdb.data.mysql

import jakdb.utils.debug
import java.sql.SQLException
import jakdb.utils.*

@Synchronized
fun createTables() {
    val users = "CREATE TABLE IF NOT EXISTS `jadb_users` (" +
            "`uuid` VARCHAR(50) NULL DEFAULT UUID()," +
            "`discordId` BIGINT NULL DEFAULT NULL," +
            "`global_COINS` BIGINT NULL DEFAULT NULL," +
            "`gold` BIGINT NULL DEFAULT NULL," +
            "`global_XP` BIGINT NULL DEFAULT NULL," +
            "`global_LVL` BIGINT NULL DEFAULT NULL," +
            "`rank` INT NULL," +
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
            "UNIQUE INDEX `uuid` (`uuid`)" +
            ") COLLATE='utf8mb4_unicode_ci';"
    val timers = "CREATE TABLE IF NOT EXISTS `jadb_timers` (" +
            "`uuid` VARCHAR(50) NULL DEFAULT UUID()," +
            "`discordId` BIGINT NULL DEFAULT NULL," +
            "`timeLeft` BIGINT NULL DEFAULT NULL" +
            ") COLLATE='utf8mb4_unicode_ci';"
    debug("MySQL - start creating `jadb_users`")
    exec(users)
    debug("MySQL - start creating `jadb_guilds`")
    exec(guilds)
    debug("MySQL - start creating `jadb_timers`")
    exec(timers)
}

@Synchronized
fun usersCount(): Long {
    val count = "SELECT COUNT(`uuid`) FROM `jadb_users`;"
    val rs = get(count)
    try {
        if (rs!!.next()) {
            debug("ResultSet has next!")
            val `in` = rs.getInt("COUNT(`uuid`)")
            debug("Users count: $`in`")
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
    debug("Updating timers...")
    exec(del)
    exec(timer)
}