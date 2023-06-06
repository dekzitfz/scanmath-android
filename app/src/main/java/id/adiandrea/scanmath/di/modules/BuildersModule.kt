package id.adiandrea.scanmath.di.modules

import id.adiandrea.scanmath.feature.listpokemon.ListPokemonActivity
import id.adiandrea.scanmath.feature.detailpokemon.DetailPokemonActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule{
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun bindDetailPokemonActivity(): DetailPokemonActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun bindListPokemonActivity(): ListPokemonActivity
}