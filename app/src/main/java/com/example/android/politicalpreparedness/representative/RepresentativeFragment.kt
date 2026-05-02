package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationServices

class RepresentativeFragment : Fragment() {
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private val viewModel: RepresentativeViewModel by viewModels()
    private lateinit var binding: FragmentRepresentativeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = RepresentativeListAdapter()
        binding.representativeRecycler.adapter = adapter

        viewModel.representatives.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            val addressLine1 = binding.addressLine1.text.toString()
            val city = binding.city.text.toString()
            val zip = binding.zip.text.toString()

            if (addressLine1.isBlank()) {
                Toast.makeText(requireContext(), "Please enter address line 1", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val address = Address(
                addressLine1,
                binding.addressLine2.text.toString(),
                city,
                binding.state.selectedItem.toString(),
                zip
            )
            viewModel.setAddress(address)
            viewModel.fetchRepresentatives(address.toFormattedString())
        }

        binding.buttonLocation.setOnClickListener {
            hideKeyboard()
            if (checkLocationPermissions()) {
                getLocation()
            }
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                showSettingsDialog()
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    viewModel.getAddressFromLocation(location.latitude, location.longitude)
                }
            }
        } catch (_: SecurityException) {
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.location_permission_required)
            .setMessage(R.string.location_permission_description)
            .setPositiveButton(R.string.settings) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}
