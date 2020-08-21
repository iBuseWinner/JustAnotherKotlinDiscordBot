package jakdb.main.events

import jakdb.data.mysql.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import jakdb.utils.debug
import jakdb.utils.getMessage
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

        //ToDo: command & level systems
    }

    fun onMsgForLevel(e: MessageReceivedEvent) {
        if(!e.isFromType(ChannelType.TEXT)) {
            return
        }

        val id = e.author.idLong
        if(isTimerExists(id, 0)) {
            return
        }

        val user = getUser(id)
        val rank = user?.rank?.id
        val currLevel = user?.globalLVL
        val currXP = user?.globalXP

        var mult = 1
        var time = 60L
        if(rank!! == 2 && rank == 3) {
            mult = 3
            time = 55L
        } else if(rank!! <= 8 && rank >= 4) {
            mult = 4
            time = 40L
        }

        val xpADD = 2*mult

        val totalXPToLevelUp = currLevel?.plus(1)?.times(33)?.plus(9)

        if (currXP != null) {
            if(currXP >= totalXPToLevelUp!!) {

            } else {

            }
        }

        addTimer(id, 0, time)

    }
}