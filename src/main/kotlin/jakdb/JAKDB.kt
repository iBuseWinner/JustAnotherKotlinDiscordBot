package jakdb

import jakdb.data.mysql.createTables
import jakdb.data.mysql.setup
import jakdb.main.commands.ICommand
import jakdb.main.commands.modules.admin.Maxwarns
import jakdb.main.commands.modules.admin.Say
import jakdb.main.commands.modules.admin.Suggest
import jakdb.main.commands.modules.economy.Balance
import jakdb.main.commands.modules.economy.Casino
import jakdb.main.commands.modules.economy.DailyReward
import jakdb.main.commands.modules.games.EightBall
import jakdb.main.commands.modules.games.Profile
import jakdb.main.commands.modules.games.ProfileLink
import jakdb.main.commands.modules.games.ProfileMessage
import jakdb.main.commands.modules.help.About
import jakdb.main.commands.modules.help.Commands
import jakdb.main.commands.modules.help.Help
import jakdb.main.commands.modules.help.Modules
import jakdb.main.commands.modules.secret.AboutBot
import jakdb.main.commands.modules.secret.End
import jakdb.main.commands.modules.secret.Send
import jakdb.main.commands.modules.utils.ProfileDebug
import jakdb.main.commands.modules.utils.Suggestion
import jakdb.main.commands.modules.xp.Level
import jakdb.main.events.GuildsGreeter
import jakdb.main.events.JAKDBEventer
import jakdb.main.events.SuggestionsOMG
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
const val version = "0.3.1 ALPHA"
val authors = arrayOf("BuseSo#6824")
var jda: JDA? = null
var settings: JSONObject? = null
var usersTime = 600L
const val defPrefix = "!"

var commands: ArrayList<ICommand> = ArrayList()
const val symbolMoney = "<:wheat:747106658471116910>"
const val symbolGold = "<:gold:747104154043809922>"
var commandsUsed = 0L
var totalXPgain = 0L

//const val symbolApproved = "<a:approved:748855884096208936>"
//const val symbolMinus = "<a:minus:748856252435791872>"
//const val symbolDisapproved = "<a:disapproved:748855883009884292>"
const val symbolApproved = 748855884096208936
const val symbolMinus = 748856252435791872
const val symbolDisapproved = 748855883009884292

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
        kotlin.system.exitProcess(1)
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
                jda = JDABuilder.createLight(token,
                        GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_EMOJIS)
                        .addEventListeners(JAKDBEventer())
                        .addEventListeners(SuggestionsOMG())
                        .addEventListeners(GuildsGreeter())
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
            0, "Check level", Permission.UNKNOWN, "XP", arrayOf("lvl","xp"))
    val help = Help("help", 0, true, "${defPrefix}help [command]",
            0, "View help", Permission.UNKNOWN, "Help", arrayOf("h"))

    val aboutbot = AboutBot("aboutbot", 7, false, "${defPrefix}aboutbot",
            0, "Send information", Permission.ADMINISTRATOR, "Secret", arrayOf("aboutme"))
    
    val modules = Modules("modules", 0, true, "${defPrefix}modules",
            0, "Show available modules", Permission.UNKNOWN, "Help", arrayOf("mdls"))

    val cmds = Commands("commands", 0, true, "${defPrefix}commands <module>",
            1, "Show commands of module", Permission.UNKNOWN, "Help", arrayOf("cmds"))

    val about = About("about", 0, true, "${defPrefix}about",
            0, "Show info and stats", Permission.UNKNOWN, "Help", arrayOf("statistics", "stats"))

    val end = End("end", 7, true, "${defPrefix}end",
            0, "Stop the bot", Permission.ADMINISTRATOR, "Secret", arrayOf("stop","disablebot","endbot","stopbot"))

    val send =  Send("send", 7, true, "${defPrefix}send <channel> <message>",
            2, "Sends message to the channel", Permission.ADMINISTRATOR, "Secret", arrayOf("sendmessage","sendmsg"))

    val eightBall = EightBall("8ball", 0, true, "${defPrefix}8ball <question>",
            1, "Ask 8ball a question", Permission.UNKNOWN, "Games", arrayOf("eightball"))

    val balance = Balance("balance", 0, true, "${defPrefix}balance [@user]",
            0, "Check your/other user balance", Permission.UNKNOWN, "Economy", arrayOf("bal","money","gold","coins"))

    val casino = Casino("casino", 0, true, "${defPrefix}casino <bet>",
            1, "Play casino to get more money", Permission.UNKNOWN, "Economy", arrayOf("bet", "gamble"))

    val dailyreward = DailyReward("dailyreward", 0, true, "${defPrefix}dailyreward",
            0, "Get daily rewards", Permission.UNKNOWN, "Economy", arrayOf("dr", "daily", "dailyrewards"))

    val say = Say("say", 0, true, "${defPrefix}say <text/json>",
            1, "Write what you wrote in args", Permission.MESSAGE_MANAGE, "Administration", arrayOf("repeat", "write"))

    val profile = Profile("profile", 0, true, "${defPrefix}profile [@user]",
            0, "Check profile", Permission.UNKNOWN, "Games", arrayOf("checkprofile","getprofile"))

    val setmessage = ProfileMessage("setmessage", 0, true, "${defPrefix}setmessage <message>",
            1, "Set message in your profile", Permission.UNKNOWN, "Games", arrayOf("setmsg","profilemessage","profmsg"))

    val setlink = ProfileLink("setlink", 0, true, "${defPrefix}setlink <link>",
            1, "Set link in your profile", Permission.UNKNOWN, "Games", arrayOf("profilelink","proflnk"))

    val setdebug = ProfileDebug("setdebug", 0, true, "${defPrefix}setdebug",
            0, "Set debug to use bot", Permission.UNKNOWN, "Games", arrayOf("profiledebug","debug"))

    val suggest = Suggest("suggest", 1, true, "${defPrefix}suggest <set/off>",
            2, "Set channel for suggestions", Permission.MESSAGE_MANAGE, "Admin", arrayOf("suggestadmin","admsuggest"))

    val suggestion = Suggestion("suggestion", 0, true, "${defPrefix}suggestion <text>",
            1, "Send suggestion in this guild", Permission.UNKNOWN, "Utils", arrayOf("sendsuggest", "wantsuggest"))

    val maxwarns = Maxwarns("maxwarns", 0, true, "${defPrefix}maxwarns [amount]",
            0, "Get or set max warns for users in this guild", Permission.KICK_MEMBERS, "Admin", arrayOf("maxwarnings","maximumwarns"))

    commands.add(lvl)
    commands.add(help)
    commands.add(aboutbot)
    commands.add(modules)
    commands.add(cmds)
    commands.add(about)
    commands.add(end)
    commands.add(send)
    commands.add(eightBall)
    commands.add(balance)
    commands.add(casino)
    commands.add(dailyreward)
    commands.add(say)
    commands.add(profile)
    commands.add(setmessage)
    commands.add(setlink)
    commands.add(setdebug)
    commands.add(suggest)
    commands.add(suggestion)
    commands.add(maxwarns)

}