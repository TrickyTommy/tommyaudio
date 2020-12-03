package com.example.recordaudio

import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Environment
import android.view.View
import java.io.File

class AudioManager(private val context: Context) {
    private var mediaRecorder: MediaRecorder? =null
    private var mediaPlayer: MediaPlayer? = null

    fun startPlayback(id: Int): Boolean {
        val path = filePathForId(id)
        if (File(path).exists()) {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(path)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            return true
        }
        return false
    }

    fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun filePathForId(id: Int): String {
        return Environment.getExternalStorageDirectory().absolutePath + "/$id.acc"
    }
    fun startRecording(id: View): Boolean {

        //check the device has a microphone
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {

            //create new instance of MediaRecorder
            mediaRecorder = MediaRecorder()

            //specify source of audio (Microphone)
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)

            //specify file type and compression format
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            //specify audio sampling rate and encoding bit rate (48kHz and 128kHz respectively)
            mediaRecorder?.setAudioSamplingRate(48000)
            mediaRecorder?.setAudioEncodingBitRate(128000)

            //specify where to save
            val fileLocation = filePathForId(id)
            mediaRecorder?.setOutputFile(fileLocation)

            //record
            mediaRecorder?.prepare()
            mediaRecorder?.start()

            return true
        } else {
            return false
        }
    }

    fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
    }



}