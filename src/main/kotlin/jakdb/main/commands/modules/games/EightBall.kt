package jakdb.main.commands.modules.games

import jakdb.data.mysql.getGuildLang
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.eightBall
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

class EightBall(command: String, rank: Int, test: Boolean,
                usage: String, argsNeed: Int, desc: String,
                perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        replace["<8ball.asnwer>"] = eightBall(7)
        replace["<8ball.question>"] = args

        channel.sendMessage(getMessage(lang!!, "Games", "8ball", replace).build()).queue()
    }
}