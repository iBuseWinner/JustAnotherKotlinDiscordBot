package jakdb.main.events

import jakdb.data.mysql.getSuggestChannel
import jakdb.jda
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class SuggestionsOMG : ListenerAdapter() {

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        if(event.user?.isBot!!) return

        event.retrieveMessage().queue {
            val channel = event.channel.idLong
            val suggestChannel = getSuggestChannel(event.guild.idLong)
            if(channel == suggestChannel) {
                //ToDo
            }
        }
    }
    //TODO
//    fun calcChance(approve: Long, minus: Long, disapprove: Long) {
//
//    }

}