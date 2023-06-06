package id.adiandrea.scanmath.feature.listpokemon

import androidx.paging.*
import id.adiandrea.scanmath.base.BaseViewModel
import id.adiandrea.scanmath.data.DataManager
import id.adiandrea.scanmath.model.api.pokemon.Pokemon
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

class ListPokemonViewModel
@Inject constructor(private val dataManager: DataManager) : BaseViewModel(){

    lateinit var dataFlow: Flow<PagingData<Pokemon>>

    fun loadData() {
        dataFlow = Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10),
            pagingSourceFactory = { ListPokemonDataSource(dataManager) }
        ).flow.cachedIn(viewModelScope)
    }

}