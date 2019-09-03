package com.taleb.samplecircularprogressstepper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val stepsSrc = arrayOf(R.drawable.ic_camera,R.drawable.ic_folder,R.drawable.ic_camera,R.drawable.ic_camera)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cpsStepper.setStepsImgSrc(stepsSrc)
    }
}
