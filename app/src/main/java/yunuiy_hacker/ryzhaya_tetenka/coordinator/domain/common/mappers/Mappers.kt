package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers

import androidx.compose.runtime.mutableStateOf
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import java.util.Date

fun Task.toDomain(): yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task {
    return yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task(
        id = id,
        timeTypeId = timeTypeId ?: 0,
        categoryId = categoryId ?: 0,
        date = Date(dateInMilliseconds!!),
        hour = hour ?: 0,
        minute = minute ?: 0,
        withEndTime = withEndTime ?: false,
        endHour = endHour ?: 0,
        endMinute = endMinute ?: 0,
        title = title ?: "",
        content = content ?: "",
        checked = mutableStateOf(checked ?: false)
    )
}

fun yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task.toData(): Task {
    return Task(
        id = id,
        timeTypeId = timeTypeId,
        categoryId = categoryId,
        dateInMilliseconds = date.time,
        hour = hour,
        minute = minute,
        withEndTime = withEndTime,
        endHour = endHour,
        endMinute = endMinute,
        title = title,
        content = content,
        checked = checked.value
    )
}

fun Category.toDomain(): yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Category {
    return yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Category(
        id = id, title = title ?: ""
    )
}

fun yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Category.toData(): Category {
    return Category(id = id, title = title)
}

fun Subtask.toDomain(): yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask {
    return yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask(
        id = id,
        taskId = taskId ?: 0,
        title = title ?: "",
        checked = mutableStateOf(checked ?: false),
        index = index ?: 0
    )
}

fun yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask.toData(): Subtask {
    return Subtask(id = id, taskId = taskId, title = title, checked = checked.value, index = 0)
}