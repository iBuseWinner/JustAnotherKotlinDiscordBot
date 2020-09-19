package jakdb.main.commands.modules.admin

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.getUsDebug
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.*
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Setpunish(command: String, rank: Int, test: Boolean,
                 usage: String, argsNeed: Int, desc: String,
                 perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val guild = jda?.getGuildChannelById(channel.id)?.guild?.idLong

        var amount = 0
        val argss = args.split(" ")

        try {
            amount = argss[0].toInt()
        } catch (e: NumberFormatException) {
            channel.sendMessage(getGlobalMessage("number", replace).build()).queue()
            return
        }

        if(argss[1] == "no" || argss[1] == "off" || argss[1] == "disable") {
            getAllWarnsAndRemoveOld(guild!!, amount)
            replace["<punish.amount>"] = "$amount"
            channel.sendMessage(getMessage(lang!!, "Admin", "removepunish", replace).build()).queue()
            return
        } else {
            var mult: Long = 1

            if (argss[3] == "second" || argss[3] == "sec" || argss[3] == "s" || argss[3] == "seconds") {
                mult = 1
            } else if (argss[3] == "minute" || argss[3] == "minutes" || argss[3] == "min" || argss[3] == "m") {
                mult = 60
            } else if (argss[3] == "hour" || argss[3] == "hours" || argss[3] == "h") {
                mult = 60 * 60
            } else if (argss[3] == "day" || argss[3] == "days" || argss[3] == "d") {
                mult = 60 * 60 * 24
            } else if (argss[3] == "week" || argss[3] == "weeks" || argss[3] == "w") {
                mult = 60 * 60 * 24 * 7
            } else if (argss[3] == "month" || argss[3] == "months" || argss[3] == "mo") {
                mult = 60 * 60 * 24 * 7 * 4
            } else if (argss[3] == "year" || argss[3] == "years" || argss[3] == "y") {
                mult = 60 * 60 * 24 * 7 * 4 * 12
            }

            var value = 1L

            try {
                value = argss[2].toLong()
            } catch (e: NumberFormatException) {
                channel.sendMessage(getGlobalMessage("number", replace).build()).queue()
                return
            }

            val calc = value * mult

            if (calc > 29030400) {
                replace["<number>"] = "$calc"
                channel.sendMessage(getGlobalMessage("tooBig", replace).build()).queue()
                return
            } else {
                if (amount <= 0) {
                    replace["<number>"] = "$amount"
                    channel.sendMessage(getGlobalMessage("tooBig", replace).build()).queue()
                    return
                } else {
                    var type = "no"
                    if (argss[1] == "ban") {
                        type = "ban>$calc"
                    } else if (argss[1] == "mute") {
                        type = "mute>$calc"
                    } else if (argss[1] == "kick") {
                        type = "kick>$calc"
                    } else {
                        if (getUsDebug(user.idLong)) {
                            channel.sendMessage(getDebugMessage("wrongargs", replace).build()).queue()
                            return
                        }
                    }

                    getAllWarnsAndAddNew(guild!!, amount, type)

                    replace["<punish.amount>"] = "$amount"
                    replace["<punish.type>"] = type
                    channel.sendMessage(getMessage(lang!!, "Admin", "setpunish", replace).build()).queue()
                }
            }
        }
    }
}