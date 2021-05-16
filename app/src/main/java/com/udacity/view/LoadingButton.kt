package com.udacity.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.withStyledAttributes
import com.udacity.R
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val loadingRect = Rect()
    private val ovalRect = RectF()
    private var lbTextSize = 40.0f
    private var lbText = "Download"
    private var lbBackgroundColor: Int = 0
    private var lbTextColor: Int = 0
    private var widthText = 0f
    private var baseLineContent = 0f
    private var lbProgress = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = lbTextSize
    }

    private var valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                lbText = "We are loading"
                valueAnimator = ValueAnimator.ofInt(0, 360)
                    .apply {
                        addUpdateListener {
                            lbProgress = it.animatedValue as Int
                            invalidate()
                        }

                        interpolator = AccelerateInterpolator()
                        duration = 1000
                        repeatCount = ValueAnimator.INFINITE
                        repeatMode = ValueAnimator.RESTART
                        start()
                    }
            }
            ButtonState.Completed -> {
                valueAnimator.cancel()

                lbProgress = 0
                lbText = "Download"
            }
            ButtonState.Clicked -> {

            }
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
        baseLineContent = (height + lbTextSize) / 2 - 4

        // Set initial background color
        setBackgroundColor(lbBackgroundColor)

        if (buttonState == ButtonState.Loading) {
            // Draw Progress rectangle
            paint.color = context.getColor(R.color.colorPrimaryDark)
            loadingRect.set(0, 0, width * lbProgress / 360, height)
            canvas.drawRect(loadingRect, paint)

            // Draw circle
            paint.color = Color.YELLOW
            widthText = paint.measureText(lbText)
            ovalRect.set(
                (width + widthText) / 2 , (height - lbTextSize) / 2 ,
                (width + widthText) / 2 + lbTextSize, (height + lbTextSize) / 2)
            canvas.drawArc(ovalRect, 0f, lbProgress.toFloat(), true, paint)
        }

        // Draw text
        paint.color = lbTextColor
        canvas.drawText(lbText, (width / 2).toFloat(), baseLineContent, paint)

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