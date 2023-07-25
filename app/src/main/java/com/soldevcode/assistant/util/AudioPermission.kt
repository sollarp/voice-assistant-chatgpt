package com.soldevcode.assistant.util

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.soldevcode.assistant.AudioPermissionListener
import com.soldevcode.assistant.util.Constants.RECORD_AUDIO_REQUEST_CODE



object AudioPermission {

    fun checkAudioPermission(activity: AppCompatActivity) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.RECORD_AUDIO
            ) -> {
                // You can use the API that requires the permission.

            }
            else -> {
                // You can directly ask for the permission.
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    RECORD_AUDIO_REQUEST_CODE

                )
            }
        }

        fun onRequestPermissionsResult(
            activity: AppCompatActivity,
            requestCode: Int,
            grantResults: IntArray
        ) {
            if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, proceed with audio-related functionality
                    (activity as? AudioPermissionListener)?.onAudioPermissionGranted()
                } else {
                    // Permission is denied, handle it gracefully
                    (activity as? AudioPermissionListener)?.onAudioPermissionDenied()
                }
            }
        }
    }
}
