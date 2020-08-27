package jakdb.main.commands.modules.secret

import jakdb.jda
import jakdb.main.commands.ICommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import java.util.concurrent.TimeUnit

class End(command: String, rank: Int, test: Boolean,
          usage: String, argsNeed: Int, desc: String,
          perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        channel.sendMessage("Bot will shutdown in 1 minute!").queue { it ->
            it.editMessage("Bot will shutdown in 30 seconds!").queueAfter(30, TimeUnit.SECONDS) { it ->
                it.editMessage("Bot will shutdown in 15 seconds!").queueAfter(15, TimeUnit.SECONDS) { it ->
                    it.editMessage("Bot will shutdown in 10 seconds!").queueAfter(5, TimeUnit.SECONDS) { it ->
                        it.editMessage("Bot will shutdown in 5 seconds").queueAfter(5, TimeUnit.SECONDS) {
                            it.delete().queueAfter(5, TimeUnit.SECONDS) {
                                jda?.shutdown()
                            }
                        }
                    }
                }
            }
        }
    }
}