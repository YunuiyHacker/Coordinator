package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers

import androidx.compose.runtime.mutableStateOf
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import java.util.Date

fun Task.toDomain(): yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task {
    return yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task(
        id = id,
        timeTypeId = timeTypeId ?: 0,
        date = Date(dateInMilliseconds!!),
        hour = hour ?: 0,
        minute = minute ?: 0,
        title = title ?: "",
        content = content ?: "",
        checked = mutableStateOf(checked ?: false)
    )
}

fun yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task.toData(): Task {
    return Task(
        id = id,
        timeTypeId = timeTypeId,
        dateInMilliseconds = date.time,
        hour = hour,
        minute = minute,
        title = title,
        content = content,
        checked = checked.value
    )
}