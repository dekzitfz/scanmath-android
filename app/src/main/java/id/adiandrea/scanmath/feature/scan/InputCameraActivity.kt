package id.adiandrea.scanmath.feature.scan

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
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
import id.adiandrea.scanmath.feature.CalculatorViewModel
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class InputCameraActivity: BaseActivity<CalculatorViewModel>(),
    ImageAnalyzer.ImageAnalyzerListener {
    override val viewModelClass: Class<CalculatorViewModel> = CalculatorViewModel::class.java
    private lateinit var binding: ActivityInputCameraBinding

    private lateinit var recognizer: TextRecognizer
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                            onImageCaptured(image)
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

    private fun startCamera() {
        Timber.i("starting camera...")
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, ImageAnalyzer(this@InputCameraActivity))
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture/*, imageAnalyzer*/
                )

            } catch (exc: Exception) {
                Toast.makeText(this, exc.localizedMessage, Toast.LENGTH_SHORT).show()
                Timber.e(exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onImageCaptured(image: InputImage) {
        var finalResult: String?
        val resultTask = recognizer.process(image)
            .addOnSuccessListener { result ->
                if(result.textBlocks.isNotEmpty()){
                    finalResult = result.textBlocks[0].text
                    finalResult?.let {
                        val calculateResult = viewModel.calculateFromString(it)
                        viewModel.latestCalculation?.let {
                            viewModel.saveResultToLocalDatabase(it)
                        }
                        Toast.makeText(this, "RESULT: $calculateResult", Toast.LENGTH_SHORT).show()
                    }
                }
                val resultText = result.text
                Timber.i("resultText: $resultText")
                for (block in result.textBlocks) {
                    val blockText = block.text
                    Timber.i("blockText: $blockText")
                    val blockCornerPoints = block.cornerPoints
                    val blockFrame = block.boundingBox
                    if(block.lines.isNotEmpty()){
                        //Toast.makeText(this, "result: ${block.lines[0].text}", Toast.LENGTH_LONG).show()
                    }
                    for (line in block.lines) {
                        val lineText = line.text
                        Timber.i("lineText: $lineText")
                        val lineCornerPoints = line.cornerPoints
                        val lineFrame = line.boundingBox
                        for (element in line.elements) {
                            val elementText = element.text
                            Timber.i("elementText: $elementText")
                            val elementCornerPoints = element.cornerPoints
                            val elementFrame = element.boundingBox
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                Timber.e(e)
            }
    }

}