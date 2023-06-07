package id.adiandrea.scanmath.data.local.history

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.adiandrea.scanmath.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): MutableList<History>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(history: History)

    @Delete
    fun delete(history: History)
}