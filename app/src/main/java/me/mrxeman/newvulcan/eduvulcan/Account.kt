package me.mrxeman.newvulcan.eduvulcan

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.runBlocking
import me.mrxeman.newvulcan.User

class Account(user: User) {

    private var email: String
    private var password: String
    private var verificationToken1: String

    lateinit var keyToken: String
    lateinit var dziennikToken: String

    init {
        email = user.email
        password = user.password
        verificationToken1 = user.verificationToken1!!
        println("Created account!")
    }

    fun getKey() = runBlocking {
        val client = HttpClient(CIO)
        dziennikToken = EduRequests.firstRequest(client).split("/")[2]
        client.close()
        println("Dziennik: $dziennikToken")
    }



}