package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case

import android.app.Application
import com.google.gson.Gson
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.CategoriesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples.PeoplesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples_in_tasks.PeoplesInTasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.PlacesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.PlacesInTasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.SubtasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.TasksUseCase

class ExportDataUseCase(
    private val tasksUseCase: TasksUseCase,
    private val categoriesUseCase: CategoriesUseCase,
    private val subtasksUseCase: SubtasksUseCase,
    private val placesUseCase: PlacesUseCase,
    private val placesInTasksUseCase: PlacesInTasksUseCase,
    private val peoplesUseCase: PeoplesUseCase,
    private val peoplesInTasksUseCase: PeoplesInTasksUseCase,
    private val application: Application
) {
    suspend operator fun invoke(): String {
        val gson = Gson()

        val exportingData: StringBuffer = StringBuffer()
        val tasks = tasksUseCase.getTasksOperator()
        val categories = categoriesUseCase.getCategoriesOperator()
        val subtasks = subtasksUseCase.getSubtasksOperator()
        val places = placesUseCase.getPlacesOperator()
        val placesInTasks = placesInTasksUseCase.getPlacesInTasksOperator()
        val peoples = peoplesUseCase.getPeoplesOperator()
        val peoplesInTasks = peoplesInTasksUseCase.getPeoplesInTasksOperator()

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
        exportingData.append(",\n")

        //peoples
        exportingData.append("\"peoples\":")
        exportingData.append(gson.toJson(peoples))
        exportingData.append(",\n")

        //peoples_in_tasks
        exportingData.append("\"peoples_in_tasks\":")
        exportingData.append(gson.toJson(peoplesInTasks))

        exportingData.append("}")

        return exportingData.toString()
    }
}