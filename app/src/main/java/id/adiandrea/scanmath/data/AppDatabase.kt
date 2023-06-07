package id.adiandrea.scanmath.data

import androidx.room.Database
import androidx.room.RoomDatabase
import id.adiandrea.scanmath.model.History
import id.adiandrea.scanmath.data.local.history.HistoryDao


@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun HistoryDao(): HistoryDao



}