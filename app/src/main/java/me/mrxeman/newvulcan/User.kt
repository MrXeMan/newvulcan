package me.mrxeman.newvulcan

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.cookie
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Cookie
import io.ktor.http.appendPathSegments
import io.ktor.http.parameters
import io.ktor.http.setCookie
import kotlinx.coroutines.runBlocking
import me.mrxeman.newvulcan.Extras.Global
import me.mrxeman.newvulcan.Extras.Global.saveCookies
import me.mrxeman.newvulcan.Extras.Global.stoppingCode
import me.mrxeman.newvulcan.exceptions.EduVulcanStatusException
import me.mrxeman.newvulcan.exceptions.InvalidUserStringVersionException
import me.mrxeman.newvulcan.exceptions.LoggedNoFoundUserStringException
import me.mrxeman.newvulcan.exceptions.NoCookiesException
import me.mrxeman.newvulcan.exceptions.NoEmailFoundException
import me.mrxeman.newvulcan.exceptions.NoPasswordFoundException
import me.mrxeman.newvulcan.exceptions.QueryException
import me.mrxeman.newvulcan.exceptions.VerificationException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class User(val email: String, val password: String) {

    private val eduVulcanURL: String = "https://eduvulcan.pl"
    var verificationToken1: String? = null
    var loggedIn: Boolean = false

    fun logIn() = runBlocking {
        loggedIn = false
        if (email.isEmpty()) {
            throw NoEmailFoundException()
        }
        if (password.isEmpty()) {
            throw NoPasswordFoundException()
        }

        println("Getting login cookies...")

        getLoginCookies()

        println("Got login cookies!")

        if (checkAccount()) {
            println("Logged in!")
            loggedIn = true
            saveCookies()
        } else {
            throw QueryException()
        }
    }

    private suspend fun getLoginCookies() = runBlocking {
        println("Sending a request for login!")
        println("Cookies: ${Global.getCookies()}")
        val client = HttpClient(CIO)
        println("Created the client!")
        val response: HttpResponse = client.get(eduVulcanURL) {
            for (cookie in Global.getCookies()) {
                cookie(cookie.name, cookie.value, cookie.maxAge, cookie.expires, cookie.domain, cookie.path, cookie.secure, cookie.httpOnly, cookie.extensions)
            }
            url {
                appendPathSegments("logowanie")
            }
        }
        println("Getting response data...")
        client.close()
        println("Closed the client!")
        if (response.status.value in 200..299) {
            Global.addCookies(response.setCookie())
            println("Got response! Saving cookies...")
            saveCookies()
            println("Saved cookies!")
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
            if (tokenElement == null) {
                throw VerificationException(doc)
            }
            println("Got verification token from the page!")
            verificationToken1 = tokenElement.`val`()
        } else {
            throw EduVulcanStatusException(response.status);
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun checkAccount(): Boolean {
        val payload: MutableMap<String, String> = mutableMapOf()
        payload["Password"] = password
        payload["captchaUser"] = ""
        payload["Alias"] = email
        payload["__RequestVerificationToken"] = verificationToken1!!
        println("Payload: $payload")

        suspend fun getCookies(): List<Cookie> {
            val client = HttpClient(CIO) {
                followRedirects = false
            }
            val response: HttpResponse = client.submitForm(
                url = eduVulcanURL,
                formParameters = parameters {
                    for ((k, v) in payload) {
                        append(k, v)
                    }
                },
                encodeInQuery = false
            ) {
                for (cookie in Global.getCookies()) {
                    cookie(cookie.name, cookie.value, cookie.maxAge, cookie.expires, cookie.domain, cookie.path, cookie.secure, cookie.httpOnly, cookie.extensions)
                }
                url {
                    appendPathSegments("logowanie")
                }
            }
            client.close()
            if (response.setCookie().isEmpty()) throw NoCookiesException(response, response.body())
            return response.setCookie()
        }
        suspend fun getPage(): HttpResponse {
            val client = HttpClient(CIO) {
                followRedirects = true
            }
            val response: HttpResponse = client.submitForm(
                url=eduVulcanURL,
                formParameters = parameters {
                    for ((k, v) in payload) {
                        append(k, v)
                    }
                },
                encodeInQuery = true
            ) {
                for (cookie in Global.getCookies()) {
                    cookie(cookie.name, cookie.value, cookie.maxAge, cookie.expires, cookie.domain, cookie.path, cookie.secure, cookie.httpOnly, cookie.extensions)
                }
                url {
                    appendPathSegments("logowanie")
                }
            }
            client.close()
            return response
        }
        suspend fun query(): JSONObject {
            val queryCookies: MutableList<Cookie> = Global.getCookies()
            queryCookies.add(Cookie("_ga", "GA1.1.859489495.1725045331", domain=".eduvulcan.pl", path="/"))
            queryCookies.add(Cookie("_ga_D1SWPMTKK6", "GS1.1.1725045331.1.0.1725045362.0.0.0", domain=".eduvulcan.pl", path="/"))
            val client = HttpClient(CIO)
            val response: HttpResponse = client.submitForm(
                url = eduVulcanURL,
                formParameters = parameters {
                    for ((k, v) in payload) {
                        append(k, v)
                    }
                },
                encodeInQuery = false
            ) {
                for (cookie in queryCookies) {
                    cookie(cookie.name, cookie.value, cookie.maxAge, cookie.expires, cookie.domain, cookie.path, cookie.secure, cookie.httpOnly, cookie.extensions)
                }
                url {
                    appendPathSegments("Account", "QueryUserInfo")
                }
            }
            val query: String = response.body<String>()
            val json = JSONObject(query)
            if (!(json["success"] as Boolean)) {
                throw QueryException(response, json)
            }
            return json
        }

        println("JSON: ${query()}")
        val cookies = getCookies()
        val page: HttpResponse = getPage()

        Global.addCookies(cookies)
        saveCookies()

        return page.status.value in 200..299
    }

    companion object {
        fun createUserFromString(userString: String?): User {
            if (userString == null) throw LoggedNoFoundUserStringException()
            val list: List<String> = userString.split(stoppingCode)
            val version: Int = list[0].toInt()
            if (version == 0) {
                val user = User(list[1], list[2])
                user.verificationToken1 = list[3]
                return user
            }
            throw InvalidUserStringVersionException(version)
        }

        fun createUserString(user: User): String {
            val builder: StringBuilder = StringBuilder()
            builder.append(0) // 0
            builder.append(stoppingCode)
            builder.append(user.email) // 1
            builder.append(stoppingCode)
            builder.append(user.password) // 2
            builder.append(stoppingCode)
            builder.append(user.verificationToken1) // 3
            return builder.toString()
        }

    }

}