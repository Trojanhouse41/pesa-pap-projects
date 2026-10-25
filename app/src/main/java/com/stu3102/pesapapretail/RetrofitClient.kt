package com.stu3102.pesapapretail

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // IMPORTANT: 10.0.2.2 is the special address for the Android Emulator to talk to your PC's localhost
    private const val BASE_URL = "http://10.0.2.2:5000/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}