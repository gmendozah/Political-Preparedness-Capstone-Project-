package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment : Fragment() {

    private lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View {
        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val database = ElectionDatabase.getInstance(requireContext())
        val viewModelFactory = ElectionsViewModelFactory(database.electionDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)

        binding.viewModel = viewModel

        val upcomingAdapter = ElectionListAdapter(ElectionListener {
            viewModel.onElectionClicked(it)
        })
        binding.upcomingElectionsRecycler.adapter = upcomingAdapter

        val savedAdapter = ElectionListAdapter(ElectionListener {
            viewModel.onElectionClicked(it)
        })
        binding.savedElectionsRecycler.adapter = savedAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner) {
            upcomingAdapter.submitList(it)
        }

        viewModel.savedElections.observe(viewLifecycleOwner) {
            savedAdapter.submitList(it)
        }

        viewModel.navigateToVoterInfo.observe(viewLifecycleOwner) { election ->
            election?.let {
                this.findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it)
                )
                viewModel.onVoterInfoNavigated()
            }
        }

        return binding.root
    }
}