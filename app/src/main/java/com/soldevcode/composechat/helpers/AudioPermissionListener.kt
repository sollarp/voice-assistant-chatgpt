package com.soldevcode.composechat.helpers

interface AudioPermissionListener {
    fun onAudioPermissionGranted()
    fun onAudioPermissionDenied()
}