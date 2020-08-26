package jakdb.main.commands.modules.xp

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.getUser
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.debug
import jakdb.utils.getGlobalMessage
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Level(command: String, rank: Int, test: Boolean,
            usage: String, argsNeed: Int, desc: String,
            perm: Permission, module: String, aliases: Array<String>,
            guildOnly: Boolean)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases, guildOnly) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        var own: Boolean = false

        try {
            val tar = getUser(msg.mentionedUsers[0].idLong)

            val xp = tar?.globalXP
            val level = tar?.globalLVL
            var mult: Long = 1
            var time = 60L
            if (tar?.rank in 2..3) {
                mult += 1
                time -= 5L
            } else if (tar?.rank in 4..8) {
                mult += 2
                time -= 10L
            }

            val xpADD = 2 * mult
            val totalXPToLevelUp = level?.plus(1)?.times(33)?.plus(9)

            replace["<level.target>"] = msg.mentionedUsers[0].asTag
            replace["<level.last>"] = "${level?.minus(1)}"
            replace["<level.current>"] = "$level"
            replace["<level.next>"] = "${level?.plus(1)}"
            replace["<level.xp>"] = "$xp"
            replace["<level.xpadd>"] = "$xpADD"
            replace["<level.mult>"] = "$mult"
            replace["<level.cooldown>"] = "$time"
            replace["<level.totalnext>"] = "$totalXPToLevelUp"
            replace["<level.nextxp>"] = "${totalXPToLevelUp!! - xp!!}"

            lang?.let { getMessage(it, "XP", "levelother", replace) }?.build()?.let { channel.sendMessage(it).queue() }
        } catch (e: IllegalArgumentException) {
            try {
                val tar = getUser(msg.mentionedUsers[0].idLong)

                val xp = tar?.globalXP
                val level = tar?.globalLVL
                var mult: Long = 1
                var time = 60L
                if (tar?.rank in 2..3) {
                    mult += 1
                    time -= 5L
                } else if (tar?.rank in 4..8) {
                    mult += 2
                    time -= 10L
                }

                val xpADD = 2 * mult
                val totalXPToLevelUp = level?.plus(1)?.times(33)?.plus(9)

                replace["<level.target>"] = "${tar?.discordId?.let { jda?.getUserById(it)?.asMention }}"
                replace["<level.last>"] = "${level?.minus(1)}"
                replace["<level.current>"] = "$level"
                replace["<level.next>"] = "${level?.plus(1)}"
                replace["<level.xp>"] = "$xp"
                replace["<level.xpadd>"] = "$xpADD"
                replace["<level.mult>"] = "$mult"
                replace["<level.cooldown>"] = "$time"
                replace["<level.totalnext>"] = "$totalXPToLevelUp"
                replace["<level.nextxp>"] = "${totalXPToLevelUp!! - xp!!}"

                lang?.let { getMessage(it, "XP", "levelother", replace) }?.build()?.let { channel.sendMessage(it).queue() }
            } catch (e: IllegalArgumentException) {
                own = true
            }
        } catch (e: IndexOutOfBoundsException) {
            own = true
        } catch (e: NullPointerException) {
            replace["<user.target>"] = args
            channel.sendMessage(getGlobalMessage("targetusernotfound", replace).build()).queue()
        }

        if(own) {
            val us = getUser(user.idLong)

            val xp = us?.globalXP
            val level = us?.globalLVL
            var mult: Long = 1
            var time = 60L
            if(us?.rank in 2..3) {
                mult += 1
                time -= 5L
            } else if(us?.rank in 4..8) {
                mult += 2
                time -= 10L
            }

            val xpADD = 2*mult
            val totalXPToLevelUp = level?.plus(1)?.times(33)?.plus(9)

            replace["<level.last>"] = "${level?.minus(1)}"
            replace["<level.current>"] = "$level"
            replace["<level.next>"] = "${level?.plus(1)}"
            replace["<level.xp>"] = "$xp"
            replace["<level.xpadd>"] = "$xpADD"
            replace["<level.mult>"] = "$mult"
            replace["<level.cooldown>"] = "$time"
            replace["<level.totalnext>"] = "$totalXPToLevelUp"
            replace["<level.nextxp>"] = "${totalXPToLevelUp!! - xp!!}"

            lang?.let { getMessage(it, "XP", "level", replace) }?.build()?.let { channel.sendMessage(it).queue() }
        }
    }

}