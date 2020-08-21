package jakdb.utils

import java.util.*
import jakdb.data.mysql.*
import jakdb.jda
import jakdb.usersTime

@Synchronized
fun updateStats() {
    val timer = Timer()
    timer.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            if (usersTime > 0) {
                usersTime--
            } else {
                jda!!.getGuildChannelById(678526767731965952L)!!.manager.setName("Guilds ⇒ " + jda!!.guilds.size).queue()
                jda!!.getGuildChannelById(678526806604906497L)!!.manager.setName("Users ⇒ " + usersCount())

                debug("Users and Guilds channels updated!")
            }
        }
    }, 1000, 1000)
}

@Synchronized
fun removingCds() {
    val timer = Timer()
    timer.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            removeOneTime()
        }
    }, 1000, 1000)
}