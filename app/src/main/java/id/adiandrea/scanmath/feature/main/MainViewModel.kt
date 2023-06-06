package id.adiandrea.scanmath.feature.main

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.adiandrea.scanmath.base.BaseViewModel
import id.adiandrea.scanmath.data.DataManager
import id.adiandrea.scanmath.data.local.history.History
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CheckResult")
class MainViewModel
@Inject constructor(private val dataManager: DataManager) : BaseViewModel() {
    internal var onHistoryLoaded = MutableLiveData<MutableList<History>>()

    fun loadHistoryFromLocalDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            onHistoryLoaded.postValue(dataManager.loadHistoryFromLocal())
        }
    }
}