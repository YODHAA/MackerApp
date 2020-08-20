package com.saurabh.mackerapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {

    private var retrofit: Retrofit? = null
    private var BASE_URL = "http://www.plantplaces.com"

    val retrofitInstance: Retrofit?
        get() {
            // has this object is created
            if (retrofit == null) {
                // create it
                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }


}