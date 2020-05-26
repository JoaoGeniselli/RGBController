package com.jgeniselli.rgbcontroller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.paired_devices_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class PairedDevicesFragment : Fragment() {

    private val viewModel by viewModel<PairedDevicesViewModel>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.paired_devices_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListView()
        setupViewModel()
    }

    private fun setupListView() {
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        list_view.adapter = adapter
        list_view.setOnItemClickListener { _, _, position, _ ->
            viewModel.onDeviceClicked(position)
        }
    }

    private fun setupViewModel() {
        swipe_to_refresh.setOnRefreshListener { viewModel.onRefresh() }
        viewModel.progressIsVisible.observe(viewLifecycleOwner, Observer { isVisible ->
            progress.visibility = if (isVisible) View.VISIBLE else View.GONE
        })
        viewModel.devices.observe(viewLifecycleOwner, Observer { devices ->
            swipe_to_refresh.isRefreshing = false
            adapter.clear()
            adapter.addAll(devices)
        })
        viewModel.redirectToControl.observe(viewLifecycleOwner, EventObserver { address ->
            redirectToRGBControl(address)
        })
        viewModel.onStart()
    }

    private fun redirectToRGBControl(deviceAddress: String) {
        val action = PairedDevicesFragmentDirections
            .actionDevicesToControl(deviceAddress)
        findNavController().navigate(action)
    }
}
