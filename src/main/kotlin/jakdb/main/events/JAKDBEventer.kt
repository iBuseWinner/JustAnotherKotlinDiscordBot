package jakdb.main.events

import jakdb.data.mysql.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import jakdb.utils.debug
import net.dv8tion.jda.api.entities.ChannelType

class JAKDBEventer : ListenerAdapter() {
    override fun onMessageReceived(e: MessageReceivedEvent) {
        if(!isUserExists(e.author.idLong)) {
            debug("User ${e.author.idLong} isn't exists so add him to MySQL")
            addUser(e.author.idLong)
        }

        /*if(getUser(e.author.idLong) == null) {
            debug("User ${e.author.idLong} isn't exists so add him to MySQL")
            addUser(e.author.idLong)
        }*/

        if(e.isFromType(ChannelType.TEXT)) {
            if(!isGuildExists(e.guild.idLong)) {
                debug("Guild ${e.guild.idLong} isn't exists so add it to MySQL")
                addGuild(e.guild.idLong)
            }
        }

        /*if (e.isFromType(ChannelType.TEXT)) {
            if (e.message.contentRaw.startsWith(defPrefix)) {
                val embed = EmbedBuilder()
                embed.setDescription("Бубубу")
                embed.setColor(Color(0x3AE377))
                embed.setTitle("А что не ожидали да??")
                e.channel.sendMessage(embed.build()).queue()
            }
        }*/
    }
}