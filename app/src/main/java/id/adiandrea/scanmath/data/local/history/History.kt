package id.adiandrea.scanmath.data.local.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey val uid: Int? = null,
    val arg1: Int = 0,
    val arg2: Int = 0,
    var symbol: String = "",
    val result: Double = 0.0
)