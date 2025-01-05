package yunuiy_hacker.ryzhaya_tetenka.coordinator.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.CoordinatorDatabase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.DeleteTaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.GetTaskByIdOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.GetTasksByLikeQueryOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.GetTasksByTimeTypeIdAndDateOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.GetTasksOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.InsertTaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.TasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.UpdateTaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.use_case.DefineTimeOfDayUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants.TASKS_DATABASE_NAME
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

    @Provides
    @Singleton
    fun provideCoordinatorDatabase(application: Application): CoordinatorDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = CoordinatorDatabase::class.java,
            name = TASKS_DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideTasksDao(coordinatorDatabase: CoordinatorDatabase): TaskDao =
        coordinatorDatabase.taskDao

    @Provides
    @Singleton
    fun provideTasksUseCase(taskDao: TaskDao): TasksUseCase {
        return TasksUseCase(
            deleteTaskOperator = DeleteTaskOperator(taskDao),
            getTaskByIdOperator = GetTaskByIdOperator(taskDao),
            getTasksByLikeQueryOperator = GetTasksByLikeQueryOperator(taskDao),
            getTasksByTimeTypeIdAndDateOperator = GetTasksByTimeTypeIdAndDateOperator(taskDao),
            getTasksOperator = GetTasksOperator(taskDao),
            insertTaskOperator = InsertTaskOperator(taskDao),
            updateTaskOperator = UpdateTaskOperator(taskDao)
        )
    }
}