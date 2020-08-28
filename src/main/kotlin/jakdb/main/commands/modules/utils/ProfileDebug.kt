package jakdb.main.commands.modules.utils

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.getUsDebug
import jakdb.data.mysql.setUsDebug
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class ProfileDebug(command: String, rank: Int, test: Boolean,
                   usage: String, argsNeed: Int, desc: String,
                   perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()

        if(getUsDebug(user.idLong)) {
            replace["<profile.debug>"] = "false"
            setUsDebug(user.idLong, false)
        } else {
            replace["<profile.debug>"] = "true"
            setUsDebug(user.idLong, true)
        }

        channel.sendMessage(getMessage(lang!!, "Utils", "setdebug", replace).build()).queue()
    }
}