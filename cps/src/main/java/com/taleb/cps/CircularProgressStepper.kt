package com.taleb.cps

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toRect
import kotlin.math.max
import kotlin.math.min


class CircularProgressStepper : View {

    private var stepsSize = (18*resources.displayMetrics.density).toInt()
    private var progressColor = Color.parseColor("#30c39e")
    private var foregroundColor = Color.parseColor("#caccd1")
    private var currentProgress = 0f
    private var endProgress = 360f
    private var numOfSteps = 6
    private var stepsImgSrc = emptyArray<Int>()
    private var currentStep = 1
    private var progressStrokeOffset = (2*resources.displayMetrics.density).toInt()
    //
    private val circlePaint = Paint()
    private val progressPaint = Paint()
    private val stepPaint = Paint()
    private val stepImgPaint = Paint()


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressStepper)
            try {
                numOfSteps = typedArray.getInteger(R.styleable.CircularProgressStepper_cps_numOfSteps, numOfSteps)
                currentStep = typedArray.getInteger(R.styleable.CircularProgressStepper_cps_currentStep, currentStep)
                stepsSize= typedArray.getDimensionPixelSize(R.styleable.CircularProgressStepper_cps_stepsSize,stepsSize)
                foregroundColor = typedArray.getColor(R.styleable.CircularProgressStepper_cps_foregroundColor,foregroundColor)
                progressColor = typedArray.getColor(R.styleable.CircularProgressStepper_cps_progressColor,progressColor)
                progressStrokeOffset = typedArray.getDimensionPixelOffset(R.styleable.CircularProgressStepper_cps_progressStrokeOffset,progressStrokeOffset)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                typedArray.recycle()
                setCurrentStep(currentStep)
            }
        }

        paintsInit()

        //todo(add infinite progress)
        //todo(add custom image to steps)
        //todo(when numOfStep is odd steps not draw correctly -> bug)
    }

    private fun paintsInit() {
        circlePaint.style = Paint.Style.STROKE
        circlePaint.color = foregroundColor
        circlePaint.isAntiAlias = true
        //
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeCap = Paint.Cap.ROUND
        progressPaint.color = progressColor
        progressPaint.isAntiAlias = true
        //
        stepPaint.style = Paint.Style.FILL
        stepPaint.color = foregroundColor
        stepPaint.isAntiAlias = true
        //
        stepImgPaint.style = Paint.Style.FILL
        val filter = PorterDuffColorFilter(progressColor, PorterDuff.Mode.SRC_IN)
        stepImgPaint.colorFilter = filter
        stepImgPaint.isAntiAlias = true
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null)
            return
        setupStepper(canvas)
    }

    fun setCurrentStep(currentStep: Int) {
        if (currentStep > numOfSteps) {
            throw java.lang.Exception("current step must be equal or smaller than numOfSteps($numOfSteps)")
        }
        this.currentStep = currentStep
        if (currentStep == 0) {
            this.endProgress = 0f
        } else {
            val stepDegree = 360f / numOfSteps
            this.endProgress = stepDegree * currentStep
        }

        /*if (currentProgress >= endProgress){
            this.currentProgress = endProgress
        }*/

        postInvalidate()
    }

    fun setNumOfSeps(numOfSteps: Int){
        this.numOfSteps = numOfSteps
        setCurrentStep(currentStep)
    }

    fun getNumOfSteps(): Int{
        return numOfSteps
    }

    fun getCurrentStep(): Int {
        return currentStep
    }

    fun goToNextStep() {
        if (currentStep+1 < numOfSteps){
            this.currentStep += 1
            setCurrentStep(currentStep)
        }
    }

    fun gotToPrevStep() {
        if (currentStep > 0){
            currentStep -= 1
            setCurrentStep(currentStep)
        }
    }

    fun setStepsImgSrc(stepsImgSrc: Array<Int>) {
        this.stepsImgSrc = stepsImgSrc
        setNumOfSeps(stepsImgSrc.size)
        setCurrentStep(currentStep)
    }

    fun setProgressStrokeOffset(progressStrokeOffset: Int){
        this.progressStrokeOffset = progressStrokeOffset
        postInvalidate()
    }

    fun getProgressStrokeOffset(): Int{
        return progressStrokeOffset
    }


    private fun setupStepper(canvas: Canvas) {
        val maxPadding = max(max(max(paddingRight,paddingLeft), max(paddingStart,paddingEnd)), max(paddingTop,paddingBottom))
        val circleRadius = ((min(width, height) / 2.toFloat()) - stepsSize / 2)-maxPadding
        val circleWidth = min(width, height).toFloat()-2*maxPadding
        val circleStrokeWidth = (stepsSize / 4).toFloat()
        val progressStrokeWidth = circleStrokeWidth-progressStrokeOffset

        //draw background layer
        circlePaint.strokeWidth = circleStrokeWidth
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), circleRadius, circlePaint)

        //draw progress layer
        progressPaint.strokeWidth = progressStrokeWidth

        val rectLeft = (width - circleWidth) / 2
        val rectTop = (height - circleWidth) / 2
        val rectProgress = RectF(rectLeft, rectTop, rectLeft + circleWidth, rectTop + circleWidth)
        rectProgress.inset((stepsSize / 2).toFloat(), (stepsSize / 2).toFloat())
        canvas.drawArc(rectProgress, 0f, currentProgress, false, progressPaint)

        //draw step circles
        val stepDegree = 360 / numOfSteps
        for (i in 0 until numOfSteps) {
            val currentDegree: Float = (stepDegree * i).toFloat()
            val theta = (currentDegree * Math.PI) / 180
            val centerX = width / 2 - stepsSize / 2
            val centerY = height / 2 - stepsSize / 2
            val left = (circleRadius * Math.cos(theta) + centerX).toFloat()
            val top = (circleRadius * Math.sin(theta) + centerY).toFloat()
            if (currentDegree <= currentProgress) {
                stepPaint.color = progressColor
                val filter1 = PorterDuffColorFilter(foregroundColor, PorterDuff.Mode.SRC_IN)
                stepImgPaint.colorFilter = filter1
            } else {
                stepPaint.color = foregroundColor
                val filter1 = PorterDuffColorFilter(progressColor, PorterDuff.Mode.SRC_IN)
                stepImgPaint.colorFilter = filter1
            }
            val rectF = RectF(left, top, left + stepsSize, top + stepsSize)
            canvas.drawOval(rectF, stepPaint)
            //
            if (stepsImgSrc.size > i) {
                val bitmap = BitmapFactory.decodeResource(resources,stepsImgSrc[i])
                canvas.drawBitmap(bitmap,null,rectF,stepImgPaint)
            }
        }

        if (currentProgress < endProgress) {
            currentProgress += 2.0f
            postInvalidate()
        }else if (currentProgress > endProgress){
            currentProgress -= 2.0f
            postInvalidate()
        }
    }

}