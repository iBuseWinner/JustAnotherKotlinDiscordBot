package jakdb.utils

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
    val format = SimpleDateFormat("'" + GREEN + "['hh:mm:ss'/'dd.MM.yyyy'] " + YELLOW + "@ INFO >>" + RESET + "'")
    println(format.format(date) + message)
}

fun debug(message: String) {
    if (jakdb.debug) {
        val date = Date()
        val format = SimpleDateFormat("'" + PURPLE + "['hh:mm:ss'/'dd.MM.yyyy'] " + CYAN + "@ DEBUG >>" + RESET + "'")
        println(format.format(date) + message)
    }
}

fun error(ex: Exception) {
    val date = Date()
    val format = SimpleDateFormat("'" + RED + "['hh:mm:ss'/'dd.MM.yyyy'] @ ERROR >>" + RESET + "'")
    println(format.format(date) + ex.message)
}