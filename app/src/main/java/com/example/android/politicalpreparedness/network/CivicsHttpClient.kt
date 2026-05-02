package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.BuildConfig
import okhttp3.OkHttpClient

class CivicsHttpClient : OkHttpClient() {
    companion object {
        fun getClient(): OkHttpClient {
            return Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original.url()

                    val newUrl = when {
                        url.host().contains("googleapis.com") -> {
                            url.newBuilder().addQueryParameter("key", BuildConfig.GOOGLE_API_KEY)
                                .build()
                        }

                        url.host().contains("geocod.io") -> {
                            url.newBuilder()
                                .addQueryParameter("api_key", BuildConfig.GEOCODIO_API_KEY).build()
                        }

                        else -> url
                    }

                    val request = original.newBuilder().url(newUrl).build()
                    chain.proceed(request)
                }
                .build()
        }
    }
}
