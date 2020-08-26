package jakdb.main.commands.modules.help

import jakdb.*
import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.usersCount
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class About(command: String, rank: Int, test: Boolean,
            usage: String, argsNeed: Int, desc: String,
            perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }

        val replace = HashMap<String, String>()
        replace["<stats.count.users>"] = "${usersCount()}"
        replace["<stats.count.guilds>"] = "${jda!!.guilds.size}"
        replace["<stats.count.cmdUsed>"] = "$commandsUsed"
        replace["<about.authors>"] = "[${java.lang.String.join(", ", *authors)}]"
        replace["<about.version>"] = version
        replace["<stats.count.commands>"] = "${commands.size}"
        replace["<stats.count.xpgain>"] = "$totalXPgain"

        channel.sendMessage(getMessage(lang!!, "Help", "about", replace).build()).queue()
    }
}