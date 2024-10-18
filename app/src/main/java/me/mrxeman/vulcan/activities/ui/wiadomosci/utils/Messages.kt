package me.mrxeman.vulcan.activities.ui.wiadomosci.utils

import com.google.gson.JsonParser
import me.mrxeman.vulcan.utils.Extensions.asZonedDateTime
import java.time.ZonedDateTime

object Messages {

    val R_MESSAGES: MutableList<message> = ArrayList()
    val S_MESSAGES: MutableList<message> = ArrayList()

    fun loadReceived(temporary: String) {
        val topLevel = JsonParser.parseString(temporary).asJsonArray
        topLevel.forEach {
            val m = it.asJsonObject
            R_MESSAGES.add(
                message(
                    m.get("id").asInt,
                    m.get("data").asZonedDateTime,
                    sender(
                        m.get("apiGlobalKey").asString,
                        m.get("korespondenci").asString,
                        m.get("uzytkownikRola").asInt
                    ),
                    m.get("temat").asString,
                    m.get("skrzynka").asString,
                    m.get("hasZalaczniki").asBoolean,
                    m.get("przeczytana").asBoolean,
                    m.get("wazna").asBoolean,
                    m.get("wycofana").asBoolean,
                    m.get("odpowiedziana").asBoolean,
                    m.get("przekazana").asBoolean
                )
            )
        }
    }

    fun loadSent(temporary: String) {
        val topLevel = JsonParser.parseString(temporary).asJsonArray
        topLevel.forEach {
            val m = it.asJsonObject
            S_MESSAGES.add(
                message(
                    m.get("id").asInt,
                    m.get("data").asZonedDateTime,
                    sender(
                        m.get("apiGlobalKey").asString,
                        m.get("korespondenci").asString,
                        m.get("uzytkownikRola").asInt
                    ),
                    m.get("temat").asString,
                    m.get("skrzynka").asString,
                    m.get("hasZalaczniki").asBoolean,
                    m.get("przeczytana").asBoolean,
                    m.get("wazna").asBoolean,
                    m.get("wycofana").asBoolean,
                    m.get("odpowiedziana").asBoolean,
                    m.get("przekazana").asBoolean
                )
            )
        }
    }


    data class message(val id: Int, val date: ZonedDateTime, val sender: sender, val topic: String, val receiver: String, val attachments: Boolean,
                       val read: Boolean, val important: Boolean, val reverted: Boolean, val responded: Boolean, val forwarded: Boolean)

    data class sender(val key: String, val name: String, val role: Int)

}