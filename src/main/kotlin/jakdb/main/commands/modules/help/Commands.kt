package jakdb.main.commands.modules.help

import jakdb.commands
import jakdb.data.mysql.getGuildLang
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import java.util.ArrayList

class Commands(command: String, rank: Int, test: Boolean,
               usage: String, argsNeed: Int, desc: String,
               perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        if(args.isEmpty()) {
            channel.sendMessage(getMessage(lang!!, "Help", "commandsEmpty", HashMap()).build()).queue()
        } else {
            val comds: ArrayList<String> = ArrayList()
            for(cmd in commands) {
                if(cmd.module.toLowerCase() == args.toLowerCase()) {
                    comds.add(cmd.command)
                }
            }

            val replace = HashMap<String, String>()
            val format = format(comds)
            if(format == "exc") {
                channel.sendMessage(getMessage(lang!!, "Help", "commandsEmpty", HashMap()).build()).queue()
            } else {
                replace["<commands.list>"] = format
                channel.sendMessage(getMessage(lang!!, "Help", "commandsList", replace).build()).queue()
            }
        }
    }

    private fun format(arr: ArrayList<String>): String {
        var str = ""
        for(ass in arr) {
            str += "$ass, "
        }
        try {
            str = str.substring(0, str.length - 2)
        } catch (e: StringIndexOutOfBoundsException) {
            str = "exc"
        }

        return str
    }
}