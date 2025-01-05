package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toTimeTypeEvent
import java.util.Date

class GetTasksByTimeTypeIdAndDateOperator(private val taskDao: TaskDao) {
    suspend operator fun invoke(
        timeTypeId: Int, dateInMilliseconds: Long, dates: Pair<Date, Date> = Pair(Date(), Date())
    ): List<Task> {
        return when (Constants.timeTypes.find { it.id == timeTypeId }?.toTimeTypeEvent()) {
            TimeTypeEnum.DAY -> taskDao.getTasksByDate(dateInMilliseconds)
            TimeTypeEnum.WEEK -> {
                taskDao.getTasksByWeek(dates.first.time, dates.second.time
                )
            }

            TimeTypeEnum.MONTH -> taskDao.getTasksByMonth(dateInMilliseconds)
            TimeTypeEnum.YEAR -> taskDao.getTasksByYear(dateInMilliseconds)
            TimeTypeEnum.LIFE -> taskDao.getTasksByLife()
            null -> {
                taskDao.getTasks()
            }
        }
    }
}