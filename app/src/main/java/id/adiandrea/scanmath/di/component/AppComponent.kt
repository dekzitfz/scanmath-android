package id.adiandrea.scanmath.di.component

import id.adiandrea.scanmath.App
import id.adiandrea.scanmath.di.modules.NetworkModule
import dagger.BindsInstance
import id.adiandrea.scanmath.di.modules.BuildersModule
import id.adiandrea.scanmath.di.modules.AppModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    NetworkModule::class,
    BuildersModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        @BindsInstance
        fun networkModule(networkModule: NetworkModule): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}