package com.mj.foodapp

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.mj.foodapp.data.Repository

class MainViewModel @ViewModelInject constructor(private val repository: Repository, application: Application) : AndroidViewModel(application) {
}