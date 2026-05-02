package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.GeocodioApi
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel : ViewModel() {
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address> get() = _address

    fun fetchRepresentatives(address: String) {
        viewModelScope.launch {
            try {
                val response = GeocodioApi.retrofitService.getRepresentatives(address)
                _representatives.value = parseRepresentatives(response)
            } catch (_: Exception) {
                _representatives.value = emptyList()
            }
        }
    }

    fun getAddressFromLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = GeocodioApi.retrofitService.reverseGeocode("$lat,$lon")
                if (response.results.isNotEmpty()) {
                    val components = response.results[0].addressComponents
                    val newAddress = Address(
                        line1 = "${components?.number ?: ""} ${components?.street ?: ""}".trim(),
                        city = components?.city ?: "",
                        state = components?.state ?: "",
                        zip = components?.zip ?: ""
                    )
                    _address.value = newAddress
                    _representatives.value = parseRepresentatives(response)
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun parseRepresentatives(response: GeocodioResponse): List<Representative> {
        val representativesMap = mutableMapOf<String, Representative>()

        response.results.forEach { result ->
            result.fields?.congressionalDistricts?.forEach { district ->
                val office = Office(district.name, Division("dummy", "us", ""), emptyList())
                district.legislators?.forEach { legislator ->
                    val legislatorId = legislator.id
                    if (!representativesMap.containsKey(legislatorId)) {
                        val official = Official(
                            name = "${legislator.bio?.firstName} ${legislator.bio?.lastName}",
                            party = legislator.bio?.party,
                            phones = legislator.contact?.phone?.let { listOf(it) },
                            urls = legislator.contact?.website?.let { listOf(it) },
                            photoUrl = legislator.bio?.photoUrl,
                            channels = mutableListOf<Channel>().apply {
                                legislator.social?.facebook?.let { add(Channel("Facebook", it)) }
                                legislator.social?.twitter?.let { add(Channel("Twitter", it)) }
                            })
                        representativesMap[legislatorId] = Representative(official, office)
                    }
                }
            }
        }
        return representativesMap.values.toList()
    }

    fun setAddress(address: Address) {
        _address.value = address
    }
}
