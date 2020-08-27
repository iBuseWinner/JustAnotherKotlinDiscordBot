package jakdb.main.commands.modules.admin

import jakdb.main.commands.ICommand
import jakdb.utils.fromJSONToEmbeddedMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Say(command: String, rank: Int, test: Boolean,
          usage: String, argsNeed: Int, desc: String,
          perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        channel.sendMessage(fromJSONToEmbeddedMessage(args).build()).queue()
    }
}