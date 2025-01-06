package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.task

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.MenuItem
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.QuestionDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.TimeRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navHostController: NavHostController, viewModel: TaskViewModel = hiltViewModel()) {
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        viewModel.onEvent(TaskEvent.LoadDataEvent)
    }

    viewModel.state.let { state ->
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(
                            R.string.task
                        ),
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    Row {
                        Spacer(modifier = Modifier.width(24.dp))
                        Box(modifier = Modifier.clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            navHostController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        }
                    }
                },
                actions = {
                    Row {
                        Box(modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = interactionSource, indication = null
                            ) {
                                viewModel.onEvent(TaskEvent.ShowTaskMenuEvent)
                            }) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                    }

                    MaterialTheme(
                        colorScheme = MaterialTheme.colorScheme.copy(surface = MaterialTheme.colorScheme.surfaceVariant),
                        shapes = MaterialTheme.shapes.copy(extraSmall = ShapeDefaults.Medium)
                    ) {
                        DropdownMenu(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = 0.3.dp,
                                    color = Color.DarkGray,
                                    shape = RoundedCornerShape(12.dp)
                                ), expanded = state.showTaskMenu, onDismissRequest = {
                                viewModel.onEvent(TaskEvent.HideTaskMenuEvent)
                            }, offset = DpOffset(x = -12.dp, y = 0.dp)
                        ) {
                            DropdownMenuItem(text = {
                                MenuItem(
                                    icon = Icons.Rounded.Edit, title = R.string.edit
                                )
                            }, onClick = {
                                viewModel.onEvent(TaskEvent.HideTaskMenuEvent)
                                navHostController.navigate("${Route.CreateUpdateTaskScreen.route}/${state.taskId}")
                            })
                            DropdownMenuItem(text = {
                                MenuItem(
                                    icon = Icons.Rounded.Delete,
                                    title = R.string.delete,
                                    iconColor = MaterialTheme.colorScheme.primary,
                                    titleColor = MaterialTheme.colorScheme.primary
                                )
                            }, onClick = {
                                viewModel.onEvent(TaskEvent.HideTaskMenuEvent)
                                viewModel.onEvent(TaskEvent.ShowQuestionDialogEvent)
                            })
                        }
                    }
                })
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(bottom = 64.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TimeRow(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    timeType = Constants.timeTypes.find { timeType ->
                        state.task.timeTypeId == timeType.id
                    }!!,
                    date = state.task.date,
                    hour = state.task.hour,
                    weekDate = state.weekDate,
                    minute = state.task.minute,
                    onDateClick = {

                    },
                    onTimeClick = {

                    },
                    onEndTimeClick = {},
                    withEndTime = state.task.withEndTime,
                    endHour = state.task.endHour,
                    endMinute = state.task.endMinute
                )

                if (state.task.title.isNotEmpty()) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp)
                            .offset(x = -16.dp)
                            .animateContentSize(), value = state.task.title, onValueChange = {
//                        viewModel.onEvent(CreateTaskEvent.HeadingChangeEvent(it))
                        }, colors = TextFieldDefaults.colors(
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                            focusedIndicatorColor = MaterialTheme.colorScheme.background
                        ), textStyle = TextStyle(
                            fontFamily = caros, fontWeight = FontWeight.Bold, fontSize = 20.sp
                        ), placeholder = {
                            Text(
                                text = stringResource(R.string.heading),
                                fontFamily = caros,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp
                            )
                        }, readOnly = true
                    )
                }
                if (state.task.content.isNotEmpty()) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp)
                            .offset(
                                x = -16.dp, y = if (state.task.title.isNotEmpty()) -16.dp else 0.dp
                            )
                            .animateContentSize(), value = state.task.content, onValueChange = {
//                        viewModel.onEvent(CreateTaskEvent.ContentChangeEvent(it))
                        }, colors = TextFieldDefaults.colors(
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                            focusedIndicatorColor = MaterialTheme.colorScheme.background
                        ), textStyle = TextStyle(
                            fontFamily = caros,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Justify
                        ), placeholder = {
                            Text(
                                text = stringResource(R.string.content),
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }, readOnly = true
                    )
                }
            }
        }

        if (state.showQuestionDialog) {
            QuestionDialog(title = R.string.deletion,
                text = R.string.really_want_to_delete,
                onDismissRequest = {
                    viewModel.onEvent(TaskEvent.HideQuestionDialogEvent)
                },
                onConfirmRequest = {
                    viewModel.onEvent(TaskEvent.DeleteTaskEvent)
                })
        }

        LaunchedEffect(state.success) {
            if (state.success) {
                navHostController.popBackStack()
            }
        }
    }
}