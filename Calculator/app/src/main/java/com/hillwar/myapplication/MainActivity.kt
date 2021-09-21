package com.hillwar.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView

const val BUNDLE_KEY = "RESULT"

class MainActivity : AppCompatActivity() {



    var number: String = "0"

    var equals: String = "0"

    var operation: String = "+"

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        result = findViewById(R.id.result)
        result.setText(number)

    }

    fun clickOnButton(view: View) {
        val button: Button = view as Button
        if (number != "0") {
            number += button.text.toString()
        } else {
            number = button.text.toString()
        }
        result.setText(number)
    }

    fun clickOnClear(view: android.view.View) {
        number = "0"
        equals = "0"
        operation = "+"
        result.setText("0")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(BUNDLE_KEY, number)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        number = savedInstanceState.getString(BUNDLE_KEY, number)
        result.setText(number)
    }

    fun clickOnOperation(view: android.view.View) {
        when(operation) {
            "+" -> equals = (equals.toDouble() + number.toDouble()).toString()
            "-" -> equals = (equals.toDouble() - number.toDouble()).toString()
            "x" -> equals = (equals.toDouble() * number.toDouble()).toString()
            "/" -> equals = (equals.toDouble() / number.toDouble()).toString()
        }
        number = "0"
        if (equals.substring(equals.length - 2, equals.length) == ".0") equals = equals.substringBefore('.')
        result.setText(equals)
        val button: Button = view as Button
        operation = button.text.toString()
    }

    fun clickOnEquals(view: android.view.View) {
        when(operation) {
            "+" -> equals = (equals.toDouble() + number.toDouble()).toString()
            "-" -> equals = (equals.toDouble() - number.toDouble()).toString()
            "x" -> equals = (equals.toDouble() * number.toDouble()).toString()
            "/" -> equals = (equals.toDouble() / number.toDouble()).toString()
        }
        if (equals.substring(equals.length - 2, equals.length) == ".0") equals = equals.substringBefore('.')
        number = equals
        equals = "0"
        operation = "+"
        result.setText(number)
    }

    fun clickOnPoint(view: android.view.View) {
        if (!("." in number)) {
            number += "."
        }
        result.setText(number)
    }

    fun copyToClipboard(view: android.view.View) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", number)
        clipboard.setPrimaryClip(clip)
    }
}
