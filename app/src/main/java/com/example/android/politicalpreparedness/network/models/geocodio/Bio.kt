package com.example.android.politicalpreparedness.network.models.geocodio

import com.squareup.moshi.Json

data class Bio(
    @Json(name = "first_name") val firstName: String?,
    @Json(name = "last_name") val lastName: String?,
    @Json(name = "photo_url") val photoUrl: String?,
    val party: String?
)