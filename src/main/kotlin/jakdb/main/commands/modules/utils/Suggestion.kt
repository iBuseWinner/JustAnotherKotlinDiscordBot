package jakdb.main.commands.modules.utils

import jakdb.data.mysql.getGuildLang
import jakdb.data.mysql.getSuggestChannel
import jakdb.data.mysql.getUsDebug
import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.symbolApproved
import jakdb.symbolDisapproved
import jakdb.symbolMinus
import jakdb.utils.debug
import jakdb.utils.getDebugMessage
import jakdb.utils.getMessage
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import java.util.concurrent.TimeUnit

class Suggestion(command: String, rank: Int, test: Boolean,
                 usage: String, argsNeed: Int, desc: String,
                 perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val lang = jda?.getGuildChannelById(channel.idLong)?.guild?.idLong?.let { getGuildLang(it) }
        val replace = HashMap<String, String>()
        val suggestChannel = jda?.getGuildChannelById(channel.id)?.guild?.idLong?.let { getSuggestChannel(it) }

        if(suggestChannel != 0L) {
            if(suggestChannel == channel.idLong) {
                replace["<suggestion.msg>"] = args
                replace["<suggestion.author>"] = user.asMention

                channel.sendMessage(getMessage(lang!!, "Utils", "suggestion", replace).build()).queue {
                    jda?.getGuildById(678517523913768982)?.retrieveEmotes()?.queue() { it2 ->
                        for(em in it2) {
                            if(em.idLong == symbolApproved) {
                                em?.let { it1 -> it.addReaction(it1) }
                                        ?.queueAfter(20, TimeUnit.MILLISECONDS)
                            } else if(em.idLong == symbolMinus) {
                                em?.let { it1 -> it.addReaction(it1) }
                                        ?.queueAfter(20, TimeUnit.MILLISECONDS)
                            } else if(em.idLong == symbolDisapproved) {
                                em?.let { it1 -> it.addReaction(it1) }
                                        ?.queueAfter(20, TimeUnit.MILLISECONDS)
                            }
                        }
                    }
                }
            } else {
                if(getUsDebug(user.idLong)) {
                    replace["<suggest.channel>"] = "<#$suggestChannel>"
                    channel.sendMessage(getDebugMessage("notsuggestchannel", replace).build()).queue()
                }
            }
        } else {
            if(getUsDebug(user.idLong)) {
                channel.sendMessage(getDebugMessage("nosuggestchannel", replace).build()).queue()
            }
        }
    }
}