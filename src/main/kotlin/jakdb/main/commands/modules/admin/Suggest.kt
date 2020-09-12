package jakdb.main.commands.modules.admin

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.setSuggestChannel
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Suggest(command: String, rank: Int, test: Boolean,
              usage: String, argsNeed: Int, desc: String,
              perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        if(args.isNotEmpty()) {
            val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
            val replace = HashMap<String, String>()

            if (args.startsWith("set")) {
                val chan = jda?.getTextChannelById(channel.idLong)
                chan?.guild?.idLong?.let { setSuggestChannel(it, chan.idLong) }

                replace["<suggest.channel>"] = "${chan?.id}"

                channel.sendMessage(getMessage(lang!!, "Admin", "suggestset", replace).build()).queue()
            } else {
                jda?.getTextChannelById(channel.id)?.guild?.idLong?.let { setSuggestChannel(it, 0) }
                channel.sendMessage(getMessage(lang!!, "Admin", "suggestoff", replace).build()).queue()
            }
        }
    }
}