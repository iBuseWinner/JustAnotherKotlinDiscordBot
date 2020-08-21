package jakdb.main.events

import jakdb.data.mysql.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import jakdb.utils.debug
import jakdb.utils.fromJSONToEmbeddedMessage
import net.dv8tion.jda.api.entities.ChannelType

class JAKDBEventer : ListenerAdapter() {
    override fun onMessageReceived(e: MessageReceivedEvent) {
        if(e.author.isBot) return

        if(!isUserExists(e.author.idLong)) {
            debug("User ${e.author.idLong} isn't exists so add him to MySQL")
            addUser(e.author.idLong)
        }

        if(e.isFromType(ChannelType.TEXT)) {
            if(!isGuildExists(e.guild.idLong)) {
                debug("Guild ${e.guild.idLong} isn't exists so add it to MySQL")
                addGuild(e.guild.idLong)
            }
        }
    }
}