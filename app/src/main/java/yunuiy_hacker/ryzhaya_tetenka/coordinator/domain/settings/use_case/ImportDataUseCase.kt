package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case

import android.app.Application
import android.net.Uri
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PlaceInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PeopleInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.categories.CategoriesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples.PeoplesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.peoples_in_tasks.PeoplesInTasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places.PlacesUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.places_in_tasks.PlacesInTasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.subtasks.SubtasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks.TasksUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.ImageUtils
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipInputStream


class ImportDataUseCase(
    private val tasksUseCase: TasksUseCase,
    private val categoriesUseCase: CategoriesUseCase,
    private val subtasksUseCase: SubtasksUseCase,
    private val placesUseCase: PlacesUseCase,
    private val placesInTasksUseCase: PlacesInTasksUseCase,
    private val peoplesUseCase: PeoplesUseCase,
    private val peoplesInTasksUseCase: PeoplesInTasksUseCase,
    private val application: Application

) {
    @OptIn(DelicateCoroutinesApi::class)
    suspend operator fun invoke(fileUri: Uri) {
        withContext(Dispatchers.IO) {
            ZipInputStream(
                application.contentResolver.openInputStream(fileUri)
            ).use { input ->
                if (Files.notExists(Paths.get(ImageUtils.IMG_DIR))) Files.createDirectory(
                    Paths.get(
                        ImageUtils.IMG_DIR
                    )
                )
                generateSequence { input.nextEntry }.filterNot { it.isDirectory }.forEach {
                    if (!it.name.endsWith("json")) {
                        val file = File(File(ImageUtils.IMG_DIR), it.name)
                        try {
                            val fos = FileOutputStream(file)
                            fos.write(input.readAllBytes())
                            fos.flush()
                        } catch (e: Exception) {

                        }
                    } else {
                        val importData = input.readAllBytes().decodeToString()

                        val gson: Gson = Gson()

                        val tasks: MutableList<Task> = mutableListOf()
                        val categories: MutableList<Category> = mutableListOf()
                        val subtasks: MutableList<Subtask> = mutableListOf()
                        val places: MutableList<Place> = mutableListOf()
                        val placesInTasks: MutableList<PlaceInTask> = mutableListOf()
                        val peoples: MutableList<People> = mutableListOf()
                        val peoplesInTasks: MutableList<PeopleInTask> = mutableListOf()

                        val lastTasks: MutableList<Task> = mutableListOf()
                        val lastPlaces: MutableList<Place> = mutableListOf()
                        val lastPeoples: MutableList<People> = mutableListOf()

                        val jsonObject: JSONObject = JSONObject(importData)
                        try {
                            //tasks
                            val tasksJsonArray = jsonObject.getJSONArray("tasks")
                            for (i in 0..<tasksJsonArray.length()) {
                                val taskJSONObject = tasksJsonArray.getJSONObject(i)
                                tasks.add(
                                    gson.fromJson(
                                        taskJSONObject.toString(), Task::class.java
                                    ).copy(id = 0)
                                )
                                lastTasks.add(
                                    gson.fromJson(
                                        taskJSONObject.toString(), Task::class.java
                                    )
                                )
                            }
                        } catch (e: Exception) {

                        }
                        //categories
                        val categoriesJsonArray = jsonObject.getJSONArray("categories")
                        for (i in 0..<categoriesJsonArray.length()) {
                            val categoryJSONObject = categoriesJsonArray.getJSONObject(i)
                            categories.add(
                                gson.fromJson(
                                    categoryJSONObject.toString(), Category::class.java
                                ).copy(id = 0)
                            )
                        }

                        try {
                            //subtasks
                            val subtasksJsonArray = jsonObject.getJSONArray("subtasks")
                            for (i in 0..<subtasksJsonArray.length()) {
                                val subtaskJSONObject = subtasksJsonArray.getJSONObject(i)
                                var subtask = gson.fromJson(
                                    subtaskJSONObject.toString(), Subtask::class.java
                                )
                                subtask = subtask.copy(
                                    id = 0,
                                    taskId = lastTasks.find { lastTask -> lastTask.id == subtask.taskId }?.id
                                )
                                subtasks.add(subtask)
                            }
                        } catch (e: Exception) {

                        }

                        try {
                            //places
                            val placesJsonArray = jsonObject.getJSONArray("places")
                            for (i in 0..<placesJsonArray.length()) {
                                val placeJsonObject = placesJsonArray.getJSONObject(i)
                                places.add(
                                    gson.fromJson(placeJsonObject.toString(), Place::class.java)
                                        .copy(id = 0)
                                )
                                lastPlaces.add(
                                    gson.fromJson(
                                        placeJsonObject.toString(), Place::class.java
                                    )
                                )
                            }
                        } catch (e: Exception) {
                        }

                        try {
                            //placesInTasks
                            val placesInTasksJsonArray = jsonObject.getJSONArray("places_in_tasks")
                            for (i in 0..<placesInTasksJsonArray.length()) {
                                val placeInTasksJsonObject = placesInTasksJsonArray.getJSONObject(i)
                                var placeInTask = gson.fromJson(
                                    placeInTasksJsonObject.toString(), PlaceInTask::class.java
                                )
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

                        try {
                            //peoples
                            val peoplesJsonArray = jsonObject.getJSONArray("peoples")
                            for (i in 0..<peoplesJsonArray.length()) {
                                val peopleJsonObject = peoplesJsonArray.getJSONObject(i)
                                peoples.add(
                                    gson.fromJson(
                                        peopleJsonObject.toString(), People::class.java
                                    ).copy(id = 0)
                                )
                                lastPeoples.add(
                                    gson.fromJson(
                                        peopleJsonObject.toString(), People::class.java
                                    )
                                )
                            }
                        } catch (e: Exception) {
                        }

                        try {
                            //peoples_in_tasks
                            val peoplesInTasksJsonArray =
                                jsonObject.getJSONArray("peoples_in_tasks")
                            for (i in 0..<peoplesInTasksJsonArray.length()) {
                                val peopleInTasksJsonObject =
                                    peoplesInTasksJsonArray.getJSONObject(i)
                                var peopleInTask = gson.fromJson(
                                    peopleInTasksJsonObject.toString(), PeopleInTask::class.java
                                )
                                peopleInTask = peopleInTask.copy(
                                    id = 0,
                                    peopleId = lastPeoples.find { lastPeople -> lastPeople.id == peopleInTask.peopleId }?.id,
                                    taskId = lastTasks.find { lastTask -> lastTask.id == peopleInTask.taskId }?.id
                                )
                                peoplesInTasks.add(
                                    peopleInTask
                                )
                            }
                        } catch (e: Exception) {
                        }

                        GlobalScope.launch(Dispatchers.IO) {
                            runBlocking {
                                tasksUseCase.insertTasksOperator(tasks)
                                categoriesUseCase.insertCategoriesOperator(categories)
                                subtasksUseCase.insertSubtasksOperator(subtasks)
                                placesUseCase.insertPlacesOperator(places)
                                placesInTasksUseCase.insertPlacesInTasksOperator(placesInTasks)
                                peoplesUseCase.insertPeoplesOperator(peoples)
                                peoplesInTasksUseCase.insertPeoplesInTasksOperator(
                                    peoplesInTasks
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}