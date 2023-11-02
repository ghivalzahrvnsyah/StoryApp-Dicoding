package com.ghivalhrvnsyah.storyappdicoding.view.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.ghivalhrvnsyah.storyappdicoding.R

class MyEditTextEmail : AppCompatEditText, View.OnTouchListener {

    private lateinit var clearButtonImage: Drawable

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
        hint = context.getString(R.string.email_hint) // Menggunakan string resource untuk hint
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close_48) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Tidak perlu melakukan apa pun sebelum teks berubah.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    showClearButton()

                    val email = s.toString()
                    if (!isValidEmail(email)) {
                        setError(
                            context.getString(R.string.errorMsgEmail),
                            null
                        ) // Menggunakan string resource untuk pesan kesalahan
                    } else {
                        error = null
                    }
                } else {
                    hideClearButton()
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Tidak perlu melakukan apa pun setelah teks berubah.
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage =
                            ContextCompat.getDrawable(context, R.drawable.ic_close_48) as Drawable
                        showClearButton()
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        clearButtonImage =
                            ContextCompat.getDrawable(context, R.drawable.ic_close_48) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }

                    else -> return false
                }
            } else return false
        }
        return false
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}