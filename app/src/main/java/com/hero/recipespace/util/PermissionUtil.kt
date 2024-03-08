package com.hero.recipespace.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtil {
    private const val GALLERY_PERMISSION_CODE = 0
    private const val CAMERA_PERMISSION_CODE = 1
    const val REQUEST_CODE_PERMISSIONS = 10
    private const val GALLERY_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    val REQUIRED_PERMISSIONS =
        mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                )
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                )
            } else {
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                )
            }
        }.toTypedArray()

    val camera_permission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
            )
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }

    fun requestGalleryPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                GALLERY_PERMISSION,
            ),
            GALLERY_PERMISSION_CODE,
        )
    }

    fun hasGalleryPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            GALLERY_PERMISSION,
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isGalleryPermissionGranted(activity: Activity): Boolean {
        if (!hasGalleryPermission(activity)) {
            requestGalleryPermission(activity)
            return false
        }
        return true
    }

    fun requestCameraPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                CAMERA_PERMISSION,
            ),
            CAMERA_PERMISSION_CODE,
        )
    }

    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            CAMERA_PERMISSION,
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isCameraPermissionGranted(activity: Activity): Boolean {
        if (!hasCameraPermission(activity)) {
            requestCameraPermission(activity)
            return false
        }
        return true
    }

    fun allPermissionsGranted(context: Context) =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                context,
                it,
            ) == PackageManager.PERMISSION_GRANTED
        }
}
