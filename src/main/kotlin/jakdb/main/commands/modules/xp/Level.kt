package jakdb.main.commands.modules.xp

import jakdb.main.commands.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Level(command: String, rank: Int, test: Boolean,
            usage: String, argsNeed: Int, desc: String,
            perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, user: User) {
        TODO("Not yet implemented")
    }

}