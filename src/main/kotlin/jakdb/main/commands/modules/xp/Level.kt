package jakdb.main.commands.modules.xp

import jakdb.data.mysql.getUser
import jakdb.main.commands.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Level(command: String, rank: Int, test: Boolean,
            usage: String, argsNeed: Int, desc: String,
            perm: Permission, module: String, aliases: Array<String>,
            guildOnly: Boolean)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases, guildOnly) {

    override fun execute(channel: MessageChannel, user: User, args: String) {
        //TODO("Not yet implemented")
        if(args.isEmpty()) {
            val us = getUser(user.idLong)

            val xp = us?.globalXP
            val level = us?.globalLVL
            var mult: Long = 1
            var time = 60L
            if(rank in 2..3) {
                mult = 2
                time = 55L
            } else if(rank in 4..8) {
                mult = 3
                time = 40L
            }

            val xpADD = 2*mult
        } else {
            //ToDo: get other-user's level info
        }
    }

}