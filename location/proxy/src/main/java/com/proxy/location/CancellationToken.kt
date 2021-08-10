package com.proxy.location

interface CancellationToken {
    fun isCancellationRequested(): Boolean
    fun onCanceledRequested(onTokenCanceledListener: OnTokenCanceledListener): CancellationToken
}

interface OnTokenCanceledListener {
    fun onCanceled()
}
