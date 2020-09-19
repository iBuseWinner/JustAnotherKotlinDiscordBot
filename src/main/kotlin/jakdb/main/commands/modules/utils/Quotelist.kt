package jakdb.main.commands.modules.utils

import jakdb.data.mysql.getAllQuotesInGuild
import jakdb.data.mysql.getGuildLang
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Quotelist(command: String, rank: Int, test: Boolean,
                usage: String, argsNeed: Int, desc: String,
                perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val guild = jda?.getGuildChannelById(channel.id)?.guild?.idLong

        val quotes = getAllQuotesInGuild(guild!!)
        val message = getMessage(lang!!, "Utils", "quotesList", replace)

        for(q in quotes) {
            val sub = q.split(":")

            val uuid = sub[0]
            val name = sub[1]
            val author = sub[2]


        }
    }
}