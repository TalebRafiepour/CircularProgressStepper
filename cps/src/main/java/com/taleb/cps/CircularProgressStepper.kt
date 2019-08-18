package com.taleb.cps

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toRectF
import kotlin.math.min


class CircularProgressStepper : View {

    private val stepRadius = 40
    private val progressColor = Color.BLUE
    private val backgroundColor = Color.GRAY
    private var currentProgress = 0f
    private var endProgress = 360f
    private var numOfStep = 20


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null)
            return

        val circleRadius = (min(width,height)/2.toFloat())-stepRadius/2
        val circleWidth = min(width,height).toFloat()
        val circleStrokeWidth = (stepRadius/3).toFloat()
        val progressStrokeWidth = (stepRadius/8).toFloat()

        //draw background layer
        val circlePaint = Paint()
        circlePaint.style = Paint.Style.STROKE
        circlePaint.color = backgroundColor
        circlePaint.isAntiAlias = true
        circlePaint.strokeWidth = circleStrokeWidth

        canvas.drawCircle((width/2).toFloat(), (height/2).toFloat(),circleRadius,circlePaint)

        //draw progress layer
        val progressPaint = Paint()
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeCap = Paint.Cap.ROUND
        progressPaint.color = progressColor
        progressPaint.isAntiAlias = true
        progressPaint.strokeWidth = progressStrokeWidth

        val rectLeft = (width-circleWidth)/2
        val rectTop = (height-circleWidth)/2
        val rectProgress = RectF(rectLeft,rectTop,rectLeft+circleWidth,rectTop+circleWidth)
        rectProgress.inset((stepRadius/2).toFloat(),(stepRadius/2).toFloat())
        canvas.drawArc(rectProgress,0f,currentProgress,false,progressPaint)
        //draw step circles

        val stepPaint = Paint()
        stepPaint.style = Paint.Style.FILL
        stepPaint.color = backgroundColor
        stepPaint.isAntiAlias = true

        val stepDegree = 360/numOfStep
        for (i in 0..numOfStep){
            val currentDegree:Float = (stepDegree*i).toFloat()
            val theta = (currentDegree*Math.PI)/180
            val centerX = width/2-stepRadius/2
            val centerY = height/2-stepRadius/2
            val left = (circleRadius*Math.cos(theta)+centerX).toFloat()
            val top = (circleRadius*Math.sin(theta)+centerY).toFloat()
            if (currentDegree <= currentProgress){
                stepPaint.color = progressColor
            }else {
                stepPaint.color = backgroundColor
            }
            val rectF = RectF(left,top,left+stepRadius,top+stepRadius)

            canvas.drawOval(rectF,stepPaint)
        }

        if (currentProgress < endProgress){
            currentProgress += 2.0f
            postInvalidate()
        }
    }

}