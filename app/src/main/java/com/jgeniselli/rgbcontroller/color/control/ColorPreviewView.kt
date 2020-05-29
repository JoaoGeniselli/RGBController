package com.jgeniselli.rgbcontroller.color.control

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jgeniselli.rgbcontroller.R
import kotlinx.android.synthetic.main.view_color_preview.view.*

class ColorPreviewView : FrameLayout {

    var previewColor: Int? = null
        set(value) {
            field = value
            invalidatePreview()
        }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context)
            .inflate(R.layout.view_color_preview, this, true)
        invalidatePreview()
    }

    private fun invalidatePreview() {
        container_color_preview.setBackgroundColor(previewColor ?: Color.TRANSPARENT)
    }
}
