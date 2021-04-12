package org.ebolapp.presentation.webview

import android.content.Context
import android.content.Intent
import android.net.MailTo
import android.webkit.WebView
import android.webkit.WebViewClient


open class EpicomWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url?.startsWith("mailto:") == true) {
            val mt = MailTo.parse(url)
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf(mt.to))
            i.putExtra(Intent.EXTRA_SUBJECT, mt.subject)
            i.putExtra(Intent.EXTRA_CC, mt.cc)
            i.putExtra(Intent.EXTRA_TEXT, mt.body)
            view?.context?.startActivity(i)
            view?.reload()
            return true
        }
        return super.shouldOverrideUrlLoading(view, url)
    }
}