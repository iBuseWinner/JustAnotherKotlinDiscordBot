package jakdb.main.commands.modules.admin

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.getWarnsPunish
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Getpunishs(command: String, rank: Int, test: Boolean,
                 usage: String, argsNeed: Int, desc: String,
                 perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val guild = jda?.getGuildChannelById(channel.id)?.guild?.idLong

        val warns = getWarnsPunish(guild!!).split(",")

        var answer = ""
        for(w in warns) {
            val split = w.split(":")
            answer = "**${split[0]}**: _${split[1]}_\n"
        }

        replace["<punish.list>"] = answer
        channel.sendMessage(getMessage(lang!!, "Admin", "getpunishes", replace).build()).queue()
    }
}