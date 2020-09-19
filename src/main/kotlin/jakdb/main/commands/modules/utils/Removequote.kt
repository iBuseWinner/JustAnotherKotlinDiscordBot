package jakdb.main.commands.modules.utils

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.isQuoteExistsByName
import jakdb.data.mysql.removeQuoteByName
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Removequote(command: String, rank: Int, test: Boolean,
                usage: String, argsNeed: Int, desc: String,
                perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val guild = jda?.getGuildChannelById(channel.id)?.guild?.idLong

        val name = args.split(" ")[0]
        replace["<quote.name>"] = name

        if(!isQuoteExistsByName(guild!!, name)) {
            channel.sendMessage(getMessage(lang!!, "Utils", "quotenotexists", replace).build()).queue()
            return
        }

        removeQuoteByName(guild, name)
        channel.sendMessage(getMessage(lang!!, "Utils", "quotedeleted", replace).build()).queue()
    }
}