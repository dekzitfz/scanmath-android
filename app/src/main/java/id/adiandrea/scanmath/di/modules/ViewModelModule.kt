package id.adiandrea.scanmath.di.modules

import dagger.Module
import androidx.lifecycle.ViewModel
import id.adiandrea.scanmath.di.scopes.ViewModelKey
import id.adiandrea.scanmath.feature.listpokemon.ListPokemonViewModel
import dagger.multibindings.IntoMap
import dagger.Binds
import id.adiandrea.scanmath.base.AppViewModelFactory
import androidx.lifecycle.ViewModelProvider
import id.adiandrea.scanmath.feature.CalculatorViewModel
import id.adiandrea.scanmath.feature.detailpokemon.DetailPokemonViewModel
import id.adiandrea.scanmath.feature.detailpokemon.basestat.BaseStatViewModel
import id.adiandrea.scanmath.feature.detailpokemon.moves.MovesViewModel
import id.adiandrea.scanmath.feature.main.MainViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalculatorViewModel::class)
    abstract fun bindCalculatorViewModel(calculatorViewModel: CalculatorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BaseStatViewModel::class)
    abstract fun bindBaseStatViewModel(baseStatViewModel: BaseStatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovesViewModel::class)
    abstract fun bindMovesViewModel(movesViewModel: MovesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailPokemonViewModel::class)
    abstract fun bindDetailPokemonViewModel(detailPokemonViewModel: DetailPokemonViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListPokemonViewModel::class)
    abstract fun bindListPokemonViewModel(listPokemonViewModel: ListPokemonViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}