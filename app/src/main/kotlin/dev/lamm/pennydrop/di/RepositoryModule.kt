package dev.lamm.pennydrop.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.lamm.pennydrop.data.PennyDropDao
import dev.lamm.pennydrop.data.PennyDropRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providePennyDropRepository(pennyDropDao: PennyDropDao): PennyDropRepository {
        return PennyDropRepository(pennyDropDao)
    }
}
