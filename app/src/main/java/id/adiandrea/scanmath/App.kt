package id.adiandrea.scanmath

import android.app.Application
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject
import dagger.android.AndroidInjector
import id.adiandrea.scanmath.di.component.AppComponent
import id.adiandrea.scanmath.di.modules.NetworkModule
import id.adiandrea.scanmath.di.component.DaggerAppComponent
import dagger.android.HasAndroidInjector
import timber.log.Timber

class App : Application(), HasAndroidInjector {

    @Inject lateinit var androidInjector : DispatchingAndroidInjector<Any>

    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .networkModule(NetworkModule())
            .build()

        appComponent?.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        lateinit var instance: App
    }
}