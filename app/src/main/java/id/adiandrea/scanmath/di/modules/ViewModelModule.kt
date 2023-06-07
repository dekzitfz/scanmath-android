package id.adiandrea.scanmath.di.modules

import dagger.Module
import androidx.lifecycle.ViewModel
import id.adiandrea.scanmath.di.scopes.ViewModelKey
import dagger.multibindings.IntoMap
import dagger.Binds
import id.adiandrea.scanmath.base.AppViewModelFactory
import androidx.lifecycle.ViewModelProvider
import id.adiandrea.scanmath.feature.CalculatorViewModel
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
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}