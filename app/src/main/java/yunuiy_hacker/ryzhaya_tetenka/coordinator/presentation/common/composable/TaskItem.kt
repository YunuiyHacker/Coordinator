package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Task
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.roboto
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.timeFormatter
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toTimeTypeEvent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
    task: Task,
    onClick: () -> Unit,
    isDeletionMode: Boolean,
    onLongClick: () -> Unit,
    isDeleteChecked: Boolean,
    onDeleteCheckedChange: (Boolean) -> Unit,
    onCheckedSubtask: (Subtask) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val localIndication = LocalIndication.current

    var showSubtasks by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .combinedClickable(interactionSource = interactionSource,
                indication = localIndication,
                onLongClick = {
                    onLongClick()
                },
                onDoubleClick = {},
                onClick = {
                    if (!isDeletionMode)
                        onClick()
                }), verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(!isDeletionMode) {
                    Checkbox(
                        checked = task.checked.value, onCheckedChange = {
                            onCheckedChange(!task.checked.value)
                        }, colors = CheckboxDefaults.colors(
                            uncheckedColor = MaterialTheme.colorScheme.onSurface,
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
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
                            textDecoration = if (task.checked.value) TextDecoration.LineThrough else TextDecoration.None,
                            lineHeight = 16.sp
                        )
                    }
                }
                if (Constants.timeTypes.find { timeType -> timeType.id == task.timeTypeId }
                        ?.toTimeTypeEvent() == TimeTypeEnum.DAY) {
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = if (!task.withEndTime) timeFormatter(
                            task.hour,
                            task.minute
                        ) else "${timeFormatter(task.hour, task.minute)} - ${
                            timeFormatter(
                                task.endHour,
                                task.endMinute
                            )
                        }",
                        color = if (task.checked.value) Color.Gray else MaterialTheme.colorScheme.onSurface,
                        fontFamily = roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                AnimatedVisibility(isDeletionMode) {
                    Checkbox(
                        checked = isDeleteChecked, onCheckedChange = {
                            onDeleteCheckedChange(!isDeleteChecked)
                        }, colors = CheckboxDefaults.colors(
                            uncheckedColor = MaterialTheme.colorScheme.onSurface,
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
            if (task.subtasks.isNotEmpty()) {
                AnimatedVisibility(showSubtasks) {
                    Column {
                        Spacer(modifier = Modifier.height(6.dp))
                        task.subtasks.forEach { subtask ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 40.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    modifier = Modifier.scale(0.7f),
                                    checked = subtask.checked.value, onCheckedChange = {
                                        subtask.checked.value = !subtask.checked.value
                                        onCheckedSubtask(subtask)
                                    }, colors = CheckboxDefaults.colors(
                                        uncheckedColor = MaterialTheme.colorScheme.onSurface,
                                        checkedColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Text(
                                    text = subtask.title,
                                    fontFamily = caros,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = if (subtask.checked.value) Color.Gray else MaterialTheme.colorScheme.onSurface,
                                    overflow = TextOverflow.Ellipsis,
                                    textDecoration = if (subtask.checked.value) TextDecoration.LineThrough else TextDecoration.None
                                )
                            }
                        }
                    }
                }
                Icon(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clip(CircleShape)
                        .clickable {
                            showSubtasks = !showSubtasks
                        },
                    imageVector = if (showSubtasks) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}