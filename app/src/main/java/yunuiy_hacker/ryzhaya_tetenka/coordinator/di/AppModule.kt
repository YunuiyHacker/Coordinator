package yunuiy_hacker.ryzhaya_tetenka.coordinator.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.CategoryDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.CoordinatorDatabase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.PlaceDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.PlaceInTaskDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.SubtaskDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.CategoriesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.DeleteCategoryOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.GetCategoriesOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.GetCategoryByIdOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.InsertCategoriesOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.InsertCategoryOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.UpdateCategoryOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.DeletePlaceOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.GetPlaceByIdOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.GetPlacesOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.InsertPlaceOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.InsertPlacesOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.PlacesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.UpdatePlaceOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.DeletePlaceInTaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.GetPlaceInTaskByTaskId
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.GetPlacesInTasksOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.InsertPlaceInTaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.InsertPlacesInTasksOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.PlacesInTasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.UpdatePlaceInTaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.DeleteSubtaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.GetSubtasksByTaskIdOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.GetSubtasksOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.InsertSubtaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.InsertSubtasksOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.SubtasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.UpdateSubtaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.DeleteTaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.GetTaskByIdOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.GetTasksByLikeQueryOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.GetTasksByTimeTypeIdDateAndCategoryIdOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.GetTasksOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.InsertTaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.InsertTasksOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.TasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.UpdateTaskOperator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.use_case.DefineTimeOfDayUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case.ExportDataUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case.ImportDataUseCase
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
            getTasksByTimeTypeIdDateAndCategoryIdOperator = GetTasksByTimeTypeIdDateAndCategoryIdOperator(
                taskDao
            ),
            getTasksOperator = GetTasksOperator(taskDao),
            insertTaskOperator = InsertTaskOperator(taskDao),
            insertTasksOperator = InsertTasksOperator(taskDao),
            updateTaskOperator = UpdateTaskOperator(taskDao)
        )
    }

    @Provides
    @Singleton
    fun provideCategoryDao(coordinatorDatabase: CoordinatorDatabase): CategoryDao =
        coordinatorDatabase.categoryDao

    @Provides
    @Singleton
    fun provideCategoriesUseCase(categoryDao: CategoryDao): CategoriesUseCase {
        return CategoriesUseCase(
            deleteCategoryOperator = DeleteCategoryOperator(categoryDao),
            getCategoriesOperator = GetCategoriesOperator(categoryDao),
            getCategoryByIdOperator = GetCategoryByIdOperator(categoryDao),
            insertCategoryOperator = InsertCategoryOperator(categoryDao),
            insertCategoriesOperator = InsertCategoriesOperator(categoryDao),
            updateCategoryOperator = UpdateCategoryOperator(categoryDao)
        )
    }

    @Provides
    @Singleton
    fun provideSubtaskDao(coordinatorDatabase: CoordinatorDatabase): SubtaskDao =
        coordinatorDatabase.subtaskDao

    @Provides
    @Singleton
    fun provideSubtasksUseCase(subtaskDao: SubtaskDao): SubtasksUseCase {
        return SubtasksUseCase(
            deleteSubtaskOperator = DeleteSubtaskOperator(subtaskDao),
            getSubtasksByTaskIdOperator = GetSubtasksByTaskIdOperator(subtaskDao),
            getSubtasksOperator = GetSubtasksOperator(subtaskDao),
            insertSubtaskOperator = InsertSubtaskOperator(subtaskDao),
            insertSubtasksOperator = InsertSubtasksOperator(subtaskDao),
            updateSubtaskOperator = UpdateSubtaskOperator(subtaskDao)
        )
    }

    @Provides
    @Singleton
    fun provideExportDataUseCase(
        tasksUseCase: TasksUseCase,
        categoriesUseCase: CategoriesUseCase,
        subtasksUseCase: SubtasksUseCase,
        placesUseCase: PlacesUseCase,
        placesInTasksUseCase: PlacesInTasksUseCase
    ): ExportDataUseCase = ExportDataUseCase(
        tasksUseCase,
        categoriesUseCase,
        subtasksUseCase,
        placesUseCase,
        placesInTasksUseCase
    )

    @Provides
    @Singleton
    fun provideImportDataUseCase(
        tasksUseCase: TasksUseCase,
        categoriesUseCase: CategoriesUseCase,
        subtasksUseCase: SubtasksUseCase,
        placesUseCase: PlacesUseCase,
        placesInTasksUseCase: PlacesInTasksUseCase,
        application: Application
    ): ImportDataUseCase = ImportDataUseCase(
        tasksUseCase,
        categoriesUseCase,
        subtasksUseCase,
        placesUseCase,
        placesInTasksUseCase,
        application
    )

    @Provides
    @Singleton
    fun providePlaceDao(coordinatorDatabase: CoordinatorDatabase): PlaceDao =
        coordinatorDatabase.placeDao

    @Provides
    @Singleton
    fun providePlacesUseCase(placeDao: PlaceDao): PlacesUseCase {
        return PlacesUseCase(
            deletePlaceOperator = DeletePlaceOperator(placeDao),
            getPlacesOperator = GetPlacesOperator(placeDao),
            getPlaceByIdOperator = GetPlaceByIdOperator(placeDao),
            updatePlaceOperator = UpdatePlaceOperator(placeDao),
            insertPlaceOperator = InsertPlaceOperator(placeDao),
            insertPlacesOperator = InsertPlacesOperator(placeDao)
        )
    }

    @Provides
    @Singleton
    fun providePlaceInTaskDao(coordinatorDatabase: CoordinatorDatabase): PlaceInTaskDao =
        coordinatorDatabase.placeInTaskDao

    @Provides
    @Singleton
    fun providePlacesInTasksUseCase(placeInTaskDao: PlaceInTaskDao): PlacesInTasksUseCase {
        return PlacesInTasksUseCase(
            deletePlaceInTaskOperator = DeletePlaceInTaskOperator(placeInTaskDao),
            getPlacesInTasksOperator = GetPlacesInTasksOperator(placeInTaskDao),
            getPlacesInTaskByTaskId = GetPlaceInTaskByTaskId(placeInTaskDao),
            updatePlaceInTaskOperator = UpdatePlaceInTaskOperator(placeInTaskDao),
            insertPlaceInTaskOperator = InsertPlaceInTaskOperator(placeInTaskDao),
            insertPlacesInTasksOperator = InsertPlacesInTasksOperator(placeInTaskDao)
        )
    }
}