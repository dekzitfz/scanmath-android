package id.adiandrea.scanmath.di.modules

import id.adiandrea.scanmath.feature.detailpokemon.basestat.BaseStatFragment
import id.adiandrea.scanmath.feature.detailpokemon.moves.MovesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeBaseStatFragment(): BaseStatFragment

    @ContributesAndroidInjector
    abstract fun contributeMovesFragment(): MovesFragment

}