package com.gzzdev.saveclass.ui.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.gzzdev.saveclass.R
import java.io.File
import java.time.LocalDateTime

class ActionsPermissionLifecycleObserver(
    private val view: View,
    private val activity: FragmentActivity,
    private val onCompleteAction: (String, Boolean, String) -> Unit
) : DefaultLifecycleObserver {
    lateinit var context: Context
    private var pictureId: String = ""
    private lateinit var _takePicture: ActivityResultLauncher<Uri>
    private lateinit var _requestPermission: ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner) {
        context = view.context
        _takePicture = activity.activityResultRegistry.register(
            TAKE_PICTURE,
            owner,
            ActivityResultContracts.TakePicture()
        ) { isSaved ->
            onCompleteAction(
                TAKE_PICTURE,
                isSaved,
                if (!isSaved) context.getString(R.string.take_picture_error) else pictureId
            )
        }
        _requestPermission = activity.activityResultRegistry.register(
            REQUEST_PERMISSION,
            owner,
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            onCompleteAction(
                REQUEST_PERMISSION,
                isGranted,
                if (!isGranted) context.getString(R.string.camera_permission_required) else "",
            )
        }
    }

    fun takePicture() {
        when {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                pictureId = LocalDateTime.now().nano.toString()
                val cacheDirectory = File(context.cacheDir, "images")
                if (!cacheDirectory.exists()) cacheDirectory.mkdirs()
                val pictureFile = File.createTempFile(
                    pictureId, ".jpg",
                    cacheDirectory
                )
                val picture = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    pictureFile
                )
                _takePicture.launch(picture)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            ) -> {
                Log.i("permission_observer", "show request permission.")
                view.showMessage(
                    context.getString(R.string.camera_permission_required),
                    duration = Snackbar.LENGTH_INDEFINITE,
                    actionMessage = context.getString(R.string.snackbar_ok)
                ) {
                    _requestPermission.launch(Manifest.permission.CAMERA)
                }
            }

            else -> {
                _requestPermission.launch(Manifest.permission.CAMERA)
            }
        }
    }

    companion object {
        const val TAKE_PICTURE = "take_picture"
        const val REQUEST_PERMISSION = "request_permission"
    }
}