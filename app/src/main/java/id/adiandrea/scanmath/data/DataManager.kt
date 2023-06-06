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
import id.adiandrea.scanmath.data.local.history.History
import id.adiandrea.scanmath.model.api.detailpokemon.DetailPokemonResponse


@Singleton
class DataManager
@Inject constructor(
    private val api: APIService,
    private val prefs: PreferencesHelper,
    private val localDatabase: AppDatabase){

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

    /* ---------------------------------------- Network ----------------------------------------- */

    suspend fun reqPokemon(page: Int, limit: Int) = api.requestListPokemon(limit, page)

    fun reqDetailPokemon(name: String): Single<Response<DetailPokemonResponse>> {
        return api.requestDetailPokemon(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}