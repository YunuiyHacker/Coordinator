package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case

import com.google.gson.Gson
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.CategoryDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.SubtaskDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao

class ExportDataUseCase(
    private val tasksDao: TaskDao,
    private val categoryDao: CategoryDao,
    private val subtaskDao: SubtaskDao
) {
    suspend operator fun invoke(): String {
        val exportingData: StringBuffer = StringBuffer()
        val tasks = tasksDao.getTasks()
        val categories = categoryDao.getCategories()
        val subtasks = subtaskDao.getSubtasks()

        //tasks
        exportingData.append("{\"tasks\":[")
        tasks.forEach { task ->
            exportingData.append(Gson().toJson(task))
            if (tasks.last() != task) exportingData.append(",\n")
        }
        exportingData.append("],\n")

        //categories
        exportingData.append("\"categories\":[")
        categories.forEach { category ->
            exportingData.append(Gson().toJson(category))
            if (categories.last() != category) exportingData.append(",\n")
        }
        exportingData.append("],\n")

        //subtasks
        exportingData.append("\"subtasks\":[")
        subtasks.forEach { subtask ->
            exportingData.append(Gson().toJson(subtask))
            if (subtasks.last() != subtask) exportingData.append(",\n")
        }
        exportingData.append("]}")

        return exportingData.toString()
    }
}