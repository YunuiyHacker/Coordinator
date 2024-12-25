package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeOfDay
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@Composable
fun TopTimeOfDayText(modifier: Modifier = Modifier, timeOfDay: TimeOfDay, userName: String) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = when (timeOfDay) {
                TimeOfDay.MORNING -> stringResource(R.string.good_morning)
                TimeOfDay.AFTERNOON -> stringResource(R.string.good_afternoon)
                TimeOfDay.EVENING -> stringResource(R.string.good_evening)
                TimeOfDay.NIGHT -> stringResource(R.string.good_night)
            },
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontFamily = caros, fontWeight = FontWeight.Normal
        )
        Text(
            text = ", ",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontFamily = caros,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = userName, color = MaterialTheme.colorScheme.onSurface, fontFamily = caros,
            fontWeight = FontWeight.Medium
        )
        Text(text = " 👋️", fontSize = 20.sp)
    }
}