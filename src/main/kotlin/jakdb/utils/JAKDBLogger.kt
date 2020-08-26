package jakdb.utils

import jakdb.jda
import net.dv8tion.jda.api.EmbedBuilder
import java.text.SimpleDateFormat
import java.util.*

private const val RESET = "\u001B[0m"
private const val RED = "\u001B[31m"
private const val BLACK = "\u001B[30m"
private const val GREEN = "\u001B[32m"
private const val YELLOW = "\u001B[33m"
private const val BLUE = "\u001B[34m"
private const val PURPLE = "\u001B[35m"
private const val CYAN = "\u001B[36m"
private const val WHITE = "\u001B[37m"

fun info(message: String) {
    val date = Date()
    val format = SimpleDateFormat("'$GREEN['HH:mm:ss'/'dd.MM.yyyy'] $YELLOW@ INFO >>$RESET '")
    println(format.format(date) + message)
}

fun command(userId: String, command: String, args: String, channel: String) {
    val date = Date()
    val format = SimpleDateFormat("'$GREEN['HH:mm:ss'/'dd.MM.yyyy'] $BLUE@ CMD >>$RESET '")
    println(format.format(date) + "User $BLUE$userId$RESET used cmd " +
            "$BLUE$command$RESET in channel $BLUE$channel$RESET with args $BLUE$args$RESET")

    val embed = EmbedBuilder()
    embed.setTitle("Command logger")
    embed.setDescription("User **$userId** used command **$command** in channel **$channel** with args **$args**")

    val otherFormat = SimpleDateFormat("'['HH:mm:ss'/'dd.MM.yyyy']'")
    embed.setFooter("Time: ${otherFormat.format(date)}")

    jda?.getTextChannelById(678651613228957747)?.sendMessage(embed.build())?.queue()
}

fun debug(message: String) {
    if (jakdb.debug) {
        val date = Date()
        val format = SimpleDateFormat("'$PURPLE['HH:mm:ss'/'dd.MM.yyyy'] $CYAN@ DEBUG >>$RESET '")
        println(format.format(date) + message)
    }
}

fun error(ex: Exception) {
    val date = Date()
    val format = SimpleDateFormat("'$RED['HH:mm:ss'/'dd.MM.yyyy'] @ ERROR >> '")
    println(format.format(date) + ex.message)
}