package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {
    private val args: VoterInfoFragmentArgs by navArgs()
    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val database = ElectionDatabase.getInstance(requireContext())
        val viewModelFactory = VoterInfoViewModelFactory(database.electionDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel

        val election = args.argElection
        viewModel.fetchVoterInfo(election)

        viewModel.urlToOpen.observe(viewLifecycleOwner) { url ->
            url?.let {
                loadUrl(it)
                viewModel.onUrlOpened()
            }
        }

        return binding.root
    }

    private fun loadUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startContextIntent(intent)
    }

    private fun startContextIntent(intent: Intent) {
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }
}