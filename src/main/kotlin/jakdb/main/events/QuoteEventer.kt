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
            val user = event.author
            val juser = getUser(user.idLong)
            val replace = HashMap<String, String>()

            replace["<target.mention>"] = user.asMention
            replace["<target.tag>"] = user.asTag
            replace["<target.avatarUrl>"] = "${user.avatarUrl}"
            if(juser != null) {
                replace["<target.jakdb.uuid>"] = juser.uuid.toString()
                replace["<target.jakdb.rank>"] = "${juser.rank}"
                replace["<target.jakdb.global.level>"] = "${juser.globalLVL}"
                replace["<target.jakdb.global.xp>"] = "${juser.globalXP}"
                replace["<target.jakdb.regtime>"] = "${juser.regTime}"
            }
            
            var quote = getQuoteByName(guild, name)
            replace.forEach { quote = quote.replace(it.key, it.value) }
            event.channel.sendMessage(fromJSONToEmbeddedMessage(quote).build()).queue()
        }
    }
}