package jakdb.main.commands.modules.economy

import jakdb.data.mysql.*
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class DailyReward(command: String, rank: Int, test: Boolean,
                  usage: String, argsNeed: Int, desc: String,
                  perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    //Timer type = 2
    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()

        if(isTimerExists(user.idLong, 2)) {
            val time = getTimeCd(user.idLong, 2)

            replace["<economy.daily.wait>"] = timeToString(time)
            channel.sendMessage(getMessage(lang!!, "Economy", "dailywait",replace).build()).queue()
        } else {
            val rank = getUser(user.idLong)?.rank
            var mult = 1
            if(rank in 2..3) {
                mult += 1
            } else if(rank in 4..8) {
                mult += 2
            }

            val reward = 100L*mult
            addCoins(user.idLong, reward)
            addTimer(user.idLong, 2, 86400)

            replace["<economy.daily.reward>"] = "$reward"
            channel.sendMessage(getMessage(lang!!, "Economy", "dailyreward",replace).build()).queue()
        }
    }

    private fun timeToString(time: Long): String {
        val hours = time / 3600
        val minutes = time / 60 % 60
        val sec = time / 1 % 60

        return "$hours:$minutes:$sec"
    }
}