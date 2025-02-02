package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Subtask
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@Composable
fun SubtaskRow(
    modifier: Modifier = Modifier,
    subtask: Subtask,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: (subtask: Subtask) -> Unit,
    isEditingEnabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    var localTitle by remember { mutableStateOf(subtask.title) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = subtask.checked.value, onCheckedChange = {
                onCheckedChange(!subtask.checked.value)
            }, colors = CheckboxDefaults.colors(
                uncheckedColor = MaterialTheme.colorScheme.onSurface,
                checkedColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            modifier = Modifier
                .weight(1f),
            text = localTitle,
            fontFamily = caros,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            textAlign = TextAlign.Start
        )
        if (isEditingEnabled) {
            Icon(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource, indication = null
                ) {
                    onDeleteClick(subtask)
                },
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CreateUpdateSubtaskRow(
    modifier: Modifier = Modifier,
    subtask: Subtask,
    onCheckedChange: (Boolean) -> Unit,
    onTitleChange: (String) -> Unit,
    onDeleteClick: (subtask: Subtask) -> Unit,
    isEditingEnabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    var localTitle by remember { mutableStateOf(subtask.title) }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = subtask.checked.value, onCheckedChange = {
                onCheckedChange(!subtask.checked.value)
            }, colors = CheckboxDefaults.colors(
                uncheckedColor = MaterialTheme.colorScheme.onSurface,
                checkedColor = MaterialTheme.colorScheme.primary
            )
        )
        TextField(modifier = Modifier
            .weight(1f)
            .animateContentSize(),
            value = localTitle,
            onValueChange = {
                if (isEditingEnabled) {
                    localTitle = it
                    onTitleChange(localTitle)
                }
            },
            readOnly = !isEditingEnabled,
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.background
            ),
            textStyle = TextStyle(
                fontFamily = caros,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            ),
            placeholder = {
                Text(
                    text = stringResource(R.string.subtask_title),
                    fontFamily = caros,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            })
        if (isEditingEnabled) {
            Icon(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource, indication = null
                ) {
                    onDeleteClick(subtask)
                },
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}