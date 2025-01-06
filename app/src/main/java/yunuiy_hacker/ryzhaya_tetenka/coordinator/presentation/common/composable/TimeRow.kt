package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeType
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.DateFormats
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.timeFormatter
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toTimeTypeEvent
import java.util.Date

@Composable
fun TimeRow(
    modifier: Modifier = Modifier,
    timeType: TimeType,
    date: Date,
    weekDate: Pair<Date, Date>,
    hour: Int,
    minute: Int,
    withEndTime: Boolean,
    endHour: Int,
    endMinute: Int,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onEndTimeClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(modifier = modifier.animateContentSize()) {
        Row(
            modifier = Modifier.border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            Row(modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable(interactionSource = interactionSource, indication = null) {
                    onDateClick()
                }
                .animateContentSize(), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Rounded.CalendarMonth,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (timeType.toTimeTypeEvent()) {
                        TimeTypeEnum.DAY -> DateFormats.DayTimeTypeOutputFormat.format(
                            date
                        )

                        TimeTypeEnum.WEEK -> "${
                            DateFormats.WeekTimeTypeOutputFormatFirstPart.format(
                                (weekDate as Pair<*, *>).first
                            )
                        } - ${
                            DateFormats.WeekTimeTypeOutputFormatSecondPart.format(
                                (weekDate).second
                            )
                        }"

                        TimeTypeEnum.MONTH -> DateFormats.MonthTimeTypeOutputFormat.format(
                            date
                        )

                        TimeTypeEnum.YEAR -> DateFormats.YearTimeTypeOutputFormat.format(
                            date
                        )

                        TimeTypeEnum.LIFE -> stringResource(R.string.life).lowercase()
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = caros,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        if (timeType.toTimeTypeEvent() == TimeTypeEnum.DAY) {
            Row(modifier = Modifier
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable(interactionSource = interactionSource, indication = null) {
                    onTimeClick()
                }
                .animateContentSize()) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Rounded.AccessTime,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = timeFormatter(hour, minute),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = caros,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                }
            }
            AnimatedVisibility(withEndTime) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "-",
                        fontFamily = caros,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(modifier = Modifier
                        .border(
                            width = 0.5.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable(interactionSource = interactionSource, indication = null) {
                            onEndTimeClick()
                        }
                        .animateContentSize()) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Rounded.AccessTime,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = timeFormatter(endHour, endMinute),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}