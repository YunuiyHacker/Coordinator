package yunuiy_hacker.ryzhaya_tetenka.coordinator.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.use_case.DefineTimeOfDayUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDefineTimeOfDayUseCase(): DefineTimeOfDayUseCase = DefineTimeOfDayUseCase()

    @Provides
    @Singleton
    fun provideSharedPrefsHelper(@ApplicationContext context: Context): SharedPrefsHelper =
        SharedPrefsHelper(context)
}