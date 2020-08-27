package jakdb.main.commands.modules.economy

import jakdb.data.mysql.addCoins
import jakdb.data.mysql.getCoins
import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.removeCoins
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getGlobalMessage
import jakdb.utils.getMessage
import jakdb.utils.item
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class Casino(command: String, rank: Int, test: Boolean,
             usage: String, argsNeed: Int, desc: String,
             perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()

        try {
            var bet = args.toLong()

            if(bet > getCoins(user.idLong)) {
                replace["<economy.have>"] = "$bet"
                channel.sendMessage(getMessage(lang!!, "Economy", "donthavecoins", replace).build()).queue()
                return
            }

            removeCoins(user.idLong, bet)

            if(bet > 1000000000) {
                channel.sendMessage(getGlobalMessage("tooBig", HashMap()).build()).queue()
                return
            }

            val st = item()
            val nd = item()
            val rd = item()

            /*
            0 - lose bet
            1 - win x1 (when 2 items equals)
            2 - win x2 (when 3 items equals)
            3 - win x5 (when 3 wheat)
             */
            var state: Int

            if(st == nd && st == rd) {
                state = 2

                if(st == 1L) {
                    state = 3
                }
            } else if(st == nd || st == rd || nd == rd) {
                state = 1
            } else {
                state = 0
            }

            if(state == 1) {
                bet = bet
            } else if(state == 2) {
                bet *= 2
            } else if(state == 3) {
                bet *= 5
            } else {
                bet = 0
            }

            replace["<casino.item.1>"] = translate(st)
            replace["<casino.item.2>"] = translate(nd)
            replace["<casino.item.3>"] = translate(rd)
            replace["<casino.won>"] = "$bet"

            addCoins(user.idLong, bet)

            channel.sendMessage(getMessage(lang!!, "Economy", "casino", replace).build()).queue()
        } catch (e: NumberFormatException) {
            channel.sendMessage(getGlobalMessage("number", HashMap()).build()).queue()
        }

    }

    private fun translate(item: Long): String {
        when(item) {
            1L -> return "<:wheat:747106658471116910>"
            2L -> return "\uD83C\uDF88"
            3L -> return "\uD83E\uDDA0"
            4L -> return "\uD83C\uDF63"
        }

        return "\uD83C\uDF49"
    }
}