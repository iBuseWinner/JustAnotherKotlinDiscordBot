package jakdb.main.events

import jakdb.data.mysql.getSuggestChannel
import jakdb.jda
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class SuggestionsOMG : ListenerAdapter() {

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        if(event.user?.isBot!!) return

        event.retrieveMessage().queue {
            if(it.author.isBot) {
                if(it.author.id == jda?.selfUser?.id) {
                    
                }
            }
        }

        val channel = event.channel
        val suggestChannel = getSuggestChannel(event.guild.idLong)
    }

}