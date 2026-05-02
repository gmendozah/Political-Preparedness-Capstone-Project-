package com.example.android.politicalpreparedness.network.models.geocodio

import com.squareup.moshi.Json

data class GeocodioResult(
    @Json(name = "address_components") val addressComponents: GeocodioAddress?,
    @Json(name = "formatted_address") val formattedAddress: String?,
    val fields: GeocodioFields?
)