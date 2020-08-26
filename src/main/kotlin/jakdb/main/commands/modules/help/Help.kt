package jakdb.main.commands.modules.help

import jakdb.commands
import jakdb.data.mysql.getGuildLang
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.debug
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Help(command: String, rank: Int, test: Boolean,
           usage: String, argsNeed: Int, desc: String,
           perm: Permission, module: String, aliases: Array<String>,
           guildOnly: Boolean)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases, guildOnly) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {

        val lang: Int? = try {
            jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        } catch (e: NullPointerException) {
            0
        }

        val replace = HashMap<String, String>()
        var cmd = false

        for(hcmd in commands) {
            if(hcmd.command == args.toLowerCase() || hcmd.aliases.contains(args.toLowerCase())) {
                replace["<command.name>"] = hcmd.command
                replace["<command.aliases>"] = formatAliases(hcmd.aliases)
                replace["<command.rank>"] = "${hcmd.rank}"
                replace["<command.test>"] = "${hcmd.test}"
                replace["<command.usage>"] = hcmd.usage
                replace["<command.argsNeed>"] = "${hcmd.argsNeed}"
                replace["<command.desc>"] = hcmd.desc
                replace["<command.perm>"] = hcmd.perm.toString()
                replace["<command.module>"] = hcmd.module
                replace["<command.guildOnly>"] = "${hcmd.guildOnly}"
                lang?.let { getMessage(it, "Help", "aboutcommand", replace) }?.build()?.let { channel.sendMessage(it).queue() }

                cmd = true
            }
        }

        if(!cmd) {
            lang?.let { getMessage(it, "Help", "help", replace) }?.build()?.let { channel.sendMessage(it).queue() }
        }

    }

    private fun formatAliases(aliases: Array<String>): String {
        var ret = "["
        for(str in aliases) {
            ret += "$str, "
        }
        return ret.substring(0, ret.length-2) + "]"
    }

}