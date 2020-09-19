package jakdb.main.commands.modules.admin

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.setLogChannel
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Logchannel(command: String, rank: Int, test: Boolean,
                 usage: String, argsNeed: Int, desc: String,
                 perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val guild = jda?.getGuildChannelById(channel.id)?.guild?.idLong

        if(args == "disable" || args == "off" || args == "no") {
            setLogChannel(guild!!, 0)
            channel.sendMessage(getMessage(lang!!, "Admin", "disablelog",replace).build()).queue()
        } else {
            setLogChannel(guild!!, channel.idLong)

            replace["<log.channel>"] = "<#${channel.idLong}>"
            channel.sendMessage(getMessage(lang!!, "Admin", "setlogchannel", replace).build()).queue()
        }
    }
}