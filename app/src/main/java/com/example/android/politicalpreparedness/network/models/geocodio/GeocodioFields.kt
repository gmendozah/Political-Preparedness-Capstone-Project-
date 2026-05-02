package com.example.android.politicalpreparedness.network.models.geocodio

import com.squareup.moshi.Json

data class GeocodioFields(
    @Json(name = "congressional_districts") val congressionalDistricts: List<District>?
)