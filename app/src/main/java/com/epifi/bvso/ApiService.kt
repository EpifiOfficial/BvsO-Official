package com.epifi.bvso

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getCoinPrice(@Url url:String):Response<CoinGeckoResponse>
}