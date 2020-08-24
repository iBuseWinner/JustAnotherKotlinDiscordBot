package jakdb.main.events

import jakdb.data.mysql.*
import jakdb.symbolMoney
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
            onMsgForLevel(e)
        }

        //ToDo: command system
    }

    private fun onMsgForLevel(e: MessageReceivedEvent) {
        if(!e.isFromType(ChannelType.TEXT)) {
            return
        }

        val id = e.author.idLong
        if(isTimerExists(id, 0)) {
            return
        }

        val user = getUser(id)
        val rank = user?.rank
        val currLevel = user?.globalLVL
        var currXP = user?.globalXP

        var mult: Long = 1
        var time = 60L
        if(rank!! == 2 && rank == 3) {
            mult = 3
            time = 55L
        } else if(rank in 4..8) {
            mult = 4
            time = 40L
        }

        val xpADD = 2*mult
        addXP(id, xpADD)
        currXP = currXP?.plus(xpADD)

        val totalXPToLevelUp = currLevel?.plus(1)?.times(33)?.plus(9)

        if (currXP != null) {
            //debug("Users $id current XP: $currXP and total XP: $totalXPToLevelUp")
            if(currXP >= totalXPToLevelUp!!) {
                currLevel.plus(1)
                val map: HashMap<String, String> = HashMap()

                map["<level.last>"] = "${currLevel-1}"
                map["<level.current>"] = "$currLevel"
                map["<level.next>"] = "${currLevel+1}"

                val reward: Long = 1*(currLevel*mult)
                map["<level.reward>"] = "$reward"
                map["<symbol.money>"] = symbolMoney
                levelUp(id, reward)

                val lang = getGuildLang(e.guild.idLong)
                e.channel.sendMessage(getMessage(lang, "XP", "levelup", map).build()).queue()
                debug("User $id level-up to level ${currLevel+1} and got reward ${reward}!")

            }
        }

        addTimer(id, 0, time)
    }
}