package com.hillwar.retrofit

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MyApp : Application() {

    lateinit var service: MyApi

    var list: List<Post>? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        service = retrofit.create(MyApi::class.java)
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}
