package com.gzzdev.saveclass.ui.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.gzzdev.saveclass.R
import java.io.File
import java.time.LocalDateTime

class MultimediaManagerObserver(
    private val view: View,
    private val context: Context,
    private val onPhotoCaptured: (Uri) -> Unit
) : DefaultLifecycleObserver {
    private lateinit var _takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var _requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var photoUri: Uri

    override fun onCreate(owner: LifecycleOwner) {
        _takePictureLauncher = (context as FragmentActivity).activityResultRegistry.register(
            TAKE_PICTURE,
            owner,
            ActivityResultContracts.TakePicture()
        ) { isSaved ->
            if (isSaved) {
                onPhotoCaptured.invoke(photoUri)
            } else {
                view.showMessage(context.getString(R.string.take_picture_error))
            }
        }

        _requestPermissionLauncher = context.activityResultRegistry.register(
            REQUEST_PERMISSION,
            owner,
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                takePhoto()
            } else {
                view.showMessage(context.getString(R.string.camera_permission_required))
            }
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun takePhoto() {
        if (hasCameraPermission()) {
            val pictureId = LocalDateTime.now().nano.toString()
            val cacheDirectory = File(context.cacheDir, "images")
            if (!cacheDirectory.exists()) cacheDirectory.mkdirs()
            val pictureFile = File.createTempFile(pictureId, ".jpg", cacheDirectory)
            photoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pictureFile
            )
            _takePictureLauncher.launch(photoUri)
        } else {
            _requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    companion object {
        const val TAKE_PICTURE = "take_picture"
        const val REQUEST_PERMISSION = "request_permission"
    }
}