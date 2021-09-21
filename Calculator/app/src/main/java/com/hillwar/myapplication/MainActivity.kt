package com.hillwar.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView

const val NUMBER_KEY = "NUMBER"

const val EQUALS_KEY = "EQUALS"

const val OPERATION_KEY = "OPERATION"

const val TAP_KEY = "TAP"

class MainActivity : AppCompatActivity() {

    var number: String = "0"

    var equals: String = "0"

    var operation: String = "+"

    var tapOperation: Boolean = false

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        result = findViewById(R.id.result)
        result.setText(number)

    }

    fun clickOnButton(view: View) {
        val button: Button = view as Button
        if (tapOperation) {
            number = "0"
            tapOperation = false
        }
        if (number != "0" && number != "Infinity" && number != "NaN") {
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
        tapOperation = false
        result.setText("0")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(NUMBER_KEY, number)
        outState.putString(EQUALS_KEY, equals)
        outState.putString(OPERATION_KEY, operation)
        outState.putBoolean(TAP_KEY, tapOperation)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        number = savedInstanceState.getString(NUMBER_KEY, number)
        equals = savedInstanceState.getString(EQUALS_KEY, equals)
        operation = savedInstanceState.getString(OPERATION_KEY, operation)
        tapOperation = savedInstanceState.getBoolean(TAP_KEY, tapOperation)
        result.setText(number)
    }

    fun clickOnOperation(view: android.view.View) {
        when(operation) {
            "+" -> equals = (equals.toDouble() + number.toDouble()).toString()
            "-" -> equals = (equals.toDouble() - number.toDouble()).toString()
            "x" -> equals = (equals.toDouble() * number.toDouble()).toString()
            "/" -> equals = (equals.toDouble() / number.toDouble()).toString()
        }
        tapOperation = true
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
