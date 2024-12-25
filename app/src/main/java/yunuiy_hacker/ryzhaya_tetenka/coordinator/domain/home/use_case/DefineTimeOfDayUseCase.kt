package yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.use_case

import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeOfDay
import java.util.Calendar
import java.util.GregorianCalendar

class DefineTimeOfDayUseCase {

    fun execute(): TimeOfDay {
        val calendar: Calendar = GregorianCalendar()
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)

        if ((hour >= 4 && minute >= 0) && (hour <= 11 && minute <= 59))
            return TimeOfDay.MORNING
        else if ((hour >= 12 && minute >= 0) && (hour <= 15 && minute <= 59))
            return TimeOfDay.AFTERNOON
        else if ((hour >= 16 && minute >= 0) && (hour <= 22 && minute <= 59))
            return TimeOfDay.EVENING
        return TimeOfDay.NIGHT
    }
}