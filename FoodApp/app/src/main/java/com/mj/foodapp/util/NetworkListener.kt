package com.mj.foodapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkListener : ConnectivityManager.NetworkCallback() {
    private val isNetworkAvailable = MutableStateFlow(false)

    fun checkNetworkAvailability(context: Context): MutableStateFlow<Boolean> {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected = false
        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }

            }
        }
        isNetworkAvailable.value = isConnected
        return isNetworkAvailable
    }
}