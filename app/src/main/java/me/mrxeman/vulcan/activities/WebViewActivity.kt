package me.mrxeman.vulcan.activities

import android.app.Activity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import java.net.URL

class WebViewActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myWebView = WebView(this)
        setContentView(myWebView)
        if (!intent.hasExtra("URL")) {
            finish()
        } else {
            val url = URL(intent.getStringExtra("URL"))
            myWebView.loadUrl(url.toString())
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("finish()"))
    override fun onBackPressed() {
        finish()
    }
}