package id.adiandrea.scanmath.di.modules

import android.content.Context
import androidx.room.Room
import androidx.security.crypto.MasterKey
import id.adiandrea.scanmath.App
import id.adiandrea.scanmath.data.AppDatabase
import dagger.Provides
import id.adiandrea.scanmath.di.scopes.ApplicationContext
import dagger.Module
import id.adiandrea.scanmath.feature.encryptedfile.EncryptedFileSystem
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
            .build()
    }

    @Provides
    @Singleton
    fun provideMasterKey(application: App): MasterKey {
        return MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    @Provides
    @Singleton
    fun provideEncryptedFileSystem(masterKey: MasterKey): EncryptedFileSystem {
        return EncryptedFileSystem(masterKey)
    }

}