package com.example.paninisupporttickets.data.remote

import com.example.paninisupporttickets.core.AppConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstants.Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
