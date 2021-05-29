package com.mj.foodapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mj.foodapp.util.Constants.Companion.API_KEY
import com.mj.foodapp.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.mj.foodapp.util.Constants.Companion.QUERY_API_KEY
import com.mj.foodapp.util.Constants.Companion.QUERY_DIET
import com.mj.foodapp.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.mj.foodapp.util.Constants.Companion.QUERY_NUMBER
import com.mj.foodapp.util.Constants.Companion.QUERY_TYPE

class RecipiesViewModel(application: Application):AndroidViewModel(application) {
    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = "1748e4b82476437bb4ee0359fad418b4"
        queries[QUERY_TYPE] = "snack"
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}