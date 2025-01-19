package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_update_task

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.AddPlaceButton
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.CreateUpdatePlaceDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.MessageDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.PlaceRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.SubtaskRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.TimePickerDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.TimeRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toTimeTypeEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUpdateTaskScreen(
    navHostController: NavHostController, viewModel: CreateUpdateTaskViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val localIndication = LocalIndication.current
    val datePickerState = rememberDatePickerState()
    val isEditMode = viewModel.state.taskId != 0

    LaunchedEffect(Unit) {
        viewModel.onEvent(CreateUpdateTaskEvent.LoadDataEvent)
    }

    viewModel.state.let { state ->
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(topBar = {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = if (isEditMode) stringResource(R.string.edit_task) else stringResource(
                                R.string.create_task,
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
                                viewModel.onEvent(CreateUpdateTaskEvent.OnBackPressEvent)
                                navHostController.popBackStack(
                                    Route.HomeScreen.route, inclusive = false, saveState = false
                                )
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
                                    viewModel.onEvent(CreateUpdateTaskEvent.ShowTaskMenuEvent)
                                }) {
                                Icon(
                                    imageVector = Icons.Rounded.MoreVert,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.width(24.dp))
                        }

                        if (state.timeType.toTimeTypeEvent() == TimeTypeEnum.DAY) {
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
                                        viewModel.onEvent(CreateUpdateTaskEvent.HideTaskMenuEvent)
                                    }, offset = DpOffset(x = -12.dp, y = 0.dp)
                                ) {
                                    DropdownMenuItem(text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(checked = state.endTimeChecked,
                                                onCheckedChange = {
                                                    viewModel.onEvent(CreateUpdateTaskEvent.EndTimeCheckToggleEvent)
                                                })
                                            Text(
                                                text = stringResource(R.string.end_time),
                                                fontFamily = caros,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }, onClick = {
                                        viewModel.onEvent(CreateUpdateTaskEvent.HideTaskMenuEvent)
                                        viewModel.onEvent(CreateUpdateTaskEvent.EndTimeCheckToggleEvent)
                                    })
                                }
                            }
                        }
                    })
            }) {
                if (!state.contentState.isLoading.value) {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .padding(bottom = 64.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.type_of_time),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = caros
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource, indication = null
                                ) {
                                    viewModel.onEvent(CreateUpdateTaskEvent.ShowTimeTypePickerMenuEvent)
                                }
                                .animateContentSize(),
                                text = stringResource(Constants.timeTypes.find { it.id == state.timeType.id }!!.resId).lowercase(),
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = caros,
                                fontWeight = FontWeight.Medium
                            )
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
                                        ),
                                    expanded = state.showTimeTypePickerMenu,
                                    onDismissRequest = {
                                        viewModel.onEvent(CreateUpdateTaskEvent.HideTimeTypePickerMenuEvent)
                                    },
                                    offset = DpOffset(x = 90.dp, y = 0.dp)
                                ) {
                                    Constants.timeTypes.forEach { timeType ->
                                        DropdownMenuItem(modifier = Modifier
                                            .clip(
                                                RoundedCornerShape(
                                                    10.dp
                                                )
                                            )
                                            .border(
                                                width = 0.dp,
                                                color = Color.Unspecified,
                                                shape = RoundedCornerShape(10.dp)
                                            ), text = {
                                            Text(
                                                text = stringResource(timeType.resId).lowercase(),
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = caros,
                                                fontWeight = FontWeight.Normal
                                            )
                                        }, onClick = {
                                            viewModel.onEvent(
                                                CreateUpdateTaskEvent.SelectTimeTypePickerMenuEvent(
                                                    timeType
                                                )
                                            )
                                        })
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.category),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = caros
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource, indication = null
                                ) {
                                    viewModel.onEvent(CreateUpdateTaskEvent.ShowCategorySelectorMenuEvent)
                                }
                                .animateContentSize(),
                                text = state.categories.find { category ->
                                    category.id == state.selectedCategory.id
                                }?.title!!,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = caros,
                                fontWeight = FontWeight.Medium
                            )
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
                                        ),
                                    expanded = state.showCategorySelectorMenu,
                                    onDismissRequest = {
                                        viewModel.onEvent(CreateUpdateTaskEvent.HideCategorySelectorMenuEvent)
                                    },
                                    offset = DpOffset(x = 90.dp, y = 0.dp)
                                ) {
                                    state.categories.forEach { category ->
                                        DropdownMenuItem(modifier = Modifier
                                            .clip(
                                                RoundedCornerShape(
                                                    10.dp
                                                )
                                            )
                                            .border(
                                                width = 0.dp,
                                                color = Color.Unspecified,
                                                shape = RoundedCornerShape(10.dp)
                                            ), text = {
                                            Text(
                                                text = category.title,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = caros,
                                                fontWeight = FontWeight.Normal
                                            )
                                        }, onClick = {
                                            viewModel.onEvent(
                                                CreateUpdateTaskEvent.SelectCategoryMenuEvent(
                                                    category
                                                )
                                            )
                                        })
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        TimeRow(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth(),
                            timeType = state.timeType,
                            date = state.date,
                            hour = state.selectedHour,
                            weekDate = state.weekDate,
                            minute = state.selectedMinute,
                            onDateClick = {
                                if (state.timeType.toTimeTypeEvent() != TimeTypeEnum.LIFE) viewModel.onEvent(
                                    CreateUpdateTaskEvent.ShowDatePickerDialogEvent
                                )
                            },
                            onTimeClick = {
                                viewModel.onEvent(CreateUpdateTaskEvent.ShowTimePickerDialogEvent)
                            },
                            onEndTimeClick = {
                                viewModel.onEvent(CreateUpdateTaskEvent.ShowEndTimePickerDialogEvent)
                            },
                            withEndTime = state.endTimeChecked,
                            endHour = state.selectedEndHour,
                            endMinute = state.selectedEndMinute
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp)
                            .offset(x = -16.dp)
                            .animateContentSize(),
                            value = state.heading,
                            onValueChange = {
                                viewModel.onEvent(CreateUpdateTaskEvent.HeadingChangeEvent(it))
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                                focusedIndicatorColor = MaterialTheme.colorScheme.background
                            ),
                            textStyle = TextStyle(
                                fontFamily = caros, fontWeight = FontWeight.Bold, fontSize = 20.sp
                            ),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.heading),
                                    fontFamily = caros,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 20.sp,
                                    color = Color.DarkGray
                                )
                            })
                        TextField(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp)
                            .offset(x = -16.dp, y = -16.dp)
                            .animateContentSize(),
                            value = state.content,
                            onValueChange = {
                                viewModel.onEvent(CreateUpdateTaskEvent.ContentChangeEvent(it))
                            },
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
                                textAlign = TextAlign.Start,
                            ),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.content),
                                    fontFamily = caros,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            })
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .offset(y = -16.dp)
                                .animateContentSize()
                        ) {
                            if (!state.contentState.isLoading.value) {
                                state.subtasks.forEach { subtask ->
                                    SubtaskRow(
                                        subtask = subtask,
                                        onCheckedChange = {
                                            subtask.checked.value = !subtask.checked.value
                                        },
                                        onTitleChange = {
                                            subtask.title = it
                                        },
                                        onDeleteClick = {
                                            viewModel.onEvent(
                                                CreateUpdateTaskEvent.DeleteSubtaskEvent(
                                                    it
                                                )
                                            )
                                        })
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        AddPlaceButton(
                            modifier = Modifier
                                .offset(x = 0.dp, y = -16.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            viewModel.onEvent(CreateUpdateTaskEvent.AddSubtaskEvent)
                        }
                        AnimatedVisibility(state.selectedPlace.id == 0) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        viewModel.onEvent(CreateUpdateTaskEvent.ShowPlaceSelectorSheetEvent)
                                    }, verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clip(RoundedCornerShape(10.dp))
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = stringResource(R.string.location_not_selected),
                                        fontFamily = caros,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        AnimatedVisibility(state.selectedPlace.id != 0) {
                            PlaceRow(modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                                place = state.selectedPlace,
                                onClick = {
                                    viewModel.onEvent(
                                        CreateUpdateTaskEvent.ShowPlaceSelectorSheetEvent
                                    )
                                })
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            ) {

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = if (state.heading.isNotEmpty() || state.content.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = if (state.heading.isEmpty() || state.content.isEmpty()) null else localIndication
                            ) {
                                if (state.heading.isNotEmpty() || state.content.isNotEmpty()) viewModel.onEvent(
                                    CreateUpdateTaskEvent.OnClickButtonEvent
                                )
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = if (isEditMode) stringResource(R.string.save_changes) else stringResource(
                                R.string.create
                            ),
                            fontFamily = caros,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            LaunchedEffect(state.success) {
                if (state.success) {
                    if (isEditMode) navHostController.popBackStack(
                        Route.HomeScreen.route, inclusive = false, saveState = false
                    )
                    else navHostController.popBackStack()
                }
            }

            if (state.showDatePickerDialog) {
                yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.DatePickerDialog(
                    modifier = Modifier.heightIn(max = 700.dp),
                    onDismissRequest = {
                        viewModel.onEvent(CreateUpdateTaskEvent.HideDatePickerDialogEvent)
                    },
                    confirmButton = {},
                    colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column {
                        DatePicker(state = datePickerState)
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            onClick = {
                                viewModel.onEvent(
                                    CreateUpdateTaskEvent.SelectDatePickerDialogEvent(
                                        datePickerState.selectedDateMillis ?: 0
                                    )
                                )
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.select),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = caros,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        if (state.showTimePickerDialog || state.showEndTimePickerDialog) {
            TimePickerDialog(
                onDismissRequest = {
                    viewModel.onEvent(CreateUpdateTaskEvent.HideTimePickerDialogEvent)
                },
                onSelectButtonClick = { hour, minute ->
                    if (state.showTimePickerDialog) viewModel.onEvent(
                        CreateUpdateTaskEvent.SelectTimePickerDialogEvent(
                            hour = hour, minute = minute
                        )
                    )
                    else if (state.showEndTimePickerDialog) viewModel.onEvent(
                        CreateUpdateTaskEvent.SelectEndTimePickerDialogEvent(
                            hour = hour, minute = minute
                        )
                    )
                },
                hour = if (state.showTimePickerDialog) state.selectedHour else state.selectedEndHour,
                minute = if (state.showTimePickerDialog) state.selectedMinute else state.selectedEndMinute
            )
        }

        if (state.showMessageDialog) {
            MessageDialog(message = state.contentState.exception.value?.message ?: "",
                onDismissRequest = {
                    viewModel.onEvent(CreateUpdateTaskEvent.HideMessageDialogEvent)
                })
        }

        if (state.showPlacesSelectorSheet) {
            ModalBottomSheet(onDismissRequest = {
                viewModel.onEvent(CreateUpdateTaskEvent.HidePlaceSelectorSheetEvent)
            }, containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                Column {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.place),
                        textAlign = TextAlign.Center,
                        fontFamily = caros,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        onClick = {
                            viewModel.onEvent(CreateUpdateTaskEvent.ShowPlaceCreateUpdateDialogEvent)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddLocationAlt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .offset(x = -16.dp),
                            text = stringResource(R.string.add_new_place),
                            fontFamily = caros,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    AnimatedVisibility(!state.contentState.isLoading.value) {
                        if (!state.contentState.isLoading.value) {
                            Column {
                                state.places.forEach { place ->
                                    PlaceRow(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp),
                                        place = place,
                                        onClick = {
                                            viewModel.onEvent(
                                                CreateUpdateTaskEvent.SelectPlaceEvent(
                                                    place
                                                )
                                            )
                                        },
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.showCreateUpdatePlaceDialog) {
            CreateUpdatePlaceDialog(onDismissRequest = {
                viewModel.onEvent(CreateUpdateTaskEvent.HidePlaceCreateUpdateDialogEvent)
            }, onAddClick = {
                viewModel.onEvent(CreateUpdateTaskEvent.CreatePlaceEvent(it))
            }, isEditMode = false)
        }
    }

    BackHandler {
        viewModel.onEvent(CreateUpdateTaskEvent.OnBackPressEvent)
        navHostController.popBackStack(
            Route.HomeScreen.route, inclusive = false, saveState = false
        )
    }
}