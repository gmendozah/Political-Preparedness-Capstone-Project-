package com.example.android.politicalpreparedness.network.models.geocodio

import com.squareup.moshi.Json

data class District(
    val name: String, @Json(name = "current_legislators") val legislators: List<Legislator>?
)