package jakdb.data.items

import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*

class JAKDBUser (rs: ResultSet) {

    lateinit var uuid: UUID
    var discordId: Long = 0
    var globalCoins: Long = 0
    var gold: Long = 0
    var globalXP: Long = 0
    var globalLVL: Long = 0
    var rank: Int = 0
    var isBanned: Int = 0
    var banReason: String = "Not banned"
    var banTime: Timestamp = Timestamp(System.currentTimeMillis())
    var regTime: Timestamp = Timestamp(System.currentTimeMillis())

    fun JAKDBUser(rs: ResultSet) {
        try {
            uuid = UUID.fromString(rs.getString("uuid"))
            discordId = rs.getLong("discordId")
            globalCoins = rs.getLong("global_COINS")
            gold = rs.getLong("gold")
            globalXP = rs.getLong("global_XP")
            globalLVL = rs.getLong("global_LVL")
            rank = rs.getInt("rank")
            isBanned = rs.getInt("isBanned")
            banReason = rs.getString("banReason")
            banTime = rs.getTimestamp("banTime")
            regTime = rs.getTimestamp("regTime")
            rs.close()
        } catch (e: Exception) {
            error(e)
        }
    }

}