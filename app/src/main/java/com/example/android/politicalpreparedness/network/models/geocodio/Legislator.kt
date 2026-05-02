package com.example.android.politicalpreparedness.network.models.geocodio

import com.squareup.moshi.Json

data class Legislator(
    @Json(name = "references") val references: References,
    val bio: Bio?,
    val contact: Contact?,
    val social: Social?,
) {
    val id: String get() = references.bioGuideId
}
