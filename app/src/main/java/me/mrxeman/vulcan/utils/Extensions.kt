package me.mrxeman.vulcan.utils

import com.google.gson.JsonElement
import java.lang.Exception
import java.lang.UnsupportedOperationException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Extensions {

    val JsonElement.asStringOrNull: String?
        get() {
            return try {
                asString
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

}