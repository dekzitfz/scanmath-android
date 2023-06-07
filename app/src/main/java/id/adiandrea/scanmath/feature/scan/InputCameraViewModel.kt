package id.adiandrea.scanmath.feature.scan

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import id.adiandrea.scanmath.base.BaseViewModel
import id.adiandrea.scanmath.data.DataManager
import id.adiandrea.scanmath.model.History
import id.adiandrea.scanmath.util.Constant.Companion.VALUE_STORAGE_DATABASE
import id.adiandrea.scanmath.util.calculateFromString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CheckResult")
class InputCameraViewModel
@Inject constructor(private val dataManager: DataManager) : BaseViewModel() {

    internal var onWarningMessage = MutableLiveData<String>()
    internal var isLoading = MutableLiveData<Boolean>()
    internal var onProcessCompleted = MutableLiveData<Boolean>()
    private var recognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun processImageFromCamera(image: InputImage){
        isLoading.postValue(true)
        recognizer.process(image)
            .addOnSuccessListener { result ->
                if(result.textBlocks.isNotEmpty()){
                    val finalResult = result.textBlocks[0].text
                    finalResult.let {
                        val historyResult = calculateFromString(it)
                        if(historyResult != null){
                            saveData(historyResult)
                        }else{
                            isLoading.postValue(false)
                            onProcessCompleted.postValue(true)
                            onWarningMessage.postValue("finding expresssion failed, please try again")
                        }
                    }
                }
            }.addOnFailureListener { e ->
                isLoading.postValue(false)
                onProcessCompleted.postValue(true)
                Timber.e(e)
            }
    }

    private fun saveData(history: History){
        if(dataManager.getCurrentSelectedStorage() == VALUE_STORAGE_DATABASE){
            CoroutineScope(Dispatchers.IO).launch {
                dataManager.saveHistoryToLocal(history)
            }
        }else{
            val data = dataManager.loadDataFromEncryptedFile()
            data.add(history)
            dataManager.saveToEncryptedFile(data)
        }

        isLoading.postValue(false)
        onProcessCompleted.postValue(true)
    }

}