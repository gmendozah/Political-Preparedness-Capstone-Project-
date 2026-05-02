package com.example.android.politicalpreparedness.representative.adapter

import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.politicalpreparedness.R

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        Glide.with(view.context).load(uri).apply(
            RequestOptions().placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile)
                .circleCrop()
        ).into(view)
    } ?: view.setImageResource(R.drawable.ic_profile)
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = this.adapter as? ArrayAdapter<String> ?: return
    for (i in 0 until adapter.count) {
        if (adapter.getItem(i) == value) {
            setSelection(i)
            return
        }
    }
    if (value != null && value.length == 2) {
        val position = findPositionByAbbreviation(adapter, value)
        if (position >= 0) {
            setSelection(position)
        }
    }
}

private fun findPositionByAbbreviation(adapter: ArrayAdapter<String>, abbreviation: String): Int {
    val stateMap = mapOf(
        "AL" to "Alabama",
        "AK" to "Alaska",
        "AZ" to "Arizona",
        "AR" to "Arkansas",
        "CA" to "California",
        "CO" to "Colorado",
        "CT" to "Connecticut",
        "DE" to "Delaware",
        "FL" to "Florida",
        "GA" to "Georgia",
        "HI" to "Hawaii",
        "ID" to "Idaho",
        "IL" to "Illinois",
        "IN" to "Indiana",
        "IA" to "Iowa",
        "KS" to "Kansas",
        "KY" to "Kentucky",
        "LA" to "Louisiana",
        "ME" to "Maine",
        "MD" to "Maryland",
        "MA" to "Massachusetts",
        "MI" to "Michigan",
        "MN" to "Minnesota",
        "MS" to "Mississippi",
        "MO" to "Missouri",
        "MT" to "Montana",
        "NE" to "Nebraska",
        "NV" to "Nevada",
        "NH" to "New Hampshire",
        "NJ" to "New Jersey",
        "NM" to "New Mexico",
        "NY" to "New York",
        "NC" to "North Carolina",
        "ND" to "North Dakota",
        "OH" to "Ohio",
        "OK" to "Oklahoma",
        "OR" to "Oregon",
        "PA" to "Pennsylvania",
        "RI" to "Rhode Island",
        "SC" to "South Carolina",
        "SD" to "South Dakota",
        "TN" to "Tennessee",
        "TX" to "Texas",
        "UT" to "Utah",
        "VT" to "Vermont",
        "VA" to "Virginia",
        "WA" to "Washington",
        "WV" to "West Virginia",
        "WI" to "Wisconsin",
        "WY" to "Wyoming"
    )
    val fullName = stateMap[abbreviation.uppercase()]
    return if (fullName != null) adapter.getPosition(fullName) else -1
}
