package id.adiandrea.scanmath.feature.scan

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage

class ImageAnalyzer(private val listener: ImageAnalyzerListener): ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val image = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
            listener.onImageCaptured(image)
        }
    }

    interface ImageAnalyzerListener{
        fun onImageCaptured(image: InputImage)
    }
}