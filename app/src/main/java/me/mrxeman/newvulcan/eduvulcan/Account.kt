package me.mrxeman.newvulcan.eduvulcan

import me.mrxeman.newvulcan.User

class Account(user: User) {

    private var email: String
    private var password: String
    private var verificationToken1: String

    lateinit var keyToken: String
    lateinit var dziennikToken: String
    lateinit var profileToken: String
    lateinit var loginEndPointToken: String
    lateinit var powiatToken: String

    init {
        email = user.email
        password = user.password
        verificationToken1 = user.verificationToken1!!
        println("Created account!")
    }

    fun getKey() {
        println("Requesting!")
        dziennikToken = EduRequests.firstRequest().split("/")[2]
        println("First request done! Requesting second...")
        profileToken = EduRequests.secondRequest(account = this@Account).split("=")[1]
        println("Second request done! Requesting third...")
        EduRequests.thirdRequest(this@Account)
        println("Third request done! Requesting fourth...")
        loginEndPointToken = EduRequests.fourthRequest()
        println("Fourth request done! Requesting fifth...")
        EduRequests.fifthRequest(this@Account)
        println("Fifth request done! Requesting sixth...")
        EduRequests.sixthRequest(this@Account)
        println("Sixth request done! Requesting seventh...")
        EduRequests.seventhRequest()
        println("Seventh request done! Requesting eighth...")
        EduRequests.eighthRequest()
        println("Eighth request done! Requesting ninth...")
        EduRequests.ninthRequest(this@Account)
        println("Ninth request done! Requesting tenth...")
        val pair: Pair<String, String> = EduRequests.tenthRequest(this@Account)
        println("Tenth request done! Requesting last...")
        keyToken = pair.first
        powiatToken = pair.second
        EduRequests.lastRequest(this@Account)
        println("All requests done!")
        println("Dziennik: $dziennikToken")
        println("Profile: $profileToken")
        println("loginEndPoint: $loginEndPointToken")
        println("keyToken: $keyToken")
        println("powiatToken: $powiatToken")
    }



}