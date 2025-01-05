package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.roboto
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.timeFormatter
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toTimeTypeEvent

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
    task: Task,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onClick()
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.checked.value, onCheckedChange = {
                    onCheckedChange(!task.checked.value)
                }, colors = CheckboxDefaults.colors(
                    uncheckedColor = MaterialTheme.colorScheme.onSurface,
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                if (task.title.isNotEmpty()) Text(
                    text = task.title,
                    fontFamily = caros,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (task.checked.value) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.checked.value) TextDecoration.LineThrough else TextDecoration.None
                )
                if (task.title.isNotEmpty() && task.content.isNotEmpty()) {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                }
                if (task.content.isNotEmpty()) {
                    Text(
                        text = task.content,
                        fontFamily = caros,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (task.checked.value) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (task.checked.value) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
            if (Constants.timeTypes.find { timeType -> timeType.id == task.timeTypeId }
                    ?.toTimeTypeEvent() == TimeTypeEnum.DAY) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = timeFormatter(task.hour, task.minute),
                    color = if (task.checked.value) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}