package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.mappers

import androidx.compose.runtime.mutableStateOf
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Category
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Notification
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PeopleInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.PlaceInTask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.getPriorityByCode
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
        checked = checked ?: false,
        notify = notify ?: false,
        notifyDate = Date(notifyDateInMilliseconds ?: 0L),
        notifyHour = notifyHour ?: 0,
        notifyMinute = notifyMinute ?: 0,
        priority = getPriorityByCode(priorityCode ?: 0)
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
        checked = checked,
        notify = notify,
        notifyDateInMilliseconds = notifyDate.time,
        notifyHour = notifyHour,
        notifyMinute = notifyMinute,
        priorityCode = priority.code
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

fun Place.toDomain(): yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place {
    return yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place(
        id = id, title = title ?: "", la = la ?: 0.0, lt = lt ?: 0.0
    )
}

fun yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place.toData(): Place {
    return Place(id = id, title = title, la = la, lt = lt)
}

fun PlaceInTask.toDomain(): yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.PlaceInTask {
    return yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.PlaceInTask(
        id = id, placeId = placeId ?: 0, taskId = taskId ?: 0
    )
}

fun yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.PlaceInTask.toData(): PlaceInTask {
    return PlaceInTask(id = id, placeId = placeId, taskId = taskId)
}

fun People.toDomain(): yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People {
    return yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People(
        id = id,
        surname = surname ?: "",
        name = name ?: "",
        lastname = lastname ?: "",
        sex = sex ?: true,
        dateOfBirthInMilliseconds = dateOfBirthInMilliseconds,
        displayName = displayName ?: "",
        phone = phone ?: "",
        email = email ?: "",
        address = address ?: "",
        avatarPath = avatarPath ?: ""
    )
}

fun yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People.toData(): People {
    return People(
        id = id,
        surname = surname,
        name = name,
        lastname = lastname,
        sex = sex,
        dateOfBirthInMilliseconds = dateOfBirthInMilliseconds,
        displayName = displayName,
        phone = phone,
        email = email,
        address = address,
        avatarPath = avatarPath
    )
}

fun PeopleInTask.toDomain(): yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.PeopleInTask {
    return yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.PeopleInTask(
        id = id, peopleId = peopleId ?: 0, taskId = taskId ?: 0
    )
}

fun yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.PeopleInTask.toData(): PeopleInTask {
    return PeopleInTask(id = id, peopleId = peopleId, taskId = taskId)
}

fun Notification.toDomain(): yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Notification {
    return yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Notification(
        id = id, taskId = taskId ?: 0, tag = tag ?: "", isDone = isDone ?: false
    )
}

fun yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Notification.toData(): Notification {
    return Notification(id = id, taskId = taskId, tag = tag, isDone = isDone)
}