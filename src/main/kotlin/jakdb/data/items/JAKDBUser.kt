package jakdb.data.items

import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*

class JAKDBUser (rs: ResultSet) {
    var uuid: UUID = UUID.fromString(rs.getString("uuid"))
    var discordId: Long = rs.getLong("discordId")
    var globalCoins: Long = rs.getLong("global_COINS")
    var gold: Long = rs.getLong("gold")
    var globalXP: Long = rs.getLong("global_XP")
    var globalLVL: Long = rs.getLong("global_LVL")
    var rank: Int = rs.getInt("rank")
    var isBanned: Int = rs.getInt("isBanned")
    var banReason: String = rs.getString("banReason")
    var banTime: Timestamp = rs.getTimestamp("banTime")
    var regTime: Timestamp = rs.getTimestamp("regTime")
}