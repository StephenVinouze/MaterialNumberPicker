package com.github.stephenvinouze.materialnumberpickersample

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker

/**
 * Created by stephenvinouze on 25/09/2017.
 */
class MainActivity : AppCompatActivity() {

    private val defaultButton: Button by lazy { findViewById<Button>(R.id.default_number_picker_button) }
    private val simpleButton: Button by lazy { findViewById<Button>(R.id.simple_number_picker_button) }
    private val customButton: Button by lazy { findViewById<Button>(R.id.custom_number_picker_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        defaultButton.setOnClickListener {
            val numberPicker = NumberPicker(this)
            numberPicker.minValue = 1
            numberPicker.maxValue = 10
            presentPickerInAlert(numberPicker, getString(R.string.alert_default_title))
        }
        simpleButton.setOnClickListener {
            val numberPicker = MaterialNumberPicker(this)
            presentPickerInAlert(numberPicker, getString(R.string.alert_simple_title))
        }
        customButton.setOnClickListener {
            val numberPicker = MaterialNumberPicker(
                context = this,
                minValue = 1,
                maxValue = 50,
                value = 10,
                separatorColor = ContextCompat.getColor(this, R.color.colorAccent),
                textColor = ContextCompat.getColor(this, R.color.colorPrimary),
                textSize = resources.getDimensionPixelSize(R.dimen.numberpicker_textsize),
                textStyle = Typeface.BOLD_ITALIC,
                editable = false,
                wrapped = false,
                fontName = "Hand.ttf",
                formatter = NumberPicker.Formatter { value ->
                    return@Formatter "Value $value"
                }
            )
            presentPickerInAlert(numberPicker, getString(R.string.alert_custom_title))
        }
    }

    private fun presentPickerInAlert(numberPicker: NumberPicker, title: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(numberPicker)
            .setNegativeButton(getString(android.R.string.cancel), null)
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                Toast.makeText(this, getString(R.string.picker_value, numberPicker.value), Toast.LENGTH_LONG).show()
            }
            .show()
    }
}