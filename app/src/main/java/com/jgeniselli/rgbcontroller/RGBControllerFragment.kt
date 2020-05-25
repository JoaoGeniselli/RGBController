package com.jgeniselli.rgbcontroller

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RGBControllerFragment : Fragment() {

    companion object {
        fun newInstance() = RGBControllerFragment()
    }

    private lateinit var viewModel: RGBControllerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rgb_controller_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RGBControllerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
