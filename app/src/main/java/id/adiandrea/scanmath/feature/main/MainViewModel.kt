package id.adiandrea.scanmath.feature.main

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import id.adiandrea.scanmath.base.BaseViewModel
import id.adiandrea.scanmath.data.DataManager
import id.adiandrea.scanmath.model.History
import id.adiandrea.scanmath.util.Constant
import id.adiandrea.scanmath.util.Constant.Companion.VALUE_STORAGE_FILE
import id.adiandrea.scanmath.util.calculateFromString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CheckResult")
class MainViewModel
@Inject constructor(private val dataManager: DataManager) : BaseViewModel() {
    internal var onHistoryLoaded = MutableLiveData<MutableList<History>>()
    internal var onWarningMessage = MutableLiveData<String>()
    private var recognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun processImageFromFile(image: InputImage){
        var finalResult: String?
        recognizer.process(image)
            .addOnSuccessListener { result ->
                if(result.textBlocks.isNotEmpty()){
                    finalResult = result.textBlocks[0].text
                    finalResult?.let {
                        val historyResult = calculateFromString(it)
                        if(historyResult != null){
                            saveData(historyResult)
                        }else{
                            onWarningMessage.postValue("finding expresssion failed, please try again")
                        }
                    }
                }
            }.addOnFailureListener { e ->
                Timber.e(e)
            }
    }

    private fun saveData(history: History){
        if(dataManager.getCurrentSelectedStorage() == Constant.VALUE_STORAGE_DATABASE){
            saveResultToLocalDatabase(history)
        }else{
            //append data
            val data = dataManager.loadDataFromEncryptedFile()
            data.add(history)
            dataManager.saveToEncryptedFile(data)
        }
        loadHistory()
    }

    private fun saveResultToLocalDatabase(history: History) = CoroutineScope(Dispatchers.IO).launch {
        dataManager.saveHistoryToLocal(history)
    }

    fun getCurrentStorage() = dataManager.getCurrentSelectedStorage()

    fun setSelectedStorage(value: String) {
        dataManager.setStorage(value)
    }

    fun loadHistory(){
        if(getCurrentStorage() == VALUE_STORAGE_FILE){
            onHistoryLoaded.postValue(dataManager.loadDataFromEncryptedFile())
        }else{
            loadHistoryFromLocalDatabase()
        }
    }

    private fun loadHistoryFromLocalDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            onHistoryLoaded.postValue(dataManager.loadHistoryFromLocal())
        }
    }
}