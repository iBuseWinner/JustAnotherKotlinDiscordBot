package jakdb.main.commands.modules.secret

import jakdb.jda
import jakdb.main.commands.ICommand
import jakdb.utils.fromStringToColor
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import java.util.concurrent.TimeUnit

class AboutBot(command: String, rank: Int, test: Boolean,
               usage: String, argsNeed: Int, desc: String,
               perm: Permission, module: String, aliases: Array<String>)
    : ICommand(command, rank, test, usage, argsNeed, desc, perm, module, aliases) {

    override fun execute(channel: MessageChannel, msg: Message, user: User, args: String) {
        val embed = EmbedBuilder()

        embed.setTitle("About JAKDB")
        embed.setDescription("JAKDB (Just Another Kotlin Discord Bot) is a bot which has many functions" +
                " such as level system, economy, punish commands and much more.\n\nIn `counter` category" +
                " you can see bot's stats (users and guilds count) which updating every 10 minutes.\n\n" +
                "About levels you can read below.\n\n\nGood luck & Have fun")
        embed.setColor(fromStringToColor("succ"))
        embed.setFooter("Have any questions? Ask in #global-chat or similar channels")

        val level = EmbedBuilder()

        level.setTitle("Level system")
        level.setDescription("**How to gain XP?**\nTo gain XP, you need to chat in text channels. " +
                "If JAKDB doesn't have permission to see messages in channel, you will not get XP. \n\n" +
                "**What is cooldown?**\n" +
                "Cooldown was made to prevent spammers gain XP. When you chat, you gain only 2 XP every 60 secs \n\n" +
                "**How to get multiplier?**\n" +
                "You need to donate us to get multiplier and reduce your cooldown. Use `!donate` to read more about donating.")
        level.setColor(fromStringToColor("succ"))

        val update = EmbedBuilder()

        update.setTitle("Updates?")
        update.setDescription("Updates will come when they will be tested and working fine. When it will be? Asap." +
                "We don't know when we complete our tests.\n\n" +
                "**Where can I follow the updates?**\nYou can see them in the #git channel or in the our " +
                "[trello board](https://trello.com/b/upnebIPz).\n\nDon't ping developers to ask about updates. " +
                "If they want to say about it, they will write in #announcements-en and similar channels.")
        update.setColor(fromStringToColor("succ"))
        update.setFooter("Be patient <3")

        val invite = EmbedBuilder()

        invite.setTitle("Inviting")
        invite.setDescription("If you want to use JAKD Bot in your guild, invite it by the url below or scan QR-code." +
                "\n[click this](https://discordapp.com/oauth2/authorize?client_id=509282444633964545&scope=bot&permissions=8)")
        invite.setImage("https://cdn.discordapp.com/attachments/678667068429828121/748198989760954388/unknown.png")
        invite.setColor(fromStringToColor("succ"))

        jda?.getTextChannelById(748125302110158898)?.sendMessage(embed.build())?.queue()
        jda?.getTextChannelById(748125302110158898)?.sendMessage(level.build())?.queue()
        jda?.getTextChannelById(748125302110158898)?.sendMessage(update.build())?.queue()
        jda?.getTextChannelById(748125302110158898)?.sendMessage(invite.build())?.queue()

        channel.sendMessage("Messages was sent!").queue {
            it.delete().queueAfter(10, TimeUnit.SECONDS)
        }
    }
}