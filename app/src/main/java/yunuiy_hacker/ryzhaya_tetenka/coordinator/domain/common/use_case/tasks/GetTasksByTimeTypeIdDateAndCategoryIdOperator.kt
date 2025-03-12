package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.use_case.tasks

import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.room.TaskDao
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.toTimeTypeEvent
import java.util.Date

class GetTasksByTimeTypeIdDateAndCategoryIdOperator(private val taskDao: TaskDao) {
    suspend operator fun invoke(
        timeTypeId: Int,
        dateInMilliseconds: Long,
        dates: Pair<Date, Date> = Pair(Date(), Date()),
        categoryId: Int
    ): List<Task> {
        return when (Constants.timeTypes.find { it.id == timeTypeId }?.toTimeTypeEvent()) {
            TimeTypeEnum.DAY -> if (categoryId == 0) taskDao.getTasksByDate(dateInMilliseconds) else taskDao.getTasksByDateAndCategoryId(
                dateInMilliseconds, categoryId
            )

            TimeTypeEnum.WEEK -> {
                if (categoryId == 0) taskDao.getTasksByWeek(
                    dates.first.time, dates.second.time
                )
                else taskDao.getTasksByWeekAndCategoryId(
                    dates.first.time, dates.second.time, categoryId
                )
            }

            TimeTypeEnum.MONTH -> if (categoryId == 0) taskDao.getTasksByMonth(dateInMilliseconds) else taskDao.getTasksByMonthAndCategoryId(
                dateInMilliseconds,
                categoryId
            )

            TimeTypeEnum.YEAR -> if (categoryId == 0) taskDao.getTasksByYear(dateInMilliseconds) else taskDao.getTasksByYearAndCategoryId(
                dateInMilliseconds,
                categoryId
            )

            TimeTypeEnum.LIFE -> if (categoryId == 0) taskDao.getTasksByLife() else taskDao.getTasksByLifeAndCategoryId(
                categoryId
            )

            null -> {
                taskDao.getTasks()
            }
        }
    }
}