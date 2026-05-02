package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.GeocodioResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.geocod.io/v1.12/"

/**
 *  Documentation for the Geocodio API Service can be found at https://www.geocod.io/docs/#congressional-districts
 */

interface GeocodioApiService {
    @GET("geocode")
    suspend fun getRepresentatives(
        @Query("q") address: String, @Query("fields") fields: String = "cd"
    ): GeocodioResponse

    @GET("reverse")
    suspend fun reverseGeocode(
        @Query("q") latLong: String, @Query("fields") fields: String = "cd"
    ): GeocodioResponse
}

object GeocodioApi {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit =
        Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(CivicsHttpClient.getClient()).baseUrl(BASE_URL).build()

    val retrofitService: GeocodioApiService by lazy {
        retrofit.create(GeocodioApiService::class.java)
    }
}
