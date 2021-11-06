package com.hillwar.animation

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onSaveInstanceState(): Parcelable {
        val stateView = CustomState(super.onSaveInstanceState())
        stateView.radiusOfFirst = radiusOfFirst
        stateView.radiusOfSecond = radiusOfSecond
        stateView.radiusOfThird = radiusOfThird
        stateView.radiusOfFourth = radiusOfFourth
        stateView.add = add
        stateView.radiusOfNormal = radiusOfNormal
        stateView.flag = flag
        stateView.state = state
        stateView.angle = angle
        return stateView
    }

    override fun onRestoreInstanceState(stateView: Parcelable?) {
        stateView as CustomState
        super.onRestoreInstanceState(stateView.superState)
        radiusOfFirst = stateView.radiusOfFirst
        radiusOfSecond = stateView.radiusOfSecond
        radiusOfThird = stateView.radiusOfThird
        radiusOfFourth = stateView.radiusOfFourth
        add = stateView.add
        radiusOfNormal = stateView.radiusOfNormal
        flag = stateView.flag
        state = stateView.state
        angle = stateView.angle
    }

    private class CustomState : BaseSavedState {
        var radiusOfNormal = 20F
        var radiusOfFirst = radiusOfNormal
        var radiusOfSecond = radiusOfNormal
        var radiusOfThird = radiusOfNormal
        var radiusOfFourth = radiusOfNormal
        var angle = 0F
        var flag = 0
        var add = 1F
        var state = 0

        constructor(superState: Parcelable?) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            radiusOfNormal = parcel.readFloat()
            radiusOfFirst = parcel.readFloat()
            radiusOfSecond = parcel.readFloat()
            radiusOfThird = parcel.readFloat()
            radiusOfFourth = parcel.readFloat()
            angle = parcel.readFloat()
            flag = parcel.readInt()
            add = parcel.readFloat()
            state = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(radiusOfNormal)
            out.writeFloat(radiusOfFirst)
            out.writeFloat(radiusOfSecond)
            out.writeFloat(radiusOfThird)
            out.writeFloat(radiusOfFourth)
            out.writeFloat(angle)
            out.writeInt(flag)
            out.writeFloat(add)
            out.writeInt(state)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<CustomState> {
                override fun createFromParcel(source: Parcel): CustomState = CustomState(source)
                override fun newArray(size: Int): Array<CustomState?> = arrayOfNulls(size)
            }
        }
    }

    private var radiusOfNormal = 20F
    private var radiusOfFirst = radiusOfNormal
    private var radiusOfSecond = radiusOfNormal
    private var radiusOfThird = radiusOfNormal
    private var radiusOfFourth = radiusOfNormal

    private var angle = 0F
    private var flag = 0
    private var add = 1F
    private var length = 100F

    private var state = 0
    private var center = Point()
    private val frame1 = RectF()
    private val frame2 = RectF()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF000000")
    }

    init {
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.CustomView, defStyleAttr, 0
        )
        try {
            state = a.getInt(R.styleable.CustomView_state, 0)
            length = a.getFloat(R.styleable.CustomView_length, 100F)
            angle = a.getFloat(R.styleable.CustomView_angle, 0F)
            radiusOfNormal = a.getFloat(R.styleable.CustomView_radiusOfNormal, 10F)
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = 400
        val desiredHeight = 200

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                widthSize
            }
            MeasureSpec.AT_MOST -> {
                desiredWidth.coerceAtMost(widthSize)
            }
            else -> {
                desiredWidth
            }
        }
        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                radiusOfNormal = (heightSize / 20).toFloat()
                length = (heightSize / 4).toFloat()

                heightSize
            }
            MeasureSpec.AT_MOST -> {
                radiusOfNormal = (desiredHeight.coerceAtMost(heightSize) / 20).toFloat()
                length = (desiredHeight.coerceAtMost(heightSize) / 4).toFloat()

                desiredHeight.coerceAtMost(heightSize)
            }
            else -> {
                radiusOfNormal = (desiredHeight / 20).toFloat()
                length = (desiredHeight / 4).toFloat()
                desiredHeight
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        center.x = width / 2
        center.y = height / 2
        frame1.set(
            (center.x / 2 - radiusOfNormal),
            (center.y + length), (center.x / 2 + radiusOfNormal), (center.y - length)
        )
        frame2.set(
            (center.x / 2 - length),
            (center.y + radiusOfNormal), (center.x / 2 + length), (center.y - radiusOfNormal)
        )
        drawCircles(canvas)
        drawCross(canvas)
        invalidate()
    }

    private fun drawCross(canvas: Canvas?) {
        val save = canvas?.save()
        if (state == 0) {
            if (angle <= 360) add *= 1.1F
            else add /= 1.09F
            angle += add
            canvas?.rotate(angle, center.x.toFloat() / 2, center.y.toFloat())
            if (angle > 720F) {
                angle = 15F
                add = 1F
                state = 1
            }
        }
        canvas?.drawRoundRect(frame1, 100F, 100F, paint)
        canvas?.drawRoundRect(frame2, 100F, 100F, paint)
        save?.let { canvas.restoreToCount(it) }
    }

    private fun drawCircles(canvas: Canvas?) {
        val save = canvas?.save()
        if (state == 1) {
            radiusOfFirst += 0.5F
            if (radiusOfFirst > 40F) flag = 1
            if (flag == 1) radiusOfFirst--
            if (radiusOfFirst == 20F && flag == 1) {
                flag = 0
                state = 2
            }
        } else if (state == 2) {
            radiusOfSecond += 0.5F
            if (radiusOfSecond > 40F) flag = 1
            if (flag == 1) radiusOfSecond--
            if (radiusOfSecond == 20F && flag == 1) {
                flag = 0
                state = 3
            }
        } else if (state == 3) {
            radiusOfThird += 0.5F
            if (radiusOfThird > 40F) flag = 1
            if (flag == 1) radiusOfThird--
            if (radiusOfThird == 20F && flag == 1) {
                flag = 0
                state = 4
            }
        } else if (state == 4) {
            radiusOfFourth += 0.5F
            if (radiusOfFourth > 40F) flag = 1
            if (flag == 1) radiusOfFourth--
            if (radiusOfFourth == 20F && flag == 1) {
                flag = 0
                state = 0
            }
        }
        canvas?.drawCircle(
            center.x.toFloat() * 3 / 2 - (length - radiusOfNormal),
            center.y.toFloat(),
            radiusOfFirst,
            paint
        )
        canvas?.drawCircle(
            (center.x.toFloat() * 3 / 2),
            center.y.toFloat() - (length - radiusOfNormal),
            radiusOfSecond,
            paint
        )
        canvas?.drawCircle(
            center.x.toFloat() * 3 / 2 + (length - radiusOfNormal),
            center.y.toFloat(),
            radiusOfThird,
            paint
        )
        canvas?.drawCircle(
            (center.x.toFloat() * 3 / 2),
            center.y.toFloat() + (length - radiusOfNormal),
            radiusOfFourth,
            paint
        )
        save?.let { canvas.restoreToCount(it) }
    }
}