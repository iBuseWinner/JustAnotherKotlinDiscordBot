package kotlin.jakdb.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.jakdb.JAKDB

private val RESET = "\u001B[0m"
private val RED = "\u001B[31m"
private val BLACK = "\u001B[30m"
private val GREEN = "\u001B[32m"
private val YELLOW = "\u001B[33m"
private val BLUE = "\u001B[34m"
private val PURPLE = "\u001B[35m"
private val CYAN = "\u001B[36m"
private val WHITE = "\u001B[37m"

fun info(message: String) {
    val date = Date()
    val format = SimpleDateFormat("'" + GREEN + "['hh:mm:ss'/'dd.MM.yyyy'] " + YELLOW + "@ INFO »" + RESET + "'")
    println(format.format(date) + message)
}

fun debug(message: String) {
    if (JAKDB().debug) {
        val date = Date()
        val format = SimpleDateFormat("'" + PURPLE + "['hh:mm:ss'/'dd.MM.yyyy'] " + CYAN + "@ DEBUG »" + RESET + "'")
        println(format.format(date) + message)
    }
}

fun error(ex: Exception) {
    val date = Date()
    val format = SimpleDateFormat("'" + RED + "['hh:mm:ss'/'dd.MM.yyyy'] @ ERROR »" + RESET + "'")
    println(format.format(date) + ex.message)
}

/*class JAKDBLogger {
    private val RESET = "\u001B[0m"
    private val RED = "\u001B[31m"
    private val BLACK = "\u001B[30m"
    private val GREEN = "\u001B[32m"
    private val YELLOW = "\u001B[33m"
    private val BLUE = "\u001B[34m"
    private val PURPLE = "\u001B[35m"
    private val CYAN = "\u001B[36m"
    private val WHITE = "\u001B[37m"

    fun info(message: String) {
        val date = Date()
        val format = SimpleDateFormat("'" + GREEN + "['hh:mm:ss'/'dd.MM.yyyy'] " + YELLOW + "@ INFO »" + RESET + "'")
        println(format.format(date) + message)
    }

    fun debug(message: String) {
        if (JAKDB().debug) {
            val date = Date()
            val format = SimpleDateFormat("'" + PURPLE + "['hh:mm:ss'/'dd.MM.yyyy'] " + CYAN + "@ DEBUG »" + RESET + "'")
            println(format.format(date) + message)
        }
    }

    fun error(ex: Exception) {
        val date = Date()
        val format = SimpleDateFormat("'" + RED + "['hh:mm:ss'/'dd.MM.yyyy'] @ ERROR »" + RESET + "'")
        println(format.format(date) + ex.message)
    }
}*/