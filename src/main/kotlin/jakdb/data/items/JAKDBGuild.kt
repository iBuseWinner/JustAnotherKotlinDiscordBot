package jakdb.data.items

import jakdb.utils.enums.Language
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import java.util.*

class JAKDBGuild (rs: ResultSet) {

    lateinit var uuid: UUID
    var discordId: Long = 0
    var isBanned: Int = 0
    var banReason: String = "Not banned"
    var banTime: Timestamp = Timestamp(System.currentTimeMillis())
    var regTime: Timestamp = Timestamp(System.currentTimeMillis())
    var langId: Language = Language.en_US

    fun JAKDBGuild(rs: ResultSet) {
        try {
            uuid = UUID.fromString(rs.getString("uuid"))
            discordId = rs.getLong("discordId")
            isBanned = rs.getInt("isBanned")
            banReason = rs.getString("banReason")
            banTime = rs.getTimestamp("banTime")
            regTime = rs.getTimestamp("regTime")
            langId = Language.valueOf(""+rs.getInt("lang_ID"))
        } catch (e: SQLException) {
            error(e)
        }
    }

}