package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case

import android.app.Application
import android.net.Uri
import com.google.gson.Gson
import org.json.JSONObject
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PlaceInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.CategoriesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.PlacesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.PlacesInTasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.SubtasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.TasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.getFileDataFromUri


class ImportDataUseCase(
    private val tasksUseCase: TasksUseCase,
    private val categoriesUseCase: CategoriesUseCase,
    private val subtasksUseCase: SubtasksUseCase,
    private val placesUseCase: PlacesUseCase,
    private val placesInTasksUseCase: PlacesInTasksUseCase,
    private val application: Application

) {
    suspend operator fun invoke(fileUri: Uri) {
        val gson: Gson = Gson()

        val tasks: MutableList<Task> = mutableListOf()
        val categories: MutableList<Category> = mutableListOf()
        val subtasks: MutableList<Subtask> = mutableListOf()
        val places: MutableList<Place> = mutableListOf()
        val placesInTasks: MutableList<PlaceInTask> = mutableListOf()

        val lastTasks: MutableList<Task> = mutableListOf()
        val lastPlaces: MutableList<Place> = mutableListOf()

        val importData = getFileDataFromUri(application, fileUri)

        var jsonObject: JSONObject = JSONObject(importData)
        try {
            //tasks
            val tasksJsonArray = jsonObject.getJSONArray("tasks")
            for (i in 0..<tasksJsonArray.length()) {
                val taskJSONObject = tasksJsonArray.getJSONObject(i)
                tasks.add(gson.fromJson(taskJSONObject.toString(), Task::class.java).copy(id = 0))
                lastTasks.add(gson.fromJson(taskJSONObject.toString(), Task::class.java))
            }
        } catch (e: Exception) {

        }

        try {
            jsonObject = JSONObject(importData)
            //categories
            val categoriesJsonArray = jsonObject.getJSONArray("categories")
            for (i in 0..<categoriesJsonArray.length()) {
                val categoryJSONObject = categoriesJsonArray.getJSONObject(i)
                categories.add(
                    gson.fromJson(categoryJSONObject.toString(), Category::class.java).copy(id = 0)
                )
            }
        } catch (e: Exception) {

        }

        try {
            jsonObject = JSONObject(importData)
            //subtasks
            val subtasksJsonArray = jsonObject.getJSONArray("subtasks")
            for (i in 0..<subtasksJsonArray.length()) {
                val subtaskJSONObject = subtasksJsonArray.getJSONObject(i)
                var subtask = gson.fromJson(subtaskJSONObject.toString(), Subtask::class.java)
                subtask = subtask.copy(
                    id = 0,
                    taskId = lastTasks.find { lastTask -> lastTask.id == subtask.taskId }?.id
                )
                subtasks.add(subtask)
            }
        } catch (e: Exception) {

        }

        try {
            jsonObject = JSONObject(importData)
            //places
            val placesJsonArray = jsonObject.getJSONArray("places")
            for (i in 0..<placesJsonArray.length()) {
                val placeJsonObject = placesJsonArray.getJSONObject(i)
                places.add(
                    gson.fromJson(placeJsonObject.toString(), Place::class.java).copy(id = 0)
                )
                lastPlaces.add(gson.fromJson(placeJsonObject.toString(), Place::class.java))
            }
        } catch (e: Exception) {
        }

        try {
            jsonObject = JSONObject(importData)
            //placesInTasks
            val placesInTasksJsonArray = jsonObject.getJSONArray("places_in_tasks")
            for (i in 0..<placesInTasksJsonArray.length()) {
                val placeInTasksJsonObject = placesInTasksJsonArray.getJSONObject(i)
                var placeInTask =
                    gson.fromJson(placeInTasksJsonObject.toString(), PlaceInTask::class.java)
                placeInTask = placeInTask.copy(
                    id = 0,
                    placeId = lastPlaces.find { lastPlace -> lastPlace.id == placeInTask.placeId }?.id,
                    taskId = lastTasks.find { lastTask -> lastTask.id == placeInTask.taskId }?.id
                )
                placesInTasks.add(
                    placeInTask
                )
            }
        } catch (e: Exception) {

        }

        tasksUseCase.insertTasksOperator(tasks)
        categoriesUseCase.insertCategoriesOperator(categories)
        subtasksUseCase.insertSubtasksOperator(subtasks)
        placesUseCase.insertPlacesOperator(places)
        placesInTasksUseCase.insertPlacesInTasksOperator(placesInTasks)
    }
}