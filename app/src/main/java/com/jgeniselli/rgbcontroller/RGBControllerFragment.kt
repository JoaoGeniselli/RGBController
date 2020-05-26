package com.jgeniselli.rgbcontroller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.rgb_controller_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RGBControllerFragment : Fragment() {

    private val args: RGBControllerFragmentArgs by navArgs()
    private val viewModel by viewModel<RGBControllerViewModel> { parametersOf(args.deviceAddress) }

    private val red: Int get() = seek_bar_red.progress
    private val green: Int get() = seek_bar_green.progress
    private val blue: Int get() = seek_bar_blue.progress

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rgb_controller_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_apply.setOnClickListener { viewModel.onApplyClicked(red, green, blue) }
        viewModel.onStart()
    }

}
