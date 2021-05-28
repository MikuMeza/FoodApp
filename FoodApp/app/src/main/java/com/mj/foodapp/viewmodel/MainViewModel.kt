package com.mj.foodapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mj.foodapp.data.Repository
import com.mj.foodapp.models.FoodRecipe
import com.mj.foodapp.util.NetworkResponse
import com.mj.foodapp.util.NetworkResult
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.temporal.TemporalQueries

class MainViewModel @ViewModelInject constructor(private val repository: Repository, application: Application) : AndroidViewModel(application) {


    fun getRecipiesSafeCall(queries: Map<String, String>): MutableLiveData<NetworkResult<FoodRecipe>> {
        var recipiesList = MutableLiveData<NetworkResult<FoodRecipe>>()
        viewModelScope.launch {
            recipiesList.value = NetworkResult.Loading()
            if (hasInternetConnection())
                try {
                    recipiesList.value = NetworkResponse<FoodRecipe>().handleNetworkResponse(repository.remote.getRecipies(queries))

                } catch (ex: Exception) {
                    recipiesList.value = NetworkResult.Error("No Result")
                }
            else
                recipiesList.value = NetworkResult.Error("No Intenet Connection")
        }
        return recipiesList
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}