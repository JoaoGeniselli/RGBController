package com.jgeniselli.rgbcontroller.color.control

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jgeniselli.rgbcontroller.R
import kotlinx.android.synthetic.main.rgb_controller_fragment.*
import kotlinx.android.synthetic.main.rgb_controller_fragment.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RGBControllerFragment : Fragment() {

    private val args: RGBControllerFragmentArgs by navArgs()
    private val viewModel by viewModel<RGBControllerViewModel> { parametersOf(args.deviceAddress) }

    private val red: Int get() = seek_bar_red.progress
    private val green: Int get() = seek_bar_green.progress
    private val blue: Int get() = seek_bar_blue.progress

    private val seekBarListener by lazy {
        object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(s: SeekBar?) = Unit
            override fun onStopTrackingTouch(s: SeekBar?) = Unit
            override fun onProgressChanged(s: SeekBar?, p: Int, u: Boolean) = updatePreview()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rgb_controller_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allSeekBars = listOf(seek_bar_red, seek_bar_green, seek_bar_blue)
        allSeekBars.forEach { it.setOnSeekBarChangeListener(seekBarListener) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_apply.setOnClickListener { viewModel.onApplyClicked(red, green, blue) }
        viewModel.onStart()
    }

    private fun updatePreview() {
        preview.previewColor = Color.rgb(red, green, blue)
    }

}
