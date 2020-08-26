package jakdb.main.events

import jakdb.commands
import jakdb.data.mysql.*
import jakdb.defPrefix
import jakdb.symbolMoney
import jakdb.utils.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
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

        onMsgForCommand(e)
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
            mult += 1
            time -= 5L
        } else if(rank in 4..8) {
            mult += 2
            time -= 10L
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

    private fun onMsgForCommand(e: MessageReceivedEvent) {
        var msg = e.message.contentRaw
        if(msg.startsWith(defPrefix)) {
            msg = msg.substring(1)
            for(cmd in commands) {
                if(msg.toLowerCase().startsWith(cmd.command) || cmd.aliases.contains(msg.toLowerCase())) {
                    val user = getUser(e.author.idLong)

                    if (user != null) {
                        debug("User ${user.discordId} tried to use command")
                        if(user.rank >= cmd.rank) {
                            if(!isTimerExists(user.discordId, 1)) {
                                if(user.rank < 2) {
                                    addTimer(user.discordId, 1, 5)
                                }

                                var argss = msg.split(" ")
                                if(argss.size > 1) {
                                    argss = argss.drop(1)
                                }
                                var args = ""
                                if(argss.isNotEmpty()/* && argss[0] == e.message.contentRaw.substring(1)*/) {
                                    for (str in argss) {
                                        args += "$str "
                                    }
                                }

                                if(e.channelType == ChannelType.TEXT) {
                                    cmd.execute(e.channel, e.message, e.author, args)
                                    command("${user.discordId}", cmd.command, args, e.channel.id)
                                } else {
                                    if(cmd.guildOnly) {
                                        val replace = HashMap<String, String>()
                                        e.channel.sendMessage(getGlobalMessage("guildOnly", replace).build()).queue()
                                    } else {
                                        cmd.execute(e.channel, e.message, e.author, args)
                                        command("${user.discordId}", cmd.command, args, e.channel.id)
                                    }
                                }
                            } else {
                                val replace = HashMap<String, String>()
                                replace["<command.cooldown>"] = "${getTimeCd(user.discordId, 1)}"
                                replace["<command.name>"] = cmd.command
                                e.channel.sendMessage(getDebugMessage("cooldown", replace).build()).queue()
                            }
                        } else {
                            val replace = HashMap<String, String>()
                            replace["<command.rank>"] = "${cmd.rank}"
                            e.channel.sendMessage(getDebugMessage("noRank", replace).build()).queue()
                        }
                    }
                }
            }
        }
    }
}