package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@Composable
fun TimeTypeSelectRow(
    modifier: Modifier = Modifier, timeTypes: List<TimeType> = listOf(
        TimeType(id = 0, resId = R.string.day),
        TimeType(id = 1, resId = R.string.week),
        TimeType(id = 2, resId = R.string.month),
        TimeType(id = 3, resId = R.string.year),
        TimeType(id = 4, resId = R.string.life)
    ),
    selectedTimeType: TimeType = timeTypes[0],
    onChangeSelectedTimeType: (timeType: TimeType) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .border(width = 0.3.dp, color = Color.DarkGray, shape = RoundedCornerShape(10.dp))
    ) {
        timeTypes.forEach { timeType ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = if (timeType.id != selectedTimeType.id) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(if (timeType.id != selectedTimeType.id) 0.dp else 10.dp)
                    )
                    .clickable(interactionSource = interactionSource, indication = null) {
                        onChangeSelectedTimeType(timeType)
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(timeType.resId),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = caros,
                    fontWeight = if (timeType.id == selectedTimeType.id) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}