package jakdb.main.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

abstract class ICommand (command: String, rank: Int, test: Boolean, usage: String, argsNeed: Int,
                         desc: String, perm: Permission, module: String, aliases: Array<String>, guildOnly: Boolean) {

    val command: String = command
    var rank: Int = rank
    val test: Boolean = test
    val usage: String = usage
    val argsNeed: Int = argsNeed
    val desc: String = desc
    val perm: Permission = perm
    val module: String = module
    val aliases: Array<String> = aliases
    val guildOnly: Boolean = guildOnly

    abstract fun execute(channel: MessageChannel, user: User, args: String)

}