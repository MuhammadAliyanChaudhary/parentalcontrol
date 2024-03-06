package com.example.parentalcontrol.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.parentalcontrol.R

class PinOverlayView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    private lateinit var pinEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var incorrectPinText: TextView
    private lateinit var parentLayout: ViewGroup // Parent layout of PinOverlayView

    init {
        LayoutInflater.from(context).inflate(R.layout.overlay_pin_screen, this, true)
        pinEditText = findViewById(R.id.pinEditText)
        submitButton = findViewById(R.id.submitButton)
        incorrectPinText = findViewById(R.id.incorrectPinTxt)

        // Find the parent layout of PinOverlayView
        parentLayout = parent as ViewGroup


        submitButton.setOnClickListener {
            val enteredPin = pinEditText.text.toString()
            if (enteredPin == "1234") {
                incorrectPinText.visibility = View.GONE
                hideOverlay()
            } else {
                incorrectPinText.visibility = View.VISIBLE
                Toast.makeText(context, "Incorrect PIN, please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideOverlay() {
        parentLayout.visibility = View.GONE
    }
}
