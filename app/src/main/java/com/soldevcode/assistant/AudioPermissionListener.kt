package com.soldevcode.assistant

interface AudioPermissionListener {
    fun onAudioPermissionGranted()
    fun onAudioPermissionDenied()
}