package id.adiandrea.scanmath.data

import id.adiandrea.scanmath.network.APIService
import javax.inject.Singleton
import javax.inject.Inject
import id.adiandrea.scanmath.model.History
import id.adiandrea.scanmath.feature.encryptedfile.EncryptedFileSystem
import id.adiandrea.scanmath.util.Constant.Companion.KEY_SELECTED_STORAGE
import id.adiandrea.scanmath.util.Constant.Companion.VALUE_STORAGE_DATABASE


@Singleton
class DataManager
@Inject constructor(
    private val api: APIService,
    private val prefs: PreferencesHelper,
    private val localDatabase: AppDatabase,
    private val encryptedFile: EncryptedFileSystem){

    /* ------------------------------------- EncryptedFile -------------------------------------- */

    fun saveToEncryptedFile(data: MutableList<History>) {
        encryptedFile.saveData(data)
    }

    fun loadDataFromEncryptedFile(): MutableList<History> = encryptedFile.getData()

    /* -------------------------------------- SharedPrefs --------------------------------------- */

    fun setStorage(value: String) { prefs.putString(KEY_SELECTED_STORAGE, value) }

    fun getCurrentSelectedStorage(): String = prefs.getString(KEY_SELECTED_STORAGE, VALUE_STORAGE_DATABASE)!!

    /* ---------------------------------------- SQLite ------------------------------------------ */

    fun saveHistoryToLocal(history: History) {
        localDatabase.HistoryDao().insert(history)
    }

    fun loadHistoryFromLocal(): MutableList<History> {
        return localDatabase.HistoryDao().getAll()
    }

    /* ---------------------------------------- Network ----------------------------------------- */

}