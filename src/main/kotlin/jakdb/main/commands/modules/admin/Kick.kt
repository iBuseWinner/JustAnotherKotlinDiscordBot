package jakdb.main.commands.modules.admin

import jakdb.data.mysql.getGuildLang
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import java.util.concurrent.TimeUnit

class Kick(command: String, rank: Int, test: Boolean,
           usage: String, argsNeed: Int, desc: String,
           perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val guild = jda?.getGuildChannelById(channel.id)?.guild?.idLong

        val targetText = args.split(" ")[0]
        val reason = args.substring(targetText.length+1)

        val target = msg.mentionedMembers[0]
        val targetId = target.idLong

        if(target.hasPermission(Permission.ADMINISTRATOR) || target.hasPermission(Permission.MANAGE_SERVER) || target.hasPermission(Permission.KICK_MEMBERS)) {
            replace["<target.mention>"] = "<@$targetId>"
            channel.sendMessage(getMessage(lang!!, "Admin", "cantpunish", replace).build()).queue()
        } else {
            replace["<kick.reason>"] = reason
            replace["<kick.moder>"] = user.asMention
            replace["<kick.guild>"] = "${jda?.getGuildById(guild!!)?.name}"

            target.user.openPrivateChannel().queue {
                it.sendMessage(getMessage(lang!!, "Admin", "youwaskicked", replace).build()).queue() {
                    target.kick(reason).queueAfter(1, TimeUnit.SECONDS)
                }
            }

            channel.sendMessage(getMessage(lang!!, "Admin", "userwaskicked", replace).build()).queue()
        }
    }
}