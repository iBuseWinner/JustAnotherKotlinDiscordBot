package jakdb.main.commands.modules.admin

import jakdb.data.mysql.addWarnForUser
import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.getMaxWarns
import jakdb.data.mysql.getWarnsForUser
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getAllUserWarnsAndAddNew
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Warn(command: String, rank: Int, test: Boolean,
           usage: String, argsNeed: Int, desc: String,
           perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val guild = jda?.getGuildChannelById(channel.id)?.guild?.idLong
        val amount = getMaxWarns(guild!!)

        val targetText = args.split(" ")[0]
        val reason = args.substring(targetText.length+1)

        val mention = msg.mentionedMembers[0]
        val target = mention.idLong

        if(amount >= getWarnsForUser(guild, target)) {
            if(mention.hasPermission(Permission.ADMINISTRATOR) || mention.hasPermission(Permission.MANAGE_SERVER)
                    || mention.hasPermission(Permission.KICK_MEMBERS) || mention.hasPermission(Permission.BAN_MEMBERS)) {
                replace["<target.mention>"] = mention.asMention
                channel.sendMessage(getMessage(lang!!, "Admin", "cantpunish", replace).build()).queue()
            } else {
                replace["<warns.user.count>"] = "${getWarnsForUser(guild, target)}"
                replace["<warns.user.reason>"] = reason
                addWarnForUser(guild, target)
                getAllUserWarnsAndAddNew(guild, target, reason)

                channel.sendMessage(getMessage(lang!!, "Admin", "warnuser", replace).build()).queue()
            }
        } else {
            replace["<warns.max>"] = "$amount"
            channel.sendMessage(getMessage(lang!!, "Admin", "getmaxwarns", replace).build()).queue()
        }
    }
}