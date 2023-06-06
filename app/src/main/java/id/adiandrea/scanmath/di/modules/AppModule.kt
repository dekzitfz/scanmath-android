package id.adiandrea.scanmath.di.modules

import android.content.Context
import androidx.room.Room
import id.adiandrea.scanmath.App
import id.adiandrea.scanmath.data.AppDatabase
import id.adiandrea.scanmath.data.AppDatabase.Companion.MIGRATION_1_2
import dagger.Provides
import id.adiandrea.scanmath.di.scopes.ApplicationContext
import dagger.Module
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @ApplicationContext
    fun provideContext(application: App): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideAppDatabase(application: App): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "scanmath.db")
            //.addMigrations(MIGRATION_1_2) //TODO enable this to test migration from v1 to v2
            .build()
    }

}