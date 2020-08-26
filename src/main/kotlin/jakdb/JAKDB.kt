package jakdb

import jakdb.data.mysql.createTables
import jakdb.data.mysql.setup
import jakdb.main.commands.ICommand
import jakdb.main.commands.modules.help.Help
import jakdb.main.commands.modules.xp.Level
import jakdb.main.events.*
import jakdb.utils.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader

const val debug = true
const val version = "0.1.47 ALPHA"
val authors = arrayOf("BuseSo#6824")
var jda: JDA? = null
var settings: JSONObject? = null
var usersTime = 600L
const val defPrefix = "!"

var commands: ArrayList<ICommand> = ArrayList()

var symbolMoney = "<:wheat:747106658471116910>"

fun main(args: Array<String>) {
    val start = System.currentTimeMillis()
    info("JAKDB starting...")
    debug("Oh, you use debug! Awesome!")
    info("Trying to create files...")
    createAllFiles()
    debug("Start putting default settings.json")
    if (putDefaultSettings()) {
        info("Please, configure settings.json!")
        info("JAKDB version $version by [${java.lang.String.join(", ", *authors)}] " +
                "disabled in ${System.currentTimeMillis() - start} ms!")
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
                jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                        .addEventListeners(JAKDBEventer())
                        .disableCache(CacheFlag.ACTIVITY)
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

        info("JAKDB version $version by [${java.lang.String.join(", ", *authors)}] " +
                "started in ${System.currentTimeMillis() - start} ms!")
    }
}

@Synchronized
fun registerCommands() {
    val lvl = Level("level", 0, true, "${defPrefix}level [@user]",
            0, "Check level", Permission.UNKNOWN, "XP", arrayOf("lvl","xp"), true)
    val help = Help("help", 0, true, "${defPrefix}help [command]",
            0, "View help", Permission.UNKNOWN, "Help", arrayOf("h"), false)

    commands.add(lvl)
    commands.add(help)


}