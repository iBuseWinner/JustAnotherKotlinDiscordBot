package kotlin.jakdb.main.events

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import kotlin.jakdb.JAKDB
class JAKDBEventer : ListenerAdapter() {
    override fun onMessageReceived(e: MessageReceivedEvent) {
        if (e.isFromType(ChannelType.TEXT)) {
            if (e.message.contentRaw.startsWith(JAKDB().defPrefix)) {
                val embed = EmbedBuilder()
                embed.setDescription("Бубубу")
                embed.setColor(Color(0x3AE377))
                embed.setTitle("А что не ожидали да??")
                e.channel.sendMessage(embed.build()).queue()
            }
        }
    }
}