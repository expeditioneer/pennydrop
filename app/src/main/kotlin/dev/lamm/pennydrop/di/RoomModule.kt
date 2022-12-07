package dev.lamm.pennydrop.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.lamm.pennydrop.data.PennyDropCallback
import dev.lamm.pennydrop.data.PennyDropDao
import dev.lamm.pennydrop.data.PennyDropDatabase
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideApplicationDatabase(
        @ApplicationContext context: Context,
        provider: Provider<PennyDropDao>
    ): PennyDropDatabase {
        return Room.databaseBuilder(
            context,
            PennyDropDatabase::class.java,
            PennyDropDatabase.DATABASE_NAME
        ).addCallback(
            PennyDropCallback(provider)
        ).build()
    }

    @Singleton
    @Provides
    fun providePennyDropDao(applicationDatabase: PennyDropDatabase): PennyDropDao {
        return applicationDatabase.pennyDropDao()
    }
}
