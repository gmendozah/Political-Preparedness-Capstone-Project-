package com.example.android.politicalpreparedness.network.models.geocodio

import com.squareup.moshi.Json

data class References(
    @Json(name = "bioguide_id") val bioGuideId: String
)