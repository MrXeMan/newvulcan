package me.mrxeman.newvulcan.eduvulcan

import com.chaquo.python.PyObject
import com.chaquo.python.Python
import me.mrxeman.newvulcan.Extras.Global
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class EduRequests {

    companion object {

        private var h: Int = 1

        private fun toConsole(text: String) {
            println()
            println()
            println()
            println(h)
            println(text)
            h += 1
        }

        lateinit var lastResponse: String
        lateinit var wsignin: String
        lateinit var wresult: String
        lateinit var wctx: String
        lateinit var lastElement: String

        val basePage: String = "https://eduvulcan.pl"
        val uczenPage: String = "https://uczen.eduvulcan.pl"

        fun firstRequest(): String {
            val py = Python.getInstance()
            val module = py.getModule("request")
            val firstRequest: PyObject = module["first_request"]!!
            val response = firstRequest.call(PyObject.fromJava(basePage), PyObject.fromJava(Global.getCookiesString()))
            val body: String = response["text"]!!.toString()
            val doc: Document = Jsoup.parse(body)
            return doc.select("a[href].connected-account").attr("href")
        }

        fun secondRequest(account: Account): String {
            val py = Python.getInstance()
            val module = py.getModule("request")
            val firstRequest: PyObject = module["second_request"]!!
            val response = firstRequest.call(
                PyObject.fromJava("$basePage/dziennik/${account.dziennikToken}"),
                PyObject.fromJava(Global.getCookiesString())
            )
            val headers: Map<PyObject, PyObject> = response["headers"]!!.asMap()
            lastResponse = headers[PyObject.fromJava("location")]!!.toString()
            return headers[PyObject.fromJava("location")]!!.toString()
        }

        fun thirdRequest(account: Account) {
            val py = Python.getInstance()
            val module = py.getModule("request")
            val firstRequest: PyObject = module["third_request"]!!
            val response = firstRequest.call(
                PyObject.fromJava(lastResponse),
                PyObject.fromJava(Global.getCookiesString()),
                PyObject.fromJava(account.profileToken)
            )
            val headers: Map<PyObject, PyObject> = response["headers"]!!.asMap()
            val cookies: Map<PyObject, PyObject> = response["cookies"]!!.callAttr("get_dict")!!.asMap()
            lastResponse = headers[PyObject.fromJava("location")]!!.toString()
            Global.addPyCookies(cookies)
            Global.saveCookies()
        }

        fun fourthRequest(): String {
            val py = Python.getInstance()
            val module = py.getModule("request")
            val firstRequest: PyObject = module["fourth_request"]!!
            val response = firstRequest.call(
                PyObject.fromJava(lastResponse),
                PyObject.fromJava(Global.getCookiesString())
            )
            val headers: Map<PyObject, PyObject> = response["headers"]!!.asMap()
            val cookies: Map<PyObject, PyObject> = response["cookies"]!!.callAttr("get_dict")!!.asMap()
            lastResponse = headers[PyObject.fromJava("location")]!!.toString()
            Global.addPyCookies(cookies)
            Global.saveCookies()
            return lastResponse
        }

        fun fifthRequest(account: Account) {
            val py = Python.getInstance()
            val module = py.getModule("request")
            val firstRequest: PyObject = module["fifth_request"]!!
            val response = firstRequest.call(
                PyObject.fromJava(lastResponse),
                PyObject.fromJava(Global.getCookiesString()),
                PyObject.fromJava(account.profileToken)
            )
            val headers: Map<PyObject, PyObject> = response["headers"]!!.asMap()
            lastResponse = headers[PyObject.fromJava("location")]!!.toString()
            wsignin = lastResponse
        }

        fun sixthRequest(account: Account) {
            val py = Python.getInstance()
            val module = py.getModule("request")
            val firstRequest: PyObject = module["sixth_request"]!!
            val response = firstRequest.call(
                PyObject.fromJava(lastResponse),
                PyObject.fromJava(Global.getCookiesString()),
                PyObject.fromJava(account.loginEndPointToken)
            )
            val headers: Map<PyObject, PyObject> = response["headers"]!!.asMap()
            lastResponse = headers[PyObject.fromJava("location")]!!.toString()
        }

        fun seventhRequest() {
            val py = Python.getInstance()
            val module = py.getModule("request")
            val firstRequest: PyObject = module["seventh_request"]!!
            val response = firstRequest.call(
                PyObject.fromJava(lastResponse),
                PyObject.fromJava(Global.getCookiesString()),
                PyObject.fromJava(wsignin)
            )
            val cookies: Map<PyObject, PyObject> = response["cookies"]!!.callAttr("get_dict")!!.asMap()
            Global.addPyCookies(cookies)
            Global.saveCookies()
            val body: String = response["text"]!!.toString()
            val doc = Jsoup.parse(body)
            wresult = doc.select("input[name=wresult]").attr("value")
            wctx = doc.select("input[name=wctx]").attr("value")
        }

        fun eighthRequest() {
            val py = Python.getInstance()
            val module = py.getModule("request")
            val firstRequest: PyObject = module["eighth_request"]!!
            val response = firstRequest.call(
                PyObject.fromJava(wsignin),
                PyObject.fromJava(Global.getCookiesString()),
                PyObject.fromJava(wresult),
                PyObject.fromJava(wctx)
            )
            val cookies: Map<PyObject, PyObject> = response["cookies"]!!.callAttr("get_dict")!!.asMap()
            Global.addPyCookies(cookies)
            Global.saveCookies()
            val body: String = response["text"]!!.toString()
            val doc = Jsoup.parse(body)
            wresult = doc.select("input[name=wresult]").attr("value")
            wctx = doc.select("input[name=wctx]").attr("value")
        }

        fun ninthRequest(account: Account) {
            val py = Python.getInstance()
            val module = py.getModule("request")
            val firstRequest: PyObject = module["ninth_request"]!!
            val response = firstRequest.call(
                PyObject.fromJava(account.loginEndPointToken),
                PyObject.fromJava(Global.getCookiesString()),
                PyObject.fromJava(wresult),
                PyObject.fromJava(wctx)
            )
            val headers: Map<PyObject, PyObject> = response["headers"]!!.asMap()
            val cookies: Map<PyObject, PyObject> = response["cookies"]!!.callAttr("get_dict")!!.asMap()
            lastResponse = headers[PyObject.fromJava("location")]!!.toString()
            Global.addPyCookies(cookies)
            Global.saveCookies()
        }

        fun tenthRequest(account: Account): Pair<String, String> {
            val py = Python.getInstance()
            val module = py.getModule("request")
            val firstRequest: PyObject = module["tenth_request"]!!
            val response = firstRequest.call(
                PyObject.fromJava(lastResponse),
                PyObject.fromJava(Global.getCookiesString()),
                PyObject.fromJava(account.profileToken)
            )
            val headers: Map<PyObject, PyObject> = response["headers"]!!.asMap()
            val cookies: Map<PyObject, PyObject> = response["cookies"]!!.callAttr("get_dict")!!.asMap()
            lastResponse = headers[PyObject.fromJava("location")]!!.toString()
            Global.addPyCookies(cookies)
            Global.saveCookies()
            val body: String = response["text"]!!.toString()
            val doc = Jsoup.parse(body)
            val element = doc.select("a[href]").attr("href")
            lastElement = element
            val key = element.split("/")[element.split("/").size - 2]
            val powiat = element.split("/")[1]
            return key to powiat
        }

        fun lastRequest(account: Account) {
            val py = Python.getInstance()
            val module = py.getModule("request")
            val firstRequest: PyObject = module["last_request"]!!
            val response = firstRequest.call(
                PyObject.fromJava("$uczenPage/${account.powiatToken}${lastElement.split("=")[lastElement.split("=").size - 1]}"),
                PyObject.fromJava(Global.getCookiesString())
            )
            val cookies: Map<PyObject, PyObject> = response["cookies"]!!.callAttr("get_dict")!!.asMap()
            Global.addPyCookies(cookies)
            Global.saveCookies()
        }

    }

}