package id.adiandrea.scanmath.feature.main

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import id.adiandrea.scanmath.base.BaseViewModel
import id.adiandrea.scanmath.data.DataManager
import id.adiandrea.scanmath.data.local.history.History
import id.adiandrea.scanmath.feature.encryptedfile.EncryptedFileSystem
import id.adiandrea.scanmath.util.Constant.Companion.VALUE_STORAGE_FILE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CheckResult")
class MainViewModel
@Inject constructor(private val dataManager: DataManager) : BaseViewModel() {
    internal var onHistoryLoaded = MutableLiveData<MutableList<History>>()

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