package jakdb.main.commands.modules.games

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.getUsDebug
import jakdb.data.mysql.setUsLink
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getDebugMessage
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class ProfileLink(command: String, rank: Int, test: Boolean,
                  usage: String, argsNeed: Int, desc: String,
                  perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()

        if(!isLink(args)) {
            if(getUsDebug(user.idLong)) {
                channel.sendMessage(getDebugMessage("nolink", HashMap()).build()).queue()
            }
        } else {
            replace["<profile.link>"] = args
            setUsLink(user.idLong, args)

            channel.sendMessage(getMessage(lang!!, "Games", "setprofilelink", replace).build()).queue()
        }
    }

    private fun isLink(str: String): Boolean {
        return str.startsWith("http")
    }
}