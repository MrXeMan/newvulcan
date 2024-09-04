package me.mrxeman.newvulcan.eduvulcan

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.coroutines.runBlocking
import me.mrxeman.newvulcan.Extras.Global
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


class EduRequests {

    companion object {

        val basePage: String = "https://eduvulcan.pl"
        val uczenPage: String = "https://uczen.eduvulcan.pl"
        fun firstRequest(client: HttpClient): String = runBlocking {
            val response: HttpResponse = client.request(basePage) {
                method = HttpMethod.Get
                header(HttpHeaders.Cookie, Global.getCookiesString())
            }
            val body: String = response.body()
            val doc: Document = Jsoup.parse(body)
            val foundElements: Elements = doc.select("a[href].connected-account")
            if (foundElements.size == 1) return@runBlocking foundElements.unwrap().attr("href")
            throw Exception("Temporary excepton: first_request:32")
        }
    }

}