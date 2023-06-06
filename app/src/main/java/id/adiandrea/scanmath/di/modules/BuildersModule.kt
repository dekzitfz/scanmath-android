package id.adiandrea.scanmath.di.modules

import id.adiandrea.scanmath.feature.listpokemon.ListPokemonActivity
import id.adiandrea.scanmath.feature.detailpokemon.DetailPokemonActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.adiandrea.scanmath.feature.main.MainActivity
import id.adiandrea.scanmath.feature.scan.InputCameraActivity

@Module
abstract class BuildersModule{
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun bindInputCameraActivity(): InputCameraActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun bindDetailPokemonActivity(): DetailPokemonActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun bindListPokemonActivity(): ListPokemonActivity
}