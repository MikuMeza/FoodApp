package com.mj.foodapp.util

sealed class NetworkResults<T>(val data:T?=null,val message:String?=null){
    class Success<T>(data: T):NetworkResults<T>(data)
    class Error<T>(data: T?=null,message: String?):NetworkResults<T>(data,message)
    class Loading<T>():NetworkResults<T>()
}

