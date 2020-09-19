package jakdb.utils

import jakdb.data.mysql.getWarnsPunish
import jakdb.data.mysql.setWarnsPunish

@Synchronized
fun getAllWarnsAndAddNew(guild: Long, count: Int, type: String) {
    var warns = getWarnsPunish(guild).split(",")
    val punishs: HashMap<String, String> = HashMap()

    for(str in warns) {
        punishs[str.split(":")[0]] = str.split(":")[1]
    }
    punishs["$count"] = type

    var sql = ""
    val new: MutableList<String> = warns.toMutableList()
    new.clear()

    for(int in punishs.keys) {
//        debug("${punishs.size}")
//        debug(int)
//        debug("${punishs[int]}")
//        debug("${new.size}")
//        new[int.toInt()] = "$int:${punishs[int]}"
        new.add("$int:${punishs[int]}")
    }

    for(str in new) {
        sql += "$str,"
    }

    sql = sql.substring(0, sql.length-1)
    setWarnsPunish(guild, sql)
}

@Synchronized
fun getAllWarnsAndRemoveOld(guild: Long, count: Int) {
    val warns = getWarnsPunish(guild).split(",")
    val punishs: HashMap<String, String> = HashMap()

    for(str in warns) {
        punishs[str.split(":")[0]] = str.split(":")[1]
    }

    punishs["$count"] = "no"

    var sql = ""
    val new: MutableList<String> = warns.toMutableList()
    new.clear()

    for(int in punishs.keys) {
        new.add("$int:${punishs[int]}")
    }

    for(str in new) {
        sql += "$str,"
    }

    sql = sql.substring(0, sql.length-1)
    setWarnsPunish(guild, sql)

}