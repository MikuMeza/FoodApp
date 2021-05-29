package com.mj.foodapp.data

import com.mj.foodapp.data.database.RecipesDao
import com.mj.foodapp.data.database.RecipesEntity
import com.mj.foodapp.models.FoodRecipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao) {

    fun readDatabase(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }
    suspend fun inseartDatabase(recipesEntity:RecipesEntity){
        recipesDao.insertRecipes(recipesEntity)
    }
}