package com.example.recordaudio

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQ = 1

    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawBoard(1, 1)
        requestPermissions()

        audioManager = AudioManager(this)
    }

    private fun requestPermissions() {
        val permissionsRequired = mutableListOf<String>()

        val hasRecordPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasRecordPermission) {
            permissionsRequired.add(Manifest.permission.RECORD_AUDIO)
        }

        val hasStoragePermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasStoragePermission) {
            permissionsRequired.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionsRequired.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsRequired.toTypedArray(),
                    PERMISSIONS_REQ
            )
        }
    }




    private val touchListener = View.OnTouchListener { v: View?, event: MotionEvent? ->
        Boolean

        val id = v?.tag as Int

        if (event?.action == MotionEvent.ACTION_DOWN) {

            if (btn_mic.isChecked) { //recording
                val isRecording = audioManager.startRecording(id)
                if (isRecording) {
                    v.background.setColorFilter(Color.RED, PorterDuff.Mode.DARKEN)
                    //PorterDuff is a class with list of blending + compositing modes, named after the authors of a paper on the subject
                } else {
                    Toast.makeText(this, "Unable to start recording", Toast.LENGTH_LONG).show()
                }

            } else {
                if (audioManager.startPlayback(id)) {
                    v.background.setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN)
                }
            }

            btn_mic.isEnabled = false

            return@OnTouchListener true
        }
        if (event?.action == MotionEvent.ACTION_UP) {

            if (btn_mic.isChecked) {
                audioManager.stopRecording()
            } else {
                audioManager.stopPlayback()
            }

            v.background.clearColorFilter()
            btn_mic.isEnabled = true

            return@OnTouchListener true
        }

        false
    }

    fun record(view: View) {
        if(view.isPressed){
            val isRecording = audioManager.startRecording(view)
            view.background.setColorFilter(Color.RED, PorterDuff.Mode.DARKEN)

        }
    }


}