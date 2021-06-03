package com.mj.foodapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mj.foodapp.data.Repository
import com.mj.foodapp.data.database.RecipesEntity
import com.mj.foodapp.models.FoodRecipe
import com.mj.foodapp.util.NetworkResponse
import com.mj.foodapp.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.temporal.TemporalQueries

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val readRecipies = repository.local.readDatabase().asLiveData()

    fun insertRecipies(recipesEntity: RecipesEntity) {
        viewModelScope.launch(Dispatchers.IO) { repository.local.inseartDatabase(recipesEntity) }
    }

    //REtrofit

    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipiesSafeCall(queries)
    }

    fun getRecipiesSafeCall(queries: Map<String, String>) {

        viewModelScope.launch {
            recipesResponse.value = NetworkResult.Loading()
            if (hasInternetConnection())
                try {
                    var response = repository.remote.getRecipies(queries)
                    recipesResponse.value =
                        NetworkResponse<FoodRecipe>().handleNetworkResponse(response)

                    val recipes = recipesResponse.value!!.data
                    if (recipes != null)
                        offlineCacheResponse(recipes)
                   

                } catch (ex: Exception) {
                    recipesResponse.value = NetworkResult.Error("No Result")
                }
            else
                recipesResponse.value = NetworkResult.Error("No Intenet Connection")
        }
    }

    fun offlineCacheResponse(foodRecipe: FoodRecipe) {
        insertRecipies(RecipesEntity(foodRecipe))
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