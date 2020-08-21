package jakdb.utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import java.awt.Color

@Synchronized
fun getMessage(lang: Int, module: String, message: String): String {
    
    return "No messages"
}

@Synchronized
fun fromJSONToEmbeddedMessage(message: String): MessageBuilder {
    val parser = JSONParser()
    var json = JSONObject()

    val msgBuilder = MessageBuilder()

    try {
        json = parser.parse(message) as JSONObject
    }catch (e: ParseException) {
        msgBuilder.setContent(message)
    }

    if(json.size == 0) return msgBuilder

    try {
        val content = json["content"] as String
        msgBuilder.setContent(content)
    } catch(ignored: NullPointerException) { }

    var embed = JSONObject()
    val embedB = EmbedBuilder()
    try {
        embed = json["embed"] as JSONObject
    } catch (ignored: NullPointerException) { }

    if(embed.size > 0) {
        try {
            val embedTitle = embed["title"] as String
            val url = embed["url"] as String
            if(url != null) {
                embedB.setTitle(embedTitle, url)
            } else {
                embedB.setTitle(embedTitle)
            }
        } catch (ignored: NullPointerException) { }

        try {
            val embedDesc = embed["description"] as String
            embedB.setDescription(embedDesc)
        } catch (ignored: NullPointerException) { }

        try {
            val color = (embed["color"] as Long).toInt()
            embedB.setColor(Color(color))
        } catch (ignored: NullPointerException) { }

        try {
            val thumbUrl = (embed["thumbnail"] as JSONObject)["url"] as String
            embedB.setThumbnail(thumbUrl)
        } catch (ignored: NullPointerException) { }

        try {
            val image = (embed["image"] as JSONObject)["url"] as String
            embedB.setImage(image)
        } catch (ignored: NullPointerException) { }

        var footer = JSONObject()
        try {
            footer = embed["footer"] as JSONObject
        } catch (ignored: NullPointerException) { }

        if(footer.size == 2) {
            try {
                val footText = footer["text"] as String
                val footIcon = footer["icon_url"] as String
                if(footIcon != null) {
                    embedB.setFooter(footText, footIcon)
                } else {
                    embedB.setFooter(footText)
                }
            } catch (ignored: NullPointerException) { }
        }

        var author = JSONObject()
        try {
            author = embed["author"] as JSONObject
        } catch (ignored: NullPointerException) { }

        if(author.size > 0) {
            try {
                val autText = author["name"] as String
                val autUrl = author["url"] as String
                val autIcon = author["icon_url"] as String

                if(autUrl != null && autIcon != null) {
                    embedB.setAuthor(autText, autUrl, autIcon)
                } else if(autIcon == null && autUrl != null) {
                    embedB.setAuthor(autText, autUrl)
                } else {
                    embedB.setAuthor(autText)
                }
            } catch (ignored: NullPointerException) { }
        }

        var fields = JSONArray()
        try {
            fields = embed["fields"] as JSONArray
        } catch (ignored: NullPointerException) { }

        if(fields.size > 0) {
            try {
                for (js: Any? in fields) {
                    var jso = js as JSONObject
                    val name = jso["name"] as String
                    val value = jso["value"] as String
                    var inline = false
                    try {
                        inline = jso["inline"] as Boolean
                    } catch(ignored: NullPointerException) { }

                    embedB.addField(name, value, inline)
                }
            } catch (ignored: NullPointerException) { }
        }

        msgBuilder.setEmbed(embedB.build())
    }

    return msgBuilder
}