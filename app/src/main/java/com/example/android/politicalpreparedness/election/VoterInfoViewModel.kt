package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class VoterInfoViewModel(private val dataSource: ElectionDao) : ViewModel() {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean>
        get() = _isSaved

    private val _urlToOpen = MutableLiveData<String?>()
    val urlToOpen: LiveData<String?>
        get() = _urlToOpen

    fun fetchVoterInfo(election: Election) {
        viewModelScope.launch {
            try {
                // Using election.name and division.state as address for now if address is not available
                val address = "${election.name} ${election.division.state}";
                _voterInfo.value = CivicsApi.retrofitService.getVoterInfo(address, election.id)
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch voter info")
            }
        }
        checkIfSaved(election.id)
    }

    private fun checkIfSaved(id: Int) {
        viewModelScope.launch {
            val election = dataSource.getElectionById(id)
            _isSaved.value = election != null
        }
    }

    fun toggleFollowElection(election: Election) {
        viewModelScope.launch {
            if (_isSaved.value == true) {
                dataSource.deleteElectionById(election.id)
                _isSaved.value = false
            } else {
                dataSource.insertElection(election)
                _isSaved.value = true
            }
        }
    }

    fun onUrlClicked(url: String?) {
        _urlToOpen.value = url
    }

    fun onUrlOpened() {
        _urlToOpen.value = null
    }

}
