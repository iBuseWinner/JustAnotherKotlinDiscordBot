package jakdb.main.events

import jakdb.data.mysql.getQuoteByName
import jakdb.data.mysql.getUser
import jakdb.data.mysql.isQuoteExistsByName
import jakdb.utils.fromJSONToEmbeddedMessage
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class QuoteEventer : ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if(getUser(event.author.idLong)?.isBanned == 1 || event.author.isBot) {
            return
        }

        val name = event.message.contentRaw.split(" ")[0]
        val guild = event.guild.idLong

        if(isQuoteExistsByName(guild, name)) {
            val quote = getQuoteByName(guild, name)
            event.channel.sendMessage(fromJSONToEmbeddedMessage(quote).build()).queue()
        }
    }
}