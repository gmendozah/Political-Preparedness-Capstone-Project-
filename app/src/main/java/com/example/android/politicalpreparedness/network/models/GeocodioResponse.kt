package com.example.android.politicalpreparedness.network.models

import com.example.android.politicalpreparedness.network.models.geocodio.GeocodioResult

data class GeocodioResponse(
    val results: List<GeocodioResult>
)
