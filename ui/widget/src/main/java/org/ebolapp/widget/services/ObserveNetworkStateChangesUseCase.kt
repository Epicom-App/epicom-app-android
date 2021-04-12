package org.ebolapp.widget.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface ObserveNetworkStateChangesUseCase {
    operator fun invoke(): Flow<NetworkState>
}

internal class ObserveNetworkStateChangesUseCaseImpl(context: Context) : ObserveNetworkStateChangesUseCase {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @ExperimentalCoroutinesApi
    override fun invoke(): Flow<NetworkState> = callbackFlow {

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                sendBlocking(NetworkState.CONNECTED)
                super.onAvailable(network)
            }

            override fun onLost(network: Network) {
                sendBlocking(NetworkState.DISCONNECTED)
                super.onLost(network)
            }

            override fun onUnavailable() {
                sendBlocking(NetworkState.DISCONNECTED)
                super.onUnavailable()
            }
        }

        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }

    }
}

enum class NetworkState {
    CONNECTED,
    DISCONNECTED
}
