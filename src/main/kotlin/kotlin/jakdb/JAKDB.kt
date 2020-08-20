package kotlin.jakdb

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader
import kotlin.jakdb.data.mysql.*
import kotlin.jakdb.main.events.JAKDBEventer
import kotlin.jakdb.utils.*

var debug = true
var version = "0.0.1 ALPHA"
var authors = arrayOf("BuseSo#6824")
var jda: JDA? = null
var settings: JSONObject? = null
var usersTime = 600L
var defPrefix = "!"

class JAKDB {
    //public static List<JADBCommand> commands = new ArrayList<>();
    fun main(args: Array<String>) {
        val start = System.currentTimeMillis()
        info("JADB starting...")
        debug("Oh, you use debug! Awesome!")
        info("Trying to create files...")
        createAllFiles()
        debug("Start putting default settings.json")
        if (putDefaultSettings()) {
            info("Please, configure settings.json!")
            info("JADB version " + version + " by [" + java.lang.String.join(", ", *authors) + "] " +
                    "disabled in " + (System.currentTimeMillis() - start) + " ms!")
            System.exit(1)
        } else {
            debug("settings.json isn't default so starting the bot...")
            val parser = JSONParser()
            try {
                FileReader("settings.json").use { reader ->
                    debug("Getting token...")
                    val obj = parser.parse(reader)
                    settings = obj as JSONObject
                    val token = settings!!["token"] as String
                    debug("Building JDA...")
                    jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                            .addEventListeners(JAKDBEventer())
                            .setActivity(Activity.listening("users"))
                            .build()
                }
            } catch (e: Exception) {
                error(e)
            }
            info("Trying to connect to MySQL...")
            setup()
            debug("Creating tables if not exists...")
            createTables()
            info("Starting timers...")
            debug("Updater info")
            updateStats()
            debug("Updater timers")
            removingCds()
            info("Registering commands...")
            registerCommands()
        }
    }

    @Synchronized
    private fun registerCommands() {
        //ToDo
    }
}