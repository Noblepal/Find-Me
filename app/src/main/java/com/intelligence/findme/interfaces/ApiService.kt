package com.intelligence.findme.interfaces

import com.intelligence.findme.models.ProviderResponse
import com.intelligence.findme.models.ServiceResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("functions.php")
    fun getProvidersWithinRadius(
        @Field("token") token: String,
        @Field("lat") lat: String,
        @Field("lng") lng: String
    ): Call<ProviderResponse>

    @FormUrlEncoded
    @POST("functions.php")
    fun getAllServices(
        @Field("token") token: String
    ): Call<ServiceResponse>

}