package jakdb.main.commands.modules.admin

import jakdb.data.mysql.*
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.fromJSONToEmbeddedMessage
import jakdb.utils.getDebugMessage
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import java.util.concurrent.TimeUnit

class Greeting(command: String, rank: Int, test: Boolean,
               usage: String, argsNeed: Int, desc: String,
               perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val guild = jda?.getGuildChannelById(channel.id)?.guild?.idLong

        var arg = args

        if(args.startsWith("join")) {
            arg = arg.substring(5)
            setHelloMessage(guild!!, arg)

            channel.sendMessage(getMessage(lang!!, "Admin", "sethellomessage", replace).build()).queue()
            channel.sendMessage(fromJSONToEmbeddedMessage(arg).build()).queue {
                it.delete().queueAfter(20, TimeUnit.SECONDS)
            }
        } else if(args.startsWith("quit")) {
            arg = arg.substring(5)
            setByeMessage(guild!!, arg)

            channel.sendMessage(getMessage(lang!!, "Admin", "setbyemessage", replace).build()).queue()
            channel.sendMessage(fromJSONToEmbeddedMessage(arg).build()).queue {
                it.delete().queueAfter(20, TimeUnit.SECONDS)
            }
        } else if(args.startsWith("channel")) {
            if(args == "channel join") {
                setHelloChannel(guild!!, channel.idLong)
                replace["<greeting.join.channel>"] = "<#${channel.id}>"

                channel.sendMessage(getMessage(lang!!, "Admin", "sethellochannel", replace).build()).queue()
            } else if(args == "channel quit") {
                setByeChannel(guild!!, channel.idLong)
                replace["<greeting.quit.channel>"] = "<#${channel.id}>"

                channel.sendMessage(getMessage(lang!!, "Admin", "setquitchannel", replace).build()).queue()
            } else {
                if(getUsDebug(user.idLong)) {
                    channel.sendMessage(getDebugMessage("wrongargs", replace).build()).queue()
                }
            }
        } else if(args.startsWith("disable")){
            if(args == "disable join") {
                setHelloChannel(guild!!, 0)
            } else if(args == "disable quit") {
                setByeChannel(guild!!, 0)
            } else {
                if(getUsDebug(user.idLong)) {
                    channel.sendMessage(getDebugMessage("wrongargs", replace).build()).queue()
                }
            }
        } else {
            if(getUsDebug(user.idLong)) {
                channel.sendMessage(getDebugMessage("wrongargs", replace).build()).queue()
            }
        }
    }
}