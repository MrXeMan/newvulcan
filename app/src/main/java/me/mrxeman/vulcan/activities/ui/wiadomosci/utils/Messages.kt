package me.mrxeman.vulcan.activities.ui.wiadomosci.utils

import com.google.gson.JsonParser
import me.mrxeman.vulcan.utils.Extensions.asURL
import me.mrxeman.vulcan.utils.Extensions.asZonedDateTime
import java.net.URL
import java.time.ZonedDateTime

object Messages {

    val R_MESSAGES: MutableList<message> = ArrayList()
    val S_MESSAGES: MutableList<message> = ArrayList()

    fun loadReceived(temporary: String, share: String) {
        val topLevel = JsonParser.parseString(temporary).asJsonArray
        topLevel.forEach {
            val m = it.asJsonObject
            R_MESSAGES.add(
                message(
                    m.get("id").asInt,
                    m.get("data").asZonedDateTime,
                    Sender(
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
                ) {
                    val json = JsonParser.parseString(share).asJsonObject
                    if (json.get("id").asInt == m.get("id").asInt) {
                        val subject: String = json.get("tresc").asString
                        val z = json.get("zalaczniki").asJsonArray
                        val files: ArrayList<Attachment> = arrayListOf()
                        z.forEach { it2 ->
                            val zl = it2.asJsonObject
                            files.add(
                                Attachment(
                                    zl.get("nazwaPliku").asString,
                                    zl.get("url").asURL
                                )
                            )
                        }
                        return@message subject to files
                    } else {
                        return@message "" to arrayListOf()
                    }
                }
            )
        }
    }

    fun loadSent(temporary: String, share: String) {
        val topLevel = JsonParser.parseString(temporary).asJsonArray
        topLevel.forEach {
            val m = it.asJsonObject
            S_MESSAGES.add(
                message(
                    m.get("id").asInt,
                    m.get("data").asZonedDateTime,
                    Sender(
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
                ) {
                    val json = JsonParser.parseString(share).asJsonObject
                    if (json.get("id").asInt == m.get("id").asInt) {
                        val subject: String = json.get("tresc").asString
                        val z = json.get("zalaczniki").asJsonArray
                        val files: ArrayList<Attachment> = arrayListOf()
                        z.forEach { it2 ->
                            val zl = it2.asJsonObject
                            files.add(
                                Attachment(
                                    zl.get("nazwaPliku").asString,
                                    zl.get("url").asURL
                                )
                            )
                        }
                        return@message subject to files
                    } else {
                        return@message "" to arrayListOf()
                    }
                }
            )
        }
    }


    data class message(val id: Int, val date: ZonedDateTime, val sender: Sender, val topic: String, val receiver: String, val hasAttachments: Boolean,
                       val read: Boolean, val important: Boolean, val reverted: Boolean, val responded: Boolean, val forwarded: Boolean,
                       val extra: () -> Pair<String, ArrayList<Attachment>>)

    data class Sender(val key: String, val name: String, val role: Int)

    data class Attachment(val name: String, val url: URL)

}