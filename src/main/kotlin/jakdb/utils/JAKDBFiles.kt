package jakdb.utils

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

@Synchronized
fun createAllFiles() {
    debug("Files creation - [start creating settings.json]")
    val settings = File("settings.json")
    if (!settings.exists()) {
        debug("Files creation - [settings.json not exists, creating it...]")
        try {
            settings.createNewFile()
            debug("Files creation - [settings.json created]")
        } catch (e: IOException) {
            error(e)
        }
    }

    debug("Files creation - [start creating /messages/]")
    val messages = File("messages")
    if (!messages.exists()) {
        debug("Files creation - [/messages/ not exists, creating it...]")
        messages.mkdirs()
        debug("Files creation - [/messages/ created]")
    }
    debug("Files creation - [start creating /modules/]")
    val modules = File("messages", "modules")
    if (!modules.exists()) {
        debug("Files creation - [/modules/ not exists, creating it...]")
        modules.mkdirs()
        debug("Files creation - [/modules/ created]")
    }
}

/**
 * @return true if no settings; false if settings != default
 */
@Synchronized
fun putDefaultSettings(): Boolean {
    val def = true
    val parser = JSONParser()
    try {
        FileReader("settings.json").use { reader ->
            debug("Files creation - [settings.json default or not]")
            val obj = parser.parse(reader)
            val json = obj as JSONObject
            debug(json.toString())
            val token = json["token"] as String
            return if (token == "putTokenHere") {
                debug("Files creation - [settings.json token is default]")
                true
            } else {
                debug("Files creation - [settings.json token isn't default]")
                false
            }
        }
    } catch (e: Exception) {
        error(e)

        val settings = JSONObject()
        settings["token"] = "putTokenHere"
        settings["botId"] = "putBotIdHere"

        val mysql = JSONObject()
        mysql["host"] = "localhost"
        mysql["port"] = 3301
        mysql["database"] = "jadb"
        mysql["user"] = "root"
        mysql["password"] = ""
        mysql["args"] = "useSSL=false&useTimezone=UTC"
        settings["mysql"] = mysql

        val pinger = JSONObject()
        pinger["enabled"] = true
        pinger["users"] = 678526806604906497L
        pinger["guilds"] = 678526767731965952L
        settings["pinger"] = pinger
        try {
            FileWriter("settings.json").use { file ->
                file.write(settings.toJSONString())
                file.flush()
            }
        } catch (ex: IOException) {
            error(ex)
        }
    }
    return def
}