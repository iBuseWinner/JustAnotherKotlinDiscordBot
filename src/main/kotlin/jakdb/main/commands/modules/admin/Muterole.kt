package jakdb.main.commands.modules.admin

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.getMuteRoleId
import jakdb.data.mysql.setMuteRoleId
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Muterole(command: String, rank: Int, test: Boolean,
               usage: String, argsNeed: Int, desc: String,
               perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val guild = jda?.getGuildChannelById(channel.id)?.guild?.idLong

        if(args.isNotEmpty()) {
            val role = msg.mentionedRoles[0].idLong
            replace["<guild.mute.role>"] = "<@&$role>"
            setMuteRoleId(guild!!, role)
            channel.sendMessage(getMessage(lang!!, "Admin", "setmuterole", replace).build()).queue()
        } else {
            val role = getMuteRoleId(guild!!)
            replace["<guild.mute.role>"] = "<@&$role>"
            channel.sendMessage(getMessage(lang!!, "Admin", "getmuterole", replace).build()).queue()
        }
    }
}