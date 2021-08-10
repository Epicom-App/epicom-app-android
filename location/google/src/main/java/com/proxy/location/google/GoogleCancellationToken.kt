package com.proxy.location.google

import com.google.android.gms.tasks.OnTokenCanceledListener
import com.proxy.location.CancellationToken
import com.google.android.gms.tasks.CancellationToken as GoogleCancellationToken

internal fun CancellationToken.toGoogleCancellationToken(): GoogleCancellationToken =
    object : GoogleCancellationToken() {
        override fun isCancellationRequested() =
            this@toGoogleCancellationToken.isCancellationRequested()

        override fun onCanceledRequested(
            listener: OnTokenCanceledListener
        ) = this@toGoogleCancellationToken.toGoogleCancellationToken()
    }
