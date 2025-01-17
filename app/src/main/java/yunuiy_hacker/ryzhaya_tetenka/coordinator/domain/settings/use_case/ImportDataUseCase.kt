package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case

import android.app.Application
import android.net.Uri
import com.google.gson.Gson
import org.json.JSONObject
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.CategoryDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.SubtaskDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.getFileDataFromUri


class ImportDataUseCase(
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao,
    private val subtaskDao: SubtaskDao,
    private val application: Application

) {
    suspend operator fun invoke(fileUri: Uri) {
        val gson: Gson = Gson()

        val tasks: MutableList<Task> = mutableListOf()
        val categories: MutableList<Category> = mutableListOf()
        val subtasks: MutableList<Subtask> = mutableListOf()

        val lastTasks: MutableList<Task> = mutableListOf()

        val importData = getFileDataFromUri(application, fileUri)

        var jsonObject: JSONObject = JSONObject(importData)

        //tasks
        val tasksJsonArray = jsonObject.getJSONArray("tasks")
        for (i in 0..<tasksJsonArray.length()) {
            val taskJSONObject = tasksJsonArray.getJSONObject(i)
            tasks.add(gson.fromJson(taskJSONObject.toString(), Task::class.java).copy(id = 0))
            lastTasks.add(gson.fromJson(taskJSONObject.toString(), Task::class.java))
        }

        jsonObject = JSONObject(importData)

        //categories
        val categoriesJsonArray = jsonObject.getJSONArray("categories")
        for (i in 0..<categoriesJsonArray.length()) {
            val categoriesJSONObject = categoriesJsonArray.getJSONObject(i)
            categories.add(
                gson.fromJson(categoriesJSONObject.toString(), Category::class.java).copy(id = 0)
            )
        }

        //subtasks
        val subtasksJsonArray = jsonObject.getJSONArray("subtasks")
        for (i in 0..<subtasksJsonArray.length()) {
            val subtasksJSONObject = subtasksJsonArray.getJSONObject(i)
            var subtask = gson.fromJson(subtasksJSONObject.toString(), Subtask::class.java)
            subtask = subtask.copy(
                id = 0,
                taskId = lastTasks.find { lastTask -> lastTask.id == subtask.taskId }?.id
            )
            subtasks.add(subtask)
        }

        taskDao.allUpsert(tasks)
        categoryDao.allUpsert(categories)
        subtaskDao.allUpsert(subtasks)
    }
}