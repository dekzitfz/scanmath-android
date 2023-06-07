package id.adiandrea.scanmath.feature.scan

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import id.adiandrea.scanmath.base.BaseActivity
import id.adiandrea.scanmath.databinding.ActivityInputCameraBinding
import id.adiandrea.scanmath.util.LoadingDialog
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class InputCameraActivity: BaseActivity<InputCameraViewModel>() {
    override val viewModelClass: Class<InputCameraViewModel> = InputCameraViewModel::class.java
    private lateinit var binding: ActivityInputCameraBinding

    private lateinit var recognizer: TextRecognizer
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageCapture: ImageCapture
    private var loadingView: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeViewModel()

        cameraExecutor = Executors.newSingleThreadExecutor()
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        startCamera()

        binding.btnTakePicture.setOnClickListener {
            imageCapture.takePicture(
                ContextCompat.getMainExecutor(this),
                object: ImageCapture.OnImageCapturedCallback() {
                    @SuppressLint("UnsafeOptInUsageError")
                    override fun onCaptureSuccess(imageProxy: ImageProxy) {
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                            viewModel.processImageFromCamera(image)
                        }
                    }
                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(this@InputCameraActivity, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                        Timber.e("OnImageCapturedCallback ERROR")
                        Timber.e(exception)
                    }
                }
            )
        }
    }

    override fun onDestroy() {
        cameraExecutor.shutdown()
        super.onDestroy()
    }

    private fun observeViewModel() {
        viewModel.onWarningMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoading.observe(this) {
            if(it){
                loadingView = LoadingDialog()
                loadingView?.isCancelable = false
                loadingView?.show(supportFragmentManager, null)
            }else{
                loadingView?.dismiss()
            }
        }

        viewModel.onProcessCompleted.observe(this) {
            this@InputCameraActivity.finish()
        }
    }

    private fun startCamera() {
        Timber.i("starting camera...")
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(this, exc.localizedMessage, Toast.LENGTH_SHORT).show()
                Timber.e(exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

}