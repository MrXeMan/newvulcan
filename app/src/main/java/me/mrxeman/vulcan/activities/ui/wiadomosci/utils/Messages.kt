package me.mrxeman.vulcan.activities.ui.wiadomosci.utils

import android.text.Html
import android.text.Spanned
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import me.mrxeman.vulcan.utils.Extensions.asURL
import me.mrxeman.vulcan.utils.Extensions.asZonedDateTime
import me.mrxeman.vulcan.utils.Global
import java.net.URL
import java.time.ZonedDateTime
import kotlin.concurrent.thread

object Messages {

    val R_MESSAGES: MutableList<message> = ArrayList()
    val S_MESSAGES: MutableList<message> = ArrayList()

    fun loadReceived(messagesJSON: JsonElement) = thread {
        val topLevel = messagesJSON.asJsonArray
        topLevel.forEach {
            val m = it.asJsonObject
            val json =  Global.user.api.DetailMessageRequest(m.get("apiGlobalKey").asString).asJsonObject
            val extra = {
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
                subject to files
            }
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
                    m.get("przekazana").asBoolean,
                    extra.invoke()
                )
            )
        }
    }

    fun loadSent(messagesJSON: JsonElement) = thread {
        val topLevel = messagesJSON.asJsonArray
        topLevel.forEach {
            val m = it.asJsonObject
            val json =  Global.user.api.DetailMessageRequest(m.get("apiGlobalKey").asString).asJsonObject
            val extra = {
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
                subject to files
            }
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
                    m.get("przekazana").asBoolean,
                    extra.invoke()
                )
            )
        }
    }


    data class message(val id: Int, val date: ZonedDateTime, val sender: Sender, val topic: String, val receiver: String, val hasAttachments: Boolean,
                       val read: Boolean, val important: Boolean, val reverted: Boolean, val responded: Boolean, val forwarded: Boolean,
                       val extra: Pair<String, ArrayList<Attachment>>)  {

        fun getMessage(): Spanned {
            return Html.fromHtml(extra.first, Html.FROM_HTML_MODE_COMPACT)
        }

    }

    data class Sender(val key: String, val name: String, val role: Int)

    data class Attachment(val name: String, val url: URL)

}