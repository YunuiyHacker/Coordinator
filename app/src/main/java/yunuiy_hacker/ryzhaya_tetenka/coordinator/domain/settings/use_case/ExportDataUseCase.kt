package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case

import com.google.gson.Gson
import org.json.JSONArray
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.CategoryDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.SubtaskDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.CategoriesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.PlacesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.PlacesInTasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.SubtasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.TasksUseCase

class ExportDataUseCase(
    private val tasksUseCase: TasksUseCase,
    private val categoriesUseCase: CategoriesUseCase,
    private val subtasksUseCase: SubtasksUseCase,
    private val placesUseCase: PlacesUseCase,
    private val placesInTasksUseCase: PlacesInTasksUseCase
) {
    suspend operator fun invoke(): String {
        val gson = Gson()

        val exportingData: StringBuffer = StringBuffer()
        val tasks = tasksUseCase.getTasksOperator()
        val categories = categoriesUseCase.getCategoriesOperator()
        val subtasks = subtasksUseCase.getSubtasksOperator()
        val places = placesUseCase.getPlacesOperator()
        val placesInTasks = placesInTasksUseCase.getPlacesInTasksOperator()

        //tasks
        exportingData.append("{\"tasks\":")
        exportingData.append(gson.toJson(tasks))
        exportingData.append(",\n")

        //categories
        exportingData.append("\"categories\":")
        exportingData.append(gson.toJson(categories))
        exportingData.append(",\n")

        //subtasks
        exportingData.append("\"subtasks\":")
        exportingData.append(gson.toJson(subtasks))
        exportingData.append(",\n")

        //places
        exportingData.append("\"places\":")
        exportingData.append(gson.toJson(places))
        exportingData.append(",\n")

        //places_in_tasks
        exportingData.append("\"places_in_tasks\":")
        exportingData.append(gson.toJson(placesInTasks))

        exportingData.append("}")

        return exportingData.toString()
    }
}