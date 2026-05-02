package com.example.android.politicalpreparedness.network.models.geocodio

import com.squareup.moshi.Json

data class Contact(
    val phone: String?, @Json(name = "url") val website: String?
)
