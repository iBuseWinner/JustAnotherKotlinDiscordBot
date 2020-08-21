package jakdb.data.mysql

import jakdb.settings
import jakdb.utils.debug
import jakdb.utils.info
import org.json.simple.JSONObject
import jakdb.utils.*
import java.sql.*

var con: Connection? = null
    private set

fun setup() {
    try {
        if(con == null || con!!.isClosed) {
            val mysql = settings?.get("mysql") as JSONObject
            debug("MySQL: $mysql")
            val host = mysql["host"] as String
            val port = mysql["port"] as Long
            val database = mysql["database"] as String
            val user = mysql["user"] as String
            val password = mysql["user"] as String
            val args = mysql["args"] as String
            debug("Trying to create connection...")
            con = DriverManager.getConnection("jdbc:mysql://$host:$port/$database?autoReconnect=true&$args", user, password)
            info("Connection to MySQL created!")
        }
    } catch (e: Exception) {
        error(e)
    }
}

fun disconnect() {
    if (con != null) {
        try {
            con!!.close()
            info("Disconnected from MySQL!")
        } catch (e: Exception) {
            error(e)
        }
    }
}

/*@Synchronized
fun exec(sql: String?) {
    setup()
    try {
        getStat(sql).use { ps -> {
            ps?.executeUpdate()
            }
        }
    } catch (e: SQLException) {
        error(e)
    }
}

@Synchronized
fun getStat(sql: String?): PreparedStatement? {
    setup()
    try {
        return con!!.prepareStatement(sql)
    } catch (e: SQLException) {
        error(e)
    }
    return null
}

@Synchronized
fun get(sql: String?): ResultSet? {
    setup()
    try {
        getStat(sql).use { ps ->
            if (ps != null) {
                return ps.executeQuery()
            }
        }
    } catch (e: SQLException) {
        error(e)
    }
    return null
}*/

@Synchronized
fun getStat(sql: String): PreparedStatement? {
    setup()
    try {
        return con?.prepareStatement(sql)
    } catch (e: SQLException) {
        error(e)
    }
    return null
}

@Synchronized
fun sendExecute(sql: String) {
    setup()
    try {
        getStat(sql)?.executeUpdate()
    } catch (e: SQLException) {
        error(e)
    }
}

@Synchronized
fun sendQuery(sql: String): ResultSet? {
    setup()
    try {
        return getStat(sql)?.executeQuery()
    } catch (e: SQLException) {
        error(e)
    }
    return null
}