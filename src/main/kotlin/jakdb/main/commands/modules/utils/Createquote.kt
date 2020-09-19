package jakdb.main.commands.modules.utils

import jakdb.data.mysql.addNewQuote
import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.getQuoteByName
import jakdb.data.mysql.isQuoteExistsByName
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.fromJSONToEmbeddedMessage
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import java.util.concurrent.TimeUnit

class Createquote(command: String, rank: Int, test: Boolean,
               usage: String, argsNeed: Int, desc: String,
               perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val guild = jda?.getGuildChannelById(channel.id)?.guild?.idLong

        val name = args.split(" ")[0]
        replace["<quote.name>"] = name

        if(isQuoteExistsByName(guild!!, name)) {
            channel.sendMessage(getMessage(lang!!, "Utils","quoteexists", replace).build()).queue()
            return
        }

        addNewQuote(user.idLong, guild, name, args.substring(name.length+1))
        channel.sendMessage(getMessage(lang!!, "Utils", "addnewquote", replace).build()).queue()
        channel.sendMessage(fromJSONToEmbeddedMessage(getQuoteByName(guild, name)).build()).queue {
            it.delete().queueAfter(60, TimeUnit.SECONDS)
        }
    }
}