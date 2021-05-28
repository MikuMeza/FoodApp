package com.mj.foodapp.util

import retrofit2.Response

class NetworkResponse<T>() {
    fun handleNetworkResponse(response: Response<T>): NetworkResult<T>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("TimeOut")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()==null -> {
                return NetworkResult.Error("Result not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }
}