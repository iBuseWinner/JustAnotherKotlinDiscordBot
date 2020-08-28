package jakdb.main.commands.modules.games

import jakdb.data.mysql.*
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Profile(command: String, rank: Int, test: Boolean,
              usage: String, argsNeed: Int, desc: String,
              perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        var self = false

        if(args.isNotEmpty()) {
            try {
                val usr = getUser(msg.mentionedUsers[0].idLong)
                val mes = usr?.discordId?.let { getProfMsg(it) }
                val link = usr?.discordId?.let { getProfLink(it) }
                val debug = usr?.discordId?.let { getUsDebug(it) }

                replace["<profile.user>"] = msg.mentionedUsers[0].asMention
                replace["<profile.msg>"] = "$mes"
                replace["<profile.link>"] = "$link"
                replace["<profile.debug>"] = "$debug"

                channel.sendMessage(getMessage(lang!!, "Games", "profileother", replace).build()).queue()
            } catch (e: NullPointerException) {
                try {
                    val usr = getUser(args.toLong())
                    val mes = usr?.discordId?.let { getProfMsg(it) }
                    val link = usr?.discordId?.let { getProfLink(it) }
                    val debug = usr?.discordId?.let { getUsDebug(it) }

                    replace["<profile.user>"] = "<@$args>"
                    replace["<profile.msg>"] = "$mes"
                    replace["<profile.link>"] = "$link"
                    replace["<profile.debug>"] = "$debug"

                    channel.sendMessage(getMessage(lang!!, "Games", "profileother", replace).build()).queue()
                } catch (e: NullPointerException) {
                    self = true
                } catch (e: NumberFormatException) {
                    self = true
                }
            } catch (e: IndexOutOfBoundsException) {
                try {
                    val usr = getUser(args.toLong())
                    val mes = usr?.discordId?.let { getProfMsg(it) }
                    val link = usr?.discordId?.let { getProfLink(it) }
                    val debug = usr?.discordId?.let { getUsDebug(it) }

                    replace["<profile.user>"] = "<@$args>"
                    replace["<profile.msg>"] = "$mes"
                    replace["<profile.link>"] = "$link"
                    replace["<profile.debug>"] = "$debug"

                    channel.sendMessage(getMessage(lang!!, "Games", "profileother", replace).build()).queue()
                } catch (e: NullPointerException) {
                    self = true
                } catch (e: NumberFormatException) {
                    self = true
                }
            }
        } else {
            self = true
        }

        if(self) {
            val usr = getUser(user.idLong)
            val mes = usr?.discordId?.let { getProfMsg(it) }
            val link = usr?.discordId?.let { getProfLink(it) }
            val debug = usr?.discordId?.let { getUsDebug(it) }

            replace["<profile.msg>"] = "$mes"
            replace["<profile.link>"] = "$link"
            replace["<profile.debug>"] = "$debug"

            channel.sendMessage(getMessage(lang!!, "Games", "profileself", replace).build()).queue()
        }
    }
}