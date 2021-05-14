package com.udacity.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.udacity.R
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var lbTextSize = 40.0f
    private var lbText = "Download"
    private var lbBackgroundColor: Int = 0
    private var lbTextColor: Int = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = lbTextSize
    }

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                lbText = "We are loading"
            }
            ButtonState.Completed -> {
                lbText = "Download"
            }
            ButtonState.Clicked -> {}
        }

        invalidate()
    }


    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            lbBackgroundColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            lbTextColor = getColor(R.styleable.LoadingButton_textColor, 0)
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        setBackgroundColor(lbBackgroundColor)

        // Draw text
        paint.color = lbTextColor
        canvas.drawText(lbText, (width / 2).toFloat(), (height + lbTextSize) / 2 - 4, paint)

        // Draw circle
        if (buttonState == ButtonState.Loading) {
            paint.color = Color.YELLOW
        }

    }

    fun setLoadingButtonState(loadingButtonState: ButtonState) {
        buttonState = loadingButtonState
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
//        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
//        val h: Int = resolveSizeAndState(
//            MeasureSpec.getSize(w),
//            heightMeasureSpec,
//            0
//        )
//        widthSize = w
//        heightSize = h
//        setMeasuredDimension(w, h)
//    }

}