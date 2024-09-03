package me.mrxeman.newvulcan


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.cookie
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Cookie
import io.ktor.http.setCookie
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class User(private val email: String, private val password: String) {

    private val loginURL: String = "https://eduvulcan.pl/logowanie"
    private val queryURL: String = "https://eduvulcan.pl/Account/QueryUserInfo"
    var verificationToken1: String? = null

    fun logIn(cookies: MutableMap<String, String>): Boolean = runBlocking {
        if (email.isEmpty()) {
            throw Exception("No email found! You need to login first!")
        }
        if (password.isEmpty()) {
            throw Exception("Seriously? All you need is password!")
        }

        getLoginCookies(cookies)

        if (checkAccount(cookies)) {
            return@runBlocking true
        } else {
            throw Exception("Couldn't get the query for this email! Invalid email, or nonexistent account!")
        }
    }

    private suspend fun getLoginCookies(cookies: MutableMap<String, String>) {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(loginURL) {
            for ((k, v) in cookies) {
                cookie(name=k, value=v)
            }
        }
        client.close()
        if (response.status.value in 200..299) {
            for (cookie: Cookie in response.setCookie()) {
                cookies[cookie.name] = cookie.value
            }
            val responseBody: String = response.body()
            val doc: Document = Jsoup.parse(responseBody)
            val inputElements: Elements = doc.getElementsByTag("input")
            var tokenElement: Element? = null
            for (ele: Element in inputElements) {
                if (ele.hasAttr("name") && ele.attr("name") == "__RequestVerificationToken") {
                    tokenElement = ele
                    break
                }
            }
            if (tokenElement == null) throw Exception("No element found with verification token! Please try again.")
            verificationToken1 = tokenElement.`val`()
        } else {
            throw Exception("Failed to get to the $loginURL. Possibly faulty.")
        }
    }

    private suspend fun checkAccount(cookies: MutableMap<String, String>): Boolean {
        val client = HttpClient(CIO)
        val response = client.get(queryURL) {
            url {
                parameters.append("__RequestVerificationToken", verificationToken1!!)
                parameters.append("alias", email)
            }
            for ((k, v) in cookies) {
                cookie(name=k, value=v)
            }
        }
        val data: String = response.body()
        val json = JSONObject(data)
        return json.getBoolean("success")
    }

}