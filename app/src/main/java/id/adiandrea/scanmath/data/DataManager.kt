package id.adiandrea.scanmath.data

import id.adiandrea.scanmath.data.local.pokemon.LocalPokemon
import id.adiandrea.scanmath.network.APIService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Singleton
import javax.inject.Inject
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import androidx.paging.DataSource
import androidx.security.crypto.EncryptedFile
import id.adiandrea.scanmath.data.local.history.History
import id.adiandrea.scanmath.feature.encryptedfile.EncryptedFileSystem
import id.adiandrea.scanmath.model.api.detailpokemon.DetailPokemonResponse
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

    fun saveToEncryptedFile(data: String) {
        encryptedFile.saveData(data)
    }

    fun loadDataFromEncryptedFile(): String {
        return encryptedFile.getData()
    }

    /* -------------------------------------- SharedPrefs --------------------------------------- */

    fun setStorage(value: String) { prefs.putString(KEY_SELECTED_STORAGE, value) }

    fun getCurrentSelectedStorage(): String = prefs.getString(KEY_SELECTED_STORAGE, VALUE_STORAGE_DATABASE)!!

    /* ---------------------------------------- SQLite ------------------------------------------ */

    fun saveAllPokemonToLocal(listPokemon: List<LocalPokemon>): Completable {
        return Completable.fromAction {
            localDatabase.PokemonDao().saveAllPokemon(listPokemon)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun loadAllPokemonFromLocal(): DataSource.Factory<Int, LocalPokemon> {
        return localDatabase.PokemonDao().loadAllPokemonPaged()
    }

    fun saveHistoryToLocal(history: History) {
        localDatabase.HistoryDao().insert(history)
    }

    fun loadHistoryFromLocal(): MutableList<History> {
        return localDatabase.HistoryDao().getAll()
    }

    /* ---------------------------------------- Network ----------------------------------------- */

    suspend fun reqPokemon(page: Int, limit: Int) = api.requestListPokemon(limit, page)

    fun reqDetailPokemon(name: String): Single<Response<DetailPokemonResponse>> {
        return api.requestDetailPokemon(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}