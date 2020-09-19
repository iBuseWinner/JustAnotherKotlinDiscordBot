package jakdb.main.events

import jakdb.*
import jakdb.data.mysql.*
import jakdb.utils.*
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.entities.ChannelType

class JAKDBEventer : ListenerAdapter() {
    override fun onMessageReceived(e: MessageReceivedEvent) {
        if(e.author.isBot) return

        if(!isUserExists(e.author.idLong)) {
            debug("User ${e.author.id} isn't exists so add him to MySQL")
            addUser(e.author.idLong)
        }

        if(!isUsSetExists(e.author.idLong)) {
            addUsSet(e.author.idLong)
        }

        if(e.isFromType(ChannelType.TEXT)) {
            if(!isGuildExists(e.guild.idLong)) {
                debug("Guild ${e.guild.id} isn't exists so add it to MySQL")
                addGuild(e.guild.idLong)
            }

            if(!isGuildSetExists(e.guild.idLong)) {
                debug("GuildSettings ${e.guild.id} isn't exists to add it to MySQL")
                addGuildSettings(e.guild.idLong)
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
        totalXPgain += xpADD
        addXP(id, xpADD)
        currXP = currXP?.plus(xpADD)

        val totalXPToLevelUp = currLevel?.plus(1)?.times(33)?.plus(9)

        if (currXP != null) {
            //debug("Users $id current XP: $currXP and total XP: $totalXPToLevelUp")
            if(currXP >= totalXPToLevelUp!!) {
                currLevel.plus(1)
                val map: HashMap<String, String> = HashMap()

                map["<level.last>"] = "${currLevel-1}"
                map["<level.current>"] = "${currLevel+1}"
                map["<level.next>"] = "${currLevel+2}"

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

            if (e.channelType == ChannelType.TEXT) {
                val self = e.jda.selfUser
                if(!e.textChannel.guild.getMember(self)?.hasAccess(e.textChannel)!! ||
                        !e.textChannel.guild.getMember(self)?.hasPermission(Permission.MESSAGE_WRITE)!!) {
                    e.member?.idLong?.let { it ->
                        jda?.getUserById(it)?.openPrivateChannel()?.queue {
                            it.sendMessage("Hey, I don't have permission to channel **${e.channel.name}**! " +
                                    "If you Admin of guild **${e.textChannel.guild.name}**, fix it " +
                                    "(give me permission to read and send messages)").queue()
                        }
                    }

                    return
                }

                val first = msg.split(" ")[0]

                for (cmd in commands) {
                    if (first.toLowerCase() == cmd.command || cmd.aliases.contains(first)) {
                        val user = getUser(e.author.idLong)

                        if (user != null) {
                            if(user.isBanned == 1) {
                                if(cmd.command != "checkban") {
                                    return
                                }
                            }

                            if (user.rank >= cmd.rank) {
                                if (!isTimerExists(user.discordId, 1)) {
                                    if (user.rank < 2) {
                                        addTimer(user.discordId, 1, 5)
                                    }

                                    var argss = msg.split(" ")
                                    var args = ""
                                    if (argss.isNotEmpty()/* && argss[0] == e.message.contentRaw.substring(1)*/) {
                                        argss = argss.drop(1)
                                        for (str in argss) {
                                            args += "$str "
                                        }
                                    }

                                    if (args.isNotEmpty()) {
                                        args = args.substring(0, args.length - 1)
                                    }

                                    if(cmd.argsNeed > args.length) {
                                        if(getUsDebug(e.author.idLong)) {
                                            e.channel.sendMessage(getDebugMessage("noargs", HashMap()).build()).queue()
                                        }
                                        return
                                    }

                                    if (e.message.member?.hasPermission(cmd.perm)!!) {
                                        cmd.execute(e.channel, e.message, e.author, args)
                                        commandsUsed++
                                        command("${user.discordId}", cmd.command, args, e.channel.id)
                                    } else {
                                        if(getUsDebug(e.author.idLong)) {
                                            e.channel.sendMessage(getDebugMessage("noperm", HashMap()).build()).queue()
                                        }
                                    }
                                } else {
                                    val replace = HashMap<String, String>()
                                    replace["<command.cooldown>"] = "${getTimeCd(user.discordId, 1)}"
                                    replace["<command.name>"] = cmd.command
                                    if(getUsDebug(e.author.idLong)) {
                                        e.channel.sendMessage(getDebugMessage("cooldown", replace).build()).queue()
                                    }
                                }
                            } else {
                                val replace = HashMap<String, String>()
                                replace["<command.rank>"] = "${cmd.rank}"
                                if(getUsDebug(e.author.idLong)) {
                                    e.channel.sendMessage(getDebugMessage("noRank", replace).build()).queue()
                                }
                            }
                        }
                    }
                }
            } else {
                e.channel.sendMessage(getGlobalMessage("guildOnly", HashMap()).build()).queue()
            }
        }
    }
}