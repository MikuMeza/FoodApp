package com.mj.foodapp.data

import com.mj.foodapp.data.network.FoodRecipiesApi
import com.mj.foodapp.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val foodRecipiesApi: FoodRecipiesApi) {

    suspend fun getRecipies(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipiesApi.getRecipes(queries)
    }
}