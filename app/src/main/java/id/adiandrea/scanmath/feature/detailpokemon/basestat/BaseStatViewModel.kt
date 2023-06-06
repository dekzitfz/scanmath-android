package id.adiandrea.scanmath.feature.detailpokemon.basestat

import id.adiandrea.scanmath.base.BaseViewModel
import id.adiandrea.scanmath.data.DataManager
import id.adiandrea.scanmath.model.api.detailpokemon.DetailPokemonResponse
import javax.inject.Inject

class BaseStatViewModel
@Inject constructor(private val dataManager: DataManager) : BaseViewModel(){

    fun generateBaseStat(data: DetailPokemonResponse) = data.stats

}