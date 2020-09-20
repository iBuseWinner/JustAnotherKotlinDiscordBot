package jakdb.utils

import jakdb.data.mysql.getWarnReasonsForUser
import jakdb.data.mysql.getWarnsPunish
import jakdb.data.mysql.setWarnReasonsForUser
import jakdb.data.mysql.setWarnsPunish

@Synchronized
fun getAllWarnsAndAddNew(guild: Long, count: Int, type: String) {
    var warns = getWarnsPunish(guild).split(",")
    val punishs: HashMap<String, String> = HashMap()
    for(str in warns) { punishs[str.split(":")[0]] = str.split(":")[1] }
    punishs["$count"] = type
    var sql = ""
    val new: MutableList<String> = warns.toMutableList()
    new.clear()
    for(int in punishs.keys) { new.add("$int:${punishs[int]}") }
    for(str in new) { sql += "$str," }
    sql = sql.substring(0, sql.length-1)
    setWarnsPunish(guild, sql)
}

@Synchronized
fun getAllWarnsAndRemoveOld(guild: Long, count: Int) {
    val warns = getWarnsPunish(guild).split(",")
    val punishs: HashMap<String, String> = HashMap()
    for(str in warns) { punishs[str.split(":")[0]] = str.split(":")[1] }
    punishs["$count"] = "no"
    var sql = ""
    val new: MutableList<String> = warns.toMutableList()
    new.clear()
    for(int in punishs.keys) { new.add("$int:${punishs[int]}") }
    for(str in new) { sql += "$str," }
    sql = sql.substring(0, sql.length-1)
    setWarnsPunish(guild, sql)
}

/**
 * It adds reason after last reason.
 */
@Synchronized
fun getAllUserWarnsAndAddNew(guild: Long, user: Long, reason: String) {
    val warns = getWarnReasonsForUser(guild, user)
    setWarnReasonsForUser(guild, user, "$warns,{\"$reason\"}")
}

/**
 * It removes only last reason.
 * It will not remove specific reason, because it can't do this.
 */
@Synchronized
fun getAllUserWarnsAndRemoveOld(guild: Long, user: Long) {
    val warns = getWarnReasonsForUser(guild, user)
    val w = warns.split(",")

    var i = 0
    val ww = HashMap<String, String>()
    for(d in w) {
        ww["$i"] = d
        i++
    }

    ww.remove("$i")

    var total = ""
    for(d in ww.keys) {
        total = "$total,{\"${ww[d]}\"}"
    }
    setWarnReasonsForUser(guild, user, total)
}
