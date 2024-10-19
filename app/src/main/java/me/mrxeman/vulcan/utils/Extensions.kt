package me.mrxeman.vulcan.utils

import com.google.gson.JsonElement
import java.lang.Exception
import java.lang.UnsupportedOperationException
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Extensions {

    val JsonElement.asStringOrNull: String?
        get() {
            return try {
                asString.ifEmpty {
                    null
                }
            } catch (_: UnsupportedOperationException) {
                null
            }
        }
    val JsonElement.asIntOrNull: Int?
        get() {
            return try {
                asInt
            } catch (_: UnsupportedOperationException) {
                null
            }
        }

    val JsonElement.asLocalDate: LocalDate
        get() {
            return LocalDate.parse(asString.split("T")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

    val JsonElement.asLocalTime: LocalTime
        get() {
            return LocalTime.parse(asString.split("T")[1].split("+")[0], DateTimeFormatter.ofPattern("HH:mm:ss"))
        }

    val JsonElement.asZonedDateTime: ZonedDateTime
        get() {
            return ZonedDateTime.parse(asString, DateTimeFormatter.ISO_DATE_TIME)
        }

    val JsonElement.asURL: URL
        get() {
            return URL(asString)
        }

    val JsonElement.asLocalDateOrNull: LocalDate?
        get() {
            return try {
                asLocalDate
            } catch (_: Exception) {
                null
            }
        }

    val JsonElement.asLocalTimeOrNull: LocalTime?
        get() {
            return try {
                asLocalTime
            } catch (_: Exception) {
                null
            }
        }

    fun LocalTime?.format(): String? {
        return this?.format(Global.hourFormat)
    }

    fun LocalDate?.format(): String? {
        return this?.format(Global.dayFormat)
    }

    fun String?.ifNull(): String {
        return this ?: "Brak"
    }

    fun LocalDate?.ifNull(): String {
        return this?.toString() ?: "Brak"
    }

}