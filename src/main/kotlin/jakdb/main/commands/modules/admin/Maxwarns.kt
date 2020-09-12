package jakdb.main.commands.modules.admin

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.getMaxWarns
import jakdb.data.mysql.getUsDebug
import jakdb.data.mysql.setMaxWarns
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.fromJSONToEmbeddedMessage
import jakdb.utils.getDebugMessage
import jakdb.utils.getGlobalMessage
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Maxwarns(command: String, rank: Int, test: Boolean,
               usage: String, argsNeed: Int, desc: String,
               perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val guild = jda?.getGuildChannelById(channel.id)?.guild?.idLong

        if(args.isNotEmpty()) {
            try {
                val amount = args.toInt()
                replace["<warns.max>"] = "$amount"

                setMaxWarns(guild!!, amount)
                channel.sendMessage(getMessage(lang!!, "Admin", "setmaxwarns", replace).build()).queue()
            } catch (e: NumberFormatException) {
                if (getUsDebug(user.idLong)) {
                    channel.sendMessage(getGlobalMessage("number", replace).build()).queue()
                }
            }
        } else {
            val amount = getMaxWarns(guild!!)
            replace["<warns.max>"] = "$amount"
            channel.sendMessage(getMessage(lang!!, "Admin", "getmaxwarns", replace).build()).queue()
        }
    }
}