package com.taleb.samplecircularprogressstepper

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import nl.dionsegijn.steppertouch.OnStepCallback


class MainActivity : AppCompatActivity() {

    private val stepsSrc = arrayOf(R.drawable.ic_camera,R.drawable.ic_folder,R.drawable.ic_camera,R.drawable.ic_camera)
    private var numOfSteps = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cpsStepper.setStepsImgSrc(stepsSrc)

        numOfSteps = cpsStepper.getNumOfSteps()
        numOfStepStepperTouch.count = cpsStepper.getNumOfSteps()
        numOfStepStepperTouch.minValue = 0
        numOfStepStepperTouch.maxValue = 100
        numOfStepStepperTouch.sideTapEnabled = true
        numOfStepStepperTouch.addStepCallback(object : OnStepCallback {
            override fun onStep(value: Int, positive: Boolean) {
                numOfSteps = value
                currentStepStepperTouch.maxValue = numOfSteps
                cpsStepper.setNumOfSteps(numOfSteps)
                Toast.makeText(applicationContext, value.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        ///
        currentStepStepperTouch.count = cpsStepper.getCurrentStep()
        currentStepStepperTouch.minValue = 0
        currentStepStepperTouch.maxValue = numOfSteps
        currentStepStepperTouch.sideTapEnabled = true
        currentStepStepperTouch.addStepCallback(object : OnStepCallback {
            override fun onStep(value: Int, positive: Boolean) {
                cpsStepper.setCurrentStep(value)
                Toast.makeText(applicationContext, value.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        progressColorTV.text = String.format("#%06X", 0xFFFFFF and cpsStepper.getProgressColor())
        progressColorV.setBackgroundColor(cpsStepper.getProgressColor())
        progressColorTV.setOnClickListener {
            showColorPicker(progressColorTV.text.toString()){
                cpsStepper.setProgressColor(it)
                progressColorTV.text = String.format("#%06X", 0xFFFFFF and it)
                progressColorV.setBackgroundColor(it)
            }
        }
        foregroundColorTV.text = String.format("#%06X", 0xFFFFFF and cpsStepper.getForegroundColor())
        foregroundColorV.setBackgroundColor(cpsStepper.getForegroundColor())
        foregroundColorTV.setOnClickListener {
            showColorPicker(foregroundColorTV.text.toString()){
                cpsStepper.setForegroundColor(it)
                foregroundColorTV.text = String.format("#%06X", 0xFFFFFF and it)
                foregroundColorV.setBackgroundColor(it)
            }
        }



        //
        progressStrokeOffsetTV.text = cpsStepper.getProgressStrokeOffset().toString()+" dp"
        progressStrokeOffsetSeekBar.progress  = (cpsStepper.getProgressStrokeOffset()/resources.displayMetrics.density).toInt()
        progressStrokeOffsetSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val offsetInDp = (progress*resources.displayMetrics.density).toInt()
                progressStrokeOffsetTV.text = "$offsetInDp dp"
                cpsStepper.setProgressStrokeOffset(offsetInDp)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        //
        progressStepSizeTV.text = cpsStepper.getStepsSize().toString()+" dp"
        progressStepSizeSeekBar.progress = (cpsStepper.getStepsSize()/resources.displayMetrics.density).toInt()
        progressStepSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val sizeInDp = (progress*resources.displayMetrics.density).toInt()
                progressStepSizeTV.text = "$sizeInDp dp"
                cpsStepper.setStepsSize(sizeInDp)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })



    }


    private fun showColorPicker(initColor: String,completion:(selectedColor:Int) -> Unit) {
        ColorPickerDialogBuilder
            .with(this)
            .setTitle("Choose color")
            .initialColor(Color.parseColor(initColor))
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorSelectedListener { selectedColor ->
                completion(selectedColor)
                Toast.makeText(this,
                    "onColorSelected: 0x" + Integer.toHexString(
                        selectedColor),Toast.LENGTH_LONG
                ).show()
            }
            .setPositiveButton(
                "ok"
            ) { dialog, selectedColor, allColors ->
                completion(selectedColor)
            }
            .setNegativeButton("cancel") { dialog, which -> }
            .build()
            .show()
    }
}
