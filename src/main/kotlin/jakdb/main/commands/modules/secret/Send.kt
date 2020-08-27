package jakdb.main.commands.modules.secret

import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.debug
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import java.util.concurrent.TimeUnit

class Send(command: String, rank: Int, test: Boolean,
           usage: String, argsNeed: Int, desc: String,
           perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val a = args.split(" ")

        try {
            val channelId = a[0]
            try {
                val text = args.substring(channelId.length+1)
                jda?.getTextChannelById(channelId)?.sendMessage(text)?.queue()
                debug(text)
            } catch (e: NullPointerException) {
                channel.sendMessage("Channel not found!").queue {
                    it.delete().queueAfter(20, TimeUnit.SECONDS)
                }
            }
        } catch (e: NumberFormatException) {
            channel.sendMessage("Specify number in 1st arg!").queue {
                it.delete().queueAfter(20, TimeUnit.SECONDS)
            }
        }
    }
}