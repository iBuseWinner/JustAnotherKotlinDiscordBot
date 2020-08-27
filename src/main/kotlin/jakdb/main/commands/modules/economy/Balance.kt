package jakdb.main.commands.modules.economy

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.getUser
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import kotlin.NullPointerException

class Balance(command: String, rank: Int, test: Boolean,
              usage: String, argsNeed: Int, desc: String,
              perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        var self = false

        if(args.isNotEmpty()) {
            try {
                val user = getUser(msg.mentionedUsers[0].idLong)
                val bal = user?.globalCoins
                val gold = user?.gold

                replace["<eco.bal.other.mention>"] = msg.mentionedUsers[0].asMention
                replace["<eco.bal.coins>"] = "$bal"
                replace["<eco.bal.gold>"] = "$gold"

                channel.sendMessage(getMessage(lang!!, "Economy", "balanceother", replace).build()).queue()
            } catch (e: NullPointerException) {
                try {
                    val user = getUser(args.toLong())
                    val bal = user?.globalCoins
                    val gold = user?.gold

                    replace["<eco.bal.other.mention>"] = "<@$args>"
                    replace["<eco.bal.coins>"] = "$bal"
                    replace["<eco.bal.gold>"] = "$gold"

                    channel.sendMessage(getMessage(lang!!, "Economy", "balanceother", replace).build()).queue()
                } catch (e: NullPointerException) {
                    self = true
                } catch (e: NumberFormatException) {
                    self = true
                }
            } catch (e: IndexOutOfBoundsException) {
                try {
                    val user = getUser(args.toLong())
                    val bal = user?.globalCoins
                    val gold = user?.gold

                    replace["<eco.bal.other.mention>"] = "<@$args>"
                    replace["<eco.bal.coins>"] = "$bal"
                    replace["<eco.bal.gold>"] = "$gold"

                    channel.sendMessage(getMessage(lang!!, "Economy", "balanceother", replace).build()).queue()
                } catch (e: NullPointerException) {
                    self = true
                } catch (e: NumberFormatException) {
                    self = true
                }
            }
        } else {
            self = true
        }

        if(self) {
            val user = getUser(user.idLong)
            val bal = user?.globalCoins
            val gold = user?.gold

            replace["<eco.bal.coins>"] = "$bal"
            replace["<eco.bal.gold>"] = "$gold"

            channel.sendMessage(getMessage(lang!!, "Economy", "balanceself", replace).build()).queue()
        }
    }
}