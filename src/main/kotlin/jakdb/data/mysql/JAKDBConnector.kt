package jakdb.data.mysql

import jakdb.settings
import jakdb.utils.debug
import jakdb.utils.info
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import jakdb.utils.*

var con: Connection? = null
    private set

fun setup() {
    val parser = JSONParser()
    try {
        val mysql = settings?.get("mysql") as JSONObject
        debug("MySQL: $mysql")
        val host = mysql["host"] as String
        val port = mysql["port"] as Int
        val database = mysql["database"] as String
        val user = mysql["user"] as String
        val password = mysql["user"] as String
        val args = mysql["args"] as String
        debug("Trying to create connection...")
        con = DriverManager.getConnection("jdbc:mysql://$host:$port/$database?autoReconnect=true&$args", user, password)
        info("Connection to MySQL created!")

        /*FileReader("settings.json").use { reader ->
            val `object` = parser.parse(reader)
            val json = `object` as JSONObject
            val mysql = json["mysql"] as JSONObject
            debug("MySQL: $mysql")
            val host = mysql["host"] as String
            val port = mysql["port"] as Int
            val database = mysql["database"] as String
            val user = mysql["user"] as String
            val password = mysql["password"] as String
            val args = mysql["args"] as String
            debug("Trying to create connection...")
            con = DriverManager.getConnection("jdbc:mysql://$host:$port/$database?autoReconnect=true&$args", user, password)
            info("Connection to MySQL created!")
        }*/
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

@Synchronized
fun exec(sql: String?, vararg args: String?) {
    setup()
    try {
        con!!.prepareStatement(sql).use { ps ->
            var i = 1
            for (arg in args) {
                ps.setString(i++, arg)
            }
            ps.executeUpdate()
        }
    } catch (e: SQLException) {
        error(e)
    }
}

@Synchronized
fun get(sql: String?, vararg args: String?): ResultSet? {
    setup()
    try {
        con!!.prepareStatement(sql).use { ps ->
            var i = 1
            for (arg in args) {
                ps.setString(i++, arg)
            }
            ps.closeOnCompletion()
            return ps.executeQuery()
        }
    } catch (e: SQLException) {
        error(e)
    }
    return null
}