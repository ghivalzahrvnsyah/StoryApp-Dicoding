package com.ghivalhrvnsyah.storyappdicoding.view.customView

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.ghivalhrvnsyah.storyappdicoding.R

class MyEditText : AppCompatEditText {



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
        hint = context.getString(R.string.password_hint) // Menggunakan string resource untuk hint
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Tidak perlu melakukan apa pun sebelum teks berubah.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 8) {
                    setError(context.getString(R.string.errorMsgPassword), null) // Menggunakan string resource untuk pesan kesalahan
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Tidak perlu melakukan apa pun setelah teks berubah.
            }
        })
    }
}