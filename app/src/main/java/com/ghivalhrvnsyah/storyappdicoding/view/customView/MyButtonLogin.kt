package com.ghivalhrvnsyah.storyappdicoding.view.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.ghivalhrvnsyah.storyappdicoding.R

class MyButtonLogin : AppCompatButton {

    private lateinit var enableBackground: Drawable
    private lateinit var disableBackground: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setBackgroundDrawable(if (isEnabled) enableBackground else disableBackground)
        setTextColor(txtColor)
        textSize = resources.getDimension(R.dimen.button_text_size) // Menggantikan angka tetap dengan dimensi
        gravity = Gravity.CENTER
        text = context.getString(R.string.loginBtnMsg) // Menggunakan string resource
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enableBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disableBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }
}