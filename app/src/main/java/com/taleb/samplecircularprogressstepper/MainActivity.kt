package com.taleb.samplecircularprogressstepper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Color
import android.widget.SeekBar
import android.widget.Toast
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import nl.dionsegijn.steppertouch.OnStepCallback


class MainActivity : AppCompatActivity() {

    private val stepsSrc = arrayOf(R.drawable.ic_camera,R.drawable.ic_folder,R.drawable.ic_camera,R.drawable.ic_camera)
    private var numOfSteps = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //cpsStepper.setStepsImgSrc(stepsSrc)

        numOfSteps = cpsStepper.getNumOfSteps()
        numOfStepStepperTouch.count = cpsStepper.getNumOfSteps()
        numOfStepStepperTouch.minValue = 0
        numOfStepStepperTouch.maxValue = 100
        numOfStepStepperTouch.sideTapEnabled = true
        numOfStepStepperTouch.addStepCallback(object : OnStepCallback {
            override fun onStep(value: Int, positive: Boolean) {
                numOfSteps = value
                currentStepStepperTouch.maxValue = numOfSteps
                cpsStepper.setNumOfSeps(numOfSteps)
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

        //
        progressStrokeOffsetTV.text = cpsStepper.getProgressStrokeOffset().toString()+" dp"
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



    }


    private fun showColorPicker() {
        ColorPickerDialogBuilder
            .with(this)
            .setTitle("Choose color")
            .initialColor(Color.parseColor("#DA5DCA"))
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorSelectedListener { selectedColor ->
                Toast.makeText(this,
                    "onColorSelected: 0x" + Integer.toHexString(
                        selectedColor),Toast.LENGTH_LONG
                ).show()
            }
            .setPositiveButton(
                "ok"
            ) { dialog, selectedColor, allColors ->/* changeBackgroundColor(selectedColor)*/ }
            .setNegativeButton("cancel") { dialog, which -> }
            .build()
            .show()
    }
}
