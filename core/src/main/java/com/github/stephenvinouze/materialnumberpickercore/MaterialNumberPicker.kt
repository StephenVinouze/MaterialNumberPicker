package com.github.stephenvinouze.materialnumberpickercore

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker

/**
 * Created by stephenvinouze on 25/09/2017.
 */
class MaterialNumberPicker : NumberPicker {

    companion object {
        private const val DEFAULT_VALUE = 1
        private const val MAX_VALUE = 10
        private const val DEFAULT_SEPARATOR_COLOR = Color.TRANSPARENT
        private const val DEFAULT_TEXT_COLOR = Color.BLACK
        private const val DEFAULT_TEXT_SIZE = 40
        private const val DEFAULT_TEXT_STYLE = Typeface.NORMAL
        private const val DEFAULT_EDITABLE = false
        private const val DEFAULT_WRAPPED = false
    }

    var separatorColor: Int = Color.TRANSPARENT
        set(value) {
            field = value
            divider?.colorFilter = PorterDuffColorFilter(separatorColor, PorterDuff.Mode.SRC_IN)
        }

    var textColor: Int = DEFAULT_TEXT_COLOR
        set(value) {
            field = value
            updateTextAttributes()
        }

    var textStyle: Int = DEFAULT_TEXT_STYLE
        set(value) {
            field = value
            updateTextAttributes()
        }

    var textSize: Int = DEFAULT_TEXT_SIZE
        set(value) {
            field = value
            updateTextAttributes()
        }

    var editable: Boolean = DEFAULT_EDITABLE
        set(value) {
            field = value
            descendantFocusability = if (value) ViewGroup.FOCUS_AFTER_DESCENDANTS else ViewGroup.FOCUS_BLOCK_DESCENDANTS
        }

    var fontName: String? = null
        set(value) {
            field = value
            updateTextAttributes()
        }

    private val inputEditText: EditText? by lazy {
        try {
            val f = NumberPicker::class.java.getDeclaredField("mInputText")
            f.isAccessible = true
            f.get(this) as EditText
        } catch (e: Exception) {
            null
        }
    }

    private val wheelPaint: Paint? by lazy {
        try {
            val selectorWheelPaintField = NumberPicker::class.java.getDeclaredField("mSelectorWheelPaint")
            selectorWheelPaintField.isAccessible = true
            selectorWheelPaintField.get(this) as Paint
        } catch (e: Exception) {
            null
        }
    }

    private val divider: Drawable? by lazy {
        val dividerField = NumberPicker::class.java.declaredFields.firstOrNull { it.name == "mSelectionDivider" }
        dividerField?.let {
            try {
                it.isAccessible = true
                it.get(this) as Drawable
            } catch (e: Exception) {
                null
            }
        }
    }

    @JvmOverloads
    constructor(context: Context,
                minValue: Int = DEFAULT_VALUE,
                maxValue: Int = MAX_VALUE,
                value: Int = DEFAULT_VALUE,
                separatorColor: Int = DEFAULT_SEPARATOR_COLOR,
                textColor: Int = DEFAULT_TEXT_COLOR,
                textSize: Int = DEFAULT_TEXT_SIZE,
                textStyle: Int = DEFAULT_TEXT_STYLE,
                editable: Boolean = DEFAULT_EDITABLE,
                wrapped: Boolean = DEFAULT_WRAPPED,
                fontName: String? = null,
                formatter: Formatter? = null
    ) : super(context) {
        this.minValue = minValue
        this.maxValue = maxValue
        this.value = value
        this.separatorColor = separatorColor
        this.textColor = textColor
        this.textSize = textSize
        this.textStyle = textStyle
        this.fontName = fontName
        this.editable = editable
        this.wrapSelectorWheel = wrapped
        setFormatter(formatter)

        disableFocusability()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MaterialNumberPicker, 0, 0)

        minValue = a.getInteger(R.styleable.MaterialNumberPicker_mnpMinValue, DEFAULT_VALUE)
        maxValue = a.getInteger(R.styleable.MaterialNumberPicker_mnpMaxValue, MAX_VALUE)
        value = a.getInteger(R.styleable.MaterialNumberPicker_mnpValue, DEFAULT_VALUE)
        separatorColor = a.getColor(R.styleable.MaterialNumberPicker_mnpSeparatorColor, DEFAULT_SEPARATOR_COLOR)
        textColor = a.getColor(R.styleable.MaterialNumberPicker_mnpTextColor, DEFAULT_TEXT_COLOR)
        textSize = a.getDimensionPixelSize(R.styleable.MaterialNumberPicker_mnpTextSize, DEFAULT_TEXT_SIZE)
        textStyle = a.getInt(R.styleable.MaterialNumberPicker_mnpTextColor, DEFAULT_TEXT_STYLE)
        editable = a.getBoolean(R.styleable.MaterialNumberPicker_mnpEditable, DEFAULT_EDITABLE)
        wrapSelectorWheel = a.getBoolean(R.styleable.MaterialNumberPicker_mnpWrapped, DEFAULT_WRAPPED)
        fontName = a.getString(R.styleable.MaterialNumberPicker_mnpFontname)

        a.recycle()

        disableFocusability()
    }

    /**
     * Disable focusability of edit text embedded inside the number picker
     * We also override the edit text filter private attribute by using reflection as the formatter is still buggy while attempting to display the default value
     * This is still an open Google @see <a href="https://code.google.com/p/android/issues/detail?id=35482#c9">issue</a> from 2012
     */
    private fun disableFocusability() {
        inputEditText?.filters = arrayOfNulls(0)
    }

    /**
     * Uses reflection to access text size private attribute for both wheel and edit text inside the number picker.
     */
    private fun updateTextAttributes() {
        val typeface = if (fontName != null) Typeface.createFromAsset(context.assets, "fonts/$fontName") else Typeface.create(Typeface.DEFAULT, textStyle)
        wheelPaint?.let {
            it.color = textColor
            it.textSize = textSize.toFloat()
            it.typeface = typeface

            val childEditText = (0 until childCount).map { getChildAt(it) as? EditText }.firstOrNull()
            childEditText?.let {
                it.setTextColor(textColor)
                it.setTextSize(TypedValue.COMPLEX_UNIT_SP, pixelsToSp(context, textSize.toFloat()))
                it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
                it.typeface = typeface

                invalidate()
            }
        }
    }

    private fun pixelsToSp(context: Context, px: Float): Float =
            px / context.resources.displayMetrics.scaledDensity

}