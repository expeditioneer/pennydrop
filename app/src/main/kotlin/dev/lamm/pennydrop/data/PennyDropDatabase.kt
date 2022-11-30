package dev.lamm.pennydrop.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.lamm.pennydrop.types.Player

@Database(
    entities = [Game::class, Player::class, GameStatus::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PennyDropDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "PennyDropDatabase"
    }

    abstract fun pennyDropDao(): PennyDropDao
}
