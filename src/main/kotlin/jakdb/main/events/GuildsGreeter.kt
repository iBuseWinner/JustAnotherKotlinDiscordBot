package jakdb.main.events

import jakdb.data.mysql.*
import jakdb.utils.debug
import jakdb.utils.fromJSONToEmbeddedMessage
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class GuildsGreeter : ListenerAdapter() {

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        if(getHelloChannel(event.guild.idLong) == 0L) return

        if(getHelloMessage(event.guild.idLong) == ".") return
        val channel = event.guild.getTextChannelById(getHelloChannel(event.guild.idLong))
        if(channel == null) {
            setHelloChannel(event.guild.idLong, 0)
            return
        }

        val replace = HashMap<String, String>()

        val user = event.user

        if(user.isBot) return

        replace["<target.mention>"] = user.asMention
        replace["<target.id>"] = user.id
        replace["<target.avatarUrl>"] = "${user.avatarUrl}"

        if(!isUserExists(user.idLong)) {
            debug("User ${user.idLong} isn't exists so add him to MySQL")
            addUser(user.idLong)
        }

        val jakdb = getUser(user.idLong)
        if (jakdb != null) {
            replace["<target.jakdb.uuid>"] = jakdb.uuid.toString()
            replace["<target.jakdb.rank>"] = "${jakdb.rank}"
            replace["<target.jakdb.global.level>"] = "${jakdb.globalLVL}"
            replace["<target.jakdb.global.xp>"] = "${jakdb.globalXP}"
            replace["<target.jakdb.regtime>"] = "${jakdb.regTime}"
        }

        var hello = getHelloMessage(event.guild.idLong)
        replace.forEach { hello = hello.replace(it.key, it.value) }

        channel.sendMessage(fromJSONToEmbeddedMessage(hello).build()).queue()
    }

    override fun onGuildMemberLeave(event: GuildMemberLeaveEvent) {
        if(getByeChannel(event.guild.idLong) == 0L) return

        if(getByeMessage(event.guild.idLong) == ".") return
        val channel = event.guild.getTextChannelById(getByeChannel(event.guild.idLong))
        if(channel == null) {
            setByeChannel(event.guild.idLong, 0)
            return
        }

        val replace = HashMap<String, String>()

        val user = event.user

        if(user.isBot) return

        replace["<target.mention>"] = user.asMention
        replace["<target.id>"] = user.id
        replace["<target.avatarUrl>"] = "${user.avatarUrl}"

        if(!isUserExists(user.idLong)) {
            debug("User ${user.idLong} isn't exists so add him to MySQL")
            addUser(user.idLong)
        }

        val jakdb = getUser(user.idLong)
        if (jakdb != null) {
            replace["<target.jakdb.uuid>"] = jakdb.uuid.toString()
            replace["<target.jakdb.rank>"] = "${jakdb.rank}"
            replace["<target.jakdb.global.level>"] = "${jakdb.globalLVL}"
            replace["<target.jakdb.global.xp>"] = "${jakdb.globalXP}"
            replace["<target.jakdb.regtime>"] = "${jakdb.regTime}"
        }

        var bye = getByeMessage(event.guild.idLong)
        replace.forEach { bye = bye.replace(it.key, it.value) }

        channel.sendMessage(fromJSONToEmbeddedMessage(bye).build()).queue()
    }

}