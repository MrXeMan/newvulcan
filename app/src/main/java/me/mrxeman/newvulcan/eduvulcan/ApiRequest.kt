package me.mrxeman.newvulcan.eduvulcan


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.http.HttpMethod
import io.ktor.http.parameters
import kotlinx.coroutines.runBlocking
import me.mrxeman.newvulcan.Extras.Global

class ApiRequest(val account: Account) {

    val apiPage = "https://uczen.eduvulcan.pl/${account.powiatToken}/api/"

    val main = "99f04f67-d7f6-4f30-9d02-61cdd656612f" // DZIENNIK ID ETC
    val todayLessons = "b92817db-ce21-45d2-abb4-c95251f11462" // DZISIEJSZY PLAN ZAJEC! + query key
    val todayMarks = "d33ed325-718a-4756-838b-5c395000590e" // DZISIEJSZE OCENY! + query key
    val weekCleaners = "b5f54be9-4afb-4703-b73d-dc27147669e4" // DYZURNI + query key
    val supervisors = "8a9bf9db-08f2-488b-a7b5-5ccafdb6f42a" // Wychowawcy? + query key
    val todayLuckyNumber = "b97bb5b5-e805-4a03-bf81-58423ea9b414" // Szczesliwy numerek + query key
    val todaySaves = "91b46f85-f542-494f-ad77-762f327c5b2d" // USPRAWIEDLIWIENIA + query key
    // all get requests

    fun mainRequest() = runBlocking {
        val client = HttpClient(CIO)
        val response = client.submitForm(
            url = "$apiPage$main"
        ) {
            header("cookies", Global.getCookiesString())
            method = HttpMethod.Get
        }
        println(response.request.headers)
        println(response.request.url)
        println(response.body<String>())
    }

    fun todayMarksRequest() = runBlocking {
        val client = HttpClient(CIO)
        val response = client.submitForm(
            url = "$apiPage$todayMarks",
            parameters {
                append("key", account.keyToken)
                append("idDziennik", account.dziennikID.toString())
            }
        ) {
            header("cookies", Global.getCookiesString())
            method = HttpMethod.Get
        }
        println(response.body<String>())
    }


}