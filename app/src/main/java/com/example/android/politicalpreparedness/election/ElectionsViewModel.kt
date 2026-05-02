package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import timber.log.Timber

class ElectionsViewModel(private val dataSource: ElectionDao): ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    val savedElections: LiveData<List<Election>> = dataSource.getElections()

    private val _navigateToVoterInfo = MutableLiveData<Election?>()
    val navigateToVoterInfo: LiveData<Election?>
        get() = _navigateToVoterInfo

    init {
        getUpcomingElections()
    }

    private fun getUpcomingElections() {
        viewModelScope.launch {
            try {
                val electionResponse = CivicsApi.retrofitService.getElections()
                _upcomingElections.value = electionResponse.elections
            } catch (e: Exception) {
                Timber.e(e, "Failed to get upcoming elections")
                _upcomingElections.value = emptyList()
            }
        }
    }

    fun onElectionClicked(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun onVoterInfoNavigated() {
        _navigateToVoterInfo.value = null
    }

}