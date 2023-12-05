package com.hero.recipespace.view.post

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaActionSound
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraState
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.common.util.concurrent.ListenableFuture
import com.hero.recipespace.R
import com.hero.recipespace.databinding.ActivityCameraBinding
import com.hero.recipespace.databinding.CameraUiContainerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraUiContainerBinding: CameraUiContainerBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var mediaStoreUtils: MediaStoreUtils

    private var displayId: Int = -1
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var cameraProviderListenableFuture: ListenableFuture<ProcessCameraProvider>? = null
    private val sound = MediaActionSound()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this@CameraActivity)
        cameraUiContainerBinding = CameraUiContainerBinding.inflate(LayoutInflater.from(this), binding.root, true)

        cameraExecutor = Executors.newSingleThreadExecutor()
        mediaStoreUtils = MediaStoreUtils(this)

        setupListeners()
        cameraAddListener()

        binding.viewFinder.post {
            // Keep track of the display in which this view is attached
            displayId = binding.viewFinder.display.displayId
        }
    }

    private fun cameraAddListener() {
        cameraProviderListenableFuture?.addListener({
            try {
                val cameraProvider = cameraProviderListenableFuture?.get()
                startCamera(cameraProvider)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        cameraUiContainerBinding.cameraCaptureButton.setOnClickListener {
            takePhoto()
        }

        // In the background, load latest photo taken (if any) for gallery thumbnail
        lifecycleScope.launch {
            val thumbnailUri = mediaStoreUtils.getLatestImageFilename()
            if (thumbnailUri != null) {
                setGalleryThumbnail(thumbnailUri)
            }
        }
    }

    private fun startCamera(cameraProvider: ProcessCameraProvider?) {
        cameraProvider?.unbindAll() // 열려 있는 모든 카메라 닫기

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val preview = Preview.Builder().build()

        preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        if (camera != null) {
            // Must remove observers from the previous camera instance
            removeCameraStateObservers(camera!!.cameraInfo)
        }

        camera = cameraProvider?.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        observeCameraState(camera?.cameraInfo!!)
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, PHOTO_TYPE)
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("failed", "Photo capture failed: ${exc.message}", exc)
                    exc.printStackTrace()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "사진 촬영 성공 → 사진 경로: ${output.savedUri}"
                    Toast.makeText(this@CameraActivity, msg, Toast.LENGTH_SHORT).show()
                    Log.d("succeeded", msg)

                    if (camera?.cameraInfo?.hasFlashUnit() == true) {
                        camera?.cameraControl?.enableTorch(true)
                    }

                    sound.play(MediaActionSound.SHUTTER_CLICK)

                    runOnUiThread {
                        Handler(Looper.myLooper()!!).postDelayed({
                            camera?.cameraControl?.enableTorch(false)
                        }, 100)
                    }

                    onTakePhotoSuccess(output)
                }
            }
        )
    }

    private fun onTakePhotoSuccess(output: ImageCapture.OutputFileResults) {
        val intent = PhotoResultActivity.getIntent(this, output.savedUri.toString())
        startActivity(intent)
    }

    private fun setGalleryThumbnail(filename: String) {
        cameraUiContainerBinding.photoViewButton.post {
            // Remove thumbnail padding
                cameraUiContainerBinding.photoViewButton.setPadding(resources.getDimension(R.dimen.stroke_small).toInt())

            // Load thumbnail into circular button using Glide
            Glide.with(cameraUiContainerBinding.photoViewButton)
                .load(filename)
                .apply(RequestOptions.circleCropTransform())
                .into(cameraUiContainerBinding.photoViewButton)
        }
    }

    private fun removeCameraStateObservers(cameraInfo: CameraInfo) {
        cameraInfo.cameraState.removeObservers(this)
    }

    private fun observeCameraState(cameraInfo: CameraInfo) {
        cameraInfo.cameraState.observe(this) { cameraState ->
            run {
                when (cameraState.type) {
                    CameraState.Type.PENDING_OPEN -> {
                        // Ask the user to close other camera apps
                        Log.d("observeCameraState", "observeCameraState: Pending_Open")
                    }
                    CameraState.Type.OPENING -> {
                        // Show the Camera UI
                        Log.d("observeCameraState", "observeCameraState: OPENING")
                    }
                    CameraState.Type.OPEN -> {
                        // Setup Camera resources and begin processing
                        Log.d("observeCameraState", "observeCameraState: OPEN")
                    }
                    CameraState.Type.CLOSING -> {
                        // Close camera UI
                        Log.d("observeCameraState", "observeCameraState: Closing")
                    }
                    CameraState.Type.CLOSED -> {
                        // Free camera resources
                        Log.d("observeCameraState", "observeCameraState: Closed")
                    }
                }
            }

            cameraState.error?.let { error ->
                when (error.code) {
                    // Open errors
                    CameraState.ERROR_STREAM_CONFIG -> {
                        // Make sure to setup the use cases properly
                        Log.d("observeCameraState", "observeCameraState: Stream config error")
                    }
                    // Opening errors
                    CameraState.ERROR_CAMERA_IN_USE -> {
                        // Close the camera or ask user to close another camera app that's using the camera
                        Log.d("observeCameraState", "observeCameraState: Camera in use")
                    }
                    CameraState.ERROR_MAX_CAMERAS_IN_USE -> {
                        // Close another open camera in the app, or ask the user to close another
                        // camera app that's using the camera
                        Log.d("observeCameraState", "observeCameraState: Max cameras in use")
                    }
                    CameraState.ERROR_OTHER_RECOVERABLE_ERROR -> {
                        Log.d("observeCameraState", "observeCameraState: Other recoverable error")
                    }
                    // Closing errors
                    CameraState.ERROR_CAMERA_DISABLED -> {
                        // Ask the user to enable the device's cameras
                        Log.d("observeCameraState", "observeCameraState: Camera disabled")
                    }
                    CameraState.ERROR_CAMERA_FATAL_ERROR -> {
                        // Ask the user to reboot the device to restore camera function
                        Log.d("observeCameraState", "observeCameraState: Fatal error")
                    }
                    // Closed errors
                    CameraState.ERROR_DO_NOT_DISTURB_MODE_ENABLED -> {
                        // Ask the user to disable the "Do Not Disturb" mode, then reopen the camera
                        Log.d("observeCameraState", "observeCameraState: Do not disturb mode enabled")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        cameraProviderListenableFuture = null
        imageCapture = null
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_TYPE = "image/jpeg"

        fun getIntent(context: Context) =
            Intent(context, CameraActivity::class.java)
    }
}


/**
 * A utility class for accessing this app's photo storage.
 *
 * Since this app doesn't request any external storage permissions, it will only be able to access
 * photos taken with this app. If the app is uninstalled, the photos taken with this app will stay
 * on the device, but reinstalling the app will not give it access to photos taken with the app's
 * previous instance. You can request further permissions to change this app's access. See this
 * guide for more: https://developer.android.com/training/data-storage.
 */
class MediaStoreUtils(private val context: Context) {

    private val mediaStoreCollection: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        context.getExternalFilesDir(null)?.toUri()
    }

    private suspend fun getMediaStoreImageCursor(mediaStoreCollection: Uri): Cursor? {
        var cursor: Cursor?
        withContext(Dispatchers.IO) {
            val projection = arrayOf(imageDataColumnIndex, imageIdColumnIndex)
            val sortOrder = "DATE_ADDED DESC"
            cursor = context.contentResolver.query(
                mediaStoreCollection, projection, null, null, sortOrder
            )
        }
        return cursor
    }

    suspend fun getLatestImageFilename(): String? {
        var filename: String?
        if (mediaStoreCollection == null) return null

        getMediaStoreImageCursor(mediaStoreCollection).use { cursor ->
            if (cursor?.moveToFirst() != true) return null
            filename = cursor.getString(cursor.getColumnIndexOrThrow(imageDataColumnIndex))
        }

        return filename
    }

    suspend fun getImages(): MutableList<MediaStoreFile> {
        val files = mutableListOf<MediaStoreFile>()
        if (mediaStoreCollection == null) return files

        getMediaStoreImageCursor(mediaStoreCollection).use { cursor ->
            val imageDataColumn = cursor?.getColumnIndexOrThrow(imageDataColumnIndex)
            val imageIdColumn = cursor?.getColumnIndexOrThrow(imageIdColumnIndex)

            if (cursor != null && imageDataColumn != null && imageIdColumn != null) {
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(imageIdColumn)
                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    val contentFile = File(cursor.getString(imageDataColumn))
                    files.add(MediaStoreFile(contentUri, contentFile, id))
                }
            }
        }

        return files
    }

    companion object {
        // Suppress DATA index deprecation warning since we need the file location for the Glide library
        @Suppress("DEPRECATION")
        private const val imageDataColumnIndex = MediaStore.Images.Media.DATA
        private const val imageIdColumnIndex = MediaStore.Images.Media._ID
    }
}

data class MediaStoreFile(val uri: Uri, val file: File, val id: Long)