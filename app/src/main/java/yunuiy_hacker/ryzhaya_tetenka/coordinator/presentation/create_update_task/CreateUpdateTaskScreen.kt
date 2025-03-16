package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_update_task

import android.Manifest
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.home.model.TimeTypeEnum
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.AddPlaceButton
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.CreateUpdatePlaceDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.CreateUpdateSubtaskRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.MessageDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.PeopleRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.PermissionInfoDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.PlaceRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.TimePickerDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.TimeRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.DateFormats
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.ImageUtils
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.displayName
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.timeFormatter
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.toTimeTypeEvent
import java.util.Locale

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class
)
@Composable
fun CreateUpdateTaskScreen(
    navHostController: NavHostController, viewModel: CreateUpdateTaskViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val bottomButtonInteractionSource = remember { MutableInteractionSource() }
    val localIndication = LocalIndication.current
    val currentLocale by remember { mutableStateOf(Locale.getDefault()) }

    val datePickerState = rememberDatePickerState()
    val isEditMode = viewModel.state.taskId != 0

    var selectedPeoplesListExpanded by remember { mutableStateOf(false) }

    var topHeightPaddingOfHeading by remember { mutableStateOf(0) }
    var heightOfHeadingTextField by remember { mutableStateOf(0) }
    var cursorPosition by remember { mutableStateOf(Offset.Zero) }

    val paddingValues = WindowInsets.navigationBars.asPaddingValues()

    val notificationPermissionState =
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

    LaunchedEffect(Unit) {
        viewModel.onEvent(CreateUpdateTaskEvent.LoadDataEvent)
    }

    viewModel.state.let { state ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(topBar = {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    title = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(x = if (state.timeType.toTimeTypeEvent() == TimeTypeEnum.DAY) 0.dp else -24.dp),
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
                        if (state.timeType.toTimeTypeEvent() == TimeTypeEnum.DAY) {
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
                                            Checkbox(
                                                checked = state.endTimeChecked,
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
            }) { it ->
                val rememberScrollState = rememberScrollState()
                val coroutineScope = rememberCoroutineScope()

                if (!state.contentState.isLoading.value) {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .padding(bottom = 64.dp)
                            .verticalScroll(rememberScrollState)
                            .imePadding()
                    ) {
                        Column(modifier = Modifier) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .onGloballyPositioned {
                                        topHeightPaddingOfHeading = it.size.height
                                    }, verticalAlignment = Alignment.CenterVertically
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
                                            key(timeType.id) {
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
                                            key(category.id) {
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
                                        CreateUpdateTaskEvent.ShowDatePickerDialogEvent()
                                    )
                                },
                                onTimeClick = {
                                    viewModel.onEvent(CreateUpdateTaskEvent.ShowTimePickerDialogEvent())
                                },
                                onEndTimeClick = {
                                    viewModel.onEvent(CreateUpdateTaskEvent.ShowEndTimePickerDialogEvent)
                                },
                                withEndTime = state.endTimeChecked,
                                endHour = state.selectedEndHour,
                                endMinute = state.selectedEndMinute
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.priority),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = caros
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(modifier = Modifier
                                    .clickable(
                                        interactionSource = interactionSource, indication = null
                                    ) {
                                        viewModel.onEvent(CreateUpdateTaskEvent.ShowPrioritySelectorMenuEvent)
                                    }
                                    .animateContentSize(),
                                    text = stringResource(state.priorities.find { priority ->
                                        priority.code == state.selectedPriority.collectAsState().value.code
                                    }?.resId ?: R.string.not_selected),
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
                                        expanded = state.showPrioritySelectorMenu,
                                        onDismissRequest = {
                                            viewModel.onEvent(CreateUpdateTaskEvent.HidePrioritySelectorMenuEvent)
                                        },
                                        offset = DpOffset(x = 90.dp, y = 0.dp)
                                    ) {
                                        state.priorities.forEach { priority ->
                                            key(priority.code) {
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
                                                        text = stringResource(priority.resId),
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        fontFamily = caros,
                                                        fontWeight = FontWeight.Normal
                                                    )
                                                }, onClick = {
                                                    viewModel.onEvent(
                                                        CreateUpdateTaskEvent.SelectPriorityMenuEvent(
                                                            priority
                                                        )
                                                    )
                                                })
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        BasicTextField(modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp)
                            .offset(x = -16.dp)
                            .onGloballyPositioned {
                                heightOfHeadingTextField = it.size.height
                            }
                            .animateContentSize(),
                            value = state.heading,
                            onValueChange = {
                                viewModel.onEvent(
                                    CreateUpdateTaskEvent.HeadingChangeEvent(
                                        it
                                    )
                                )
                                coroutineScope.launch {
                                    rememberScrollState.animateScrollTo((topHeightPaddingOfHeading + cursorPosition.y).toInt())
                                }
                            },
                            onTextLayout = { textLayoutResult ->
                                val cursorRect =
                                    textLayoutResult.getCursorRect(state.heading.length)
                                cursorPosition = Offset(cursorRect.topLeft.x, cursorRect.topLeft.y)
                            },
                            decorationBox = {
                                TextFieldDefaults.OutlinedTextFieldDecorationBox(value = state.heading,
                                    isError = false,
                                    innerTextField = it,
                                    visualTransformation = VisualTransformation.None,
                                    colors = TextFieldDefaults.colors(
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        focusedContainerColor = MaterialTheme.colorScheme.background,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                        unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                                        focusedIndicatorColor = MaterialTheme.colorScheme.background,
                                        cursorColor = MaterialTheme.colorScheme.primary
                                    ),
                                    enabled = true,
                                    singleLine = false,
                                    interactionSource = interactionSource,
                                    placeholder = {
                                        Text(
                                            text = stringResource(R.string.heading),
                                            fontFamily = caros,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 20.sp,
                                            color = Color.DarkGray
                                        )
                                    })
                            },
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            textStyle = TextStyle(
                                fontFamily = caros,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        BasicTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp)
                                .offset(x = -16.dp, y = -16.dp)
                                .animateContentSize(),
                            value = state.content,
                            onValueChange = {
                                viewModel.onEvent(
                                    CreateUpdateTaskEvent.ContentChangeEvent(
                                        it
                                    )
                                )
                                coroutineScope.launch {
                                    rememberScrollState.animateScrollTo((topHeightPaddingOfHeading + heightOfHeadingTextField + cursorPosition.y).toInt())
                                }
                            },
                            onTextLayout = { textLayoutResult ->
                                val cursorRect =
                                    textLayoutResult.getCursorRect(state.content.length)
                                cursorPosition = Offset(cursorRect.topLeft.x, cursorRect.topLeft.y)
                            },
                            decorationBox = {
                                TextFieldDefaults.OutlinedTextFieldDecorationBox(value = state.content,
                                    isError = false,
                                    innerTextField = it,
                                    visualTransformation = VisualTransformation.None,
                                    colors = TextFieldDefaults.colors(
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        focusedContainerColor = MaterialTheme.colorScheme.background,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                        unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                                        focusedIndicatorColor = MaterialTheme.colorScheme.background,
                                        cursorColor = MaterialTheme.colorScheme.primary
                                    ),
                                    enabled = true,
                                    singleLine = false,
                                    interactionSource = interactionSource,
                                    placeholder = {
                                        Text(
                                            text = stringResource(R.string.content),
                                            fontFamily = caros,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 14.sp,
                                            color = Color.DarkGray
                                        )
                                    })
                            },
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            textStyle = TextStyle(
                                fontFamily = caros,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Column(
                            modifier = Modifier
                                .offset(y = -16.dp)
                                .animateContentSize()
                        ) {
                            if (!state.contentState.isLoading.value) {
                                state.subtasks.forEach { subtask ->
                                    key(subtask.id) {
                                        CreateUpdateSubtaskRow(modifier = Modifier.padding(
                                            horizontal = 24.dp
                                        ), subtask = subtask, onCheckedChange = {
                                            subtask.checked.value = !subtask.checked.value
                                        }, onTitleChange = {
                                            subtask.title = it
                                        }, onDeleteClick = {
                                            viewModel.onEvent(
                                                CreateUpdateTaskEvent.DeleteSubtaskEvent(
                                                    it
                                                )
                                            )
                                        })
                                    }
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
                            coroutineScope.launch(Dispatchers.Main) {
                                rememberScrollState.animateScrollTo(
                                    rememberScrollState.maxValue
                                )
                            }
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
                                },
                                showDeletionButton = true,
                                onLongClick = {},
                                onDeleteClick = {
                                    state.selectedPlace = Place(id = 0)
                                })
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        AnimatedVisibility(state.selectedPeoples.isEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        viewModel.onEvent(CreateUpdateTaskEvent.ShowPeopleSelectorSheetEvent)
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
                                        imageVector = Icons.Default.People,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = stringResource(R.string.peoples_not_selected),
                                        fontFamily = caros,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        AnimatedVisibility(state.selectedPeoples.isNotEmpty() && !state.contentState.isLoading.value) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                Row(modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        viewModel.onEvent(CreateUpdateTaskEvent.ShowPeopleSelectorSheetEvent)
                                    }
                                    .fillMaxWidth()
                                    .animateContentSize(),
                                    verticalAlignment = Alignment.CenterVertically) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .animateContentSize()
                                            .padding(vertical = 6.dp, horizontal = 6.dp)
                                            .clip(RoundedCornerShape(10.dp)),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box {
                                            state.selectedPeoples.forEachIndexed { index, people ->
                                                key(people.id) {
                                                    if (index <= 4) {
                                                        Row(modifier = Modifier.padding(start = index * 30.dp)) {
                                                            if (people.avatarPath.isNotEmpty()) {
                                                                AsyncImage(
                                                                    modifier = Modifier
                                                                        .size(60.dp)
                                                                        .clip(CircleShape),
                                                                    model = ImageRequest.Builder(
                                                                        context
                                                                    )
                                                                        .data("${ImageUtils.IMG_DIR}/${people.avatarPath}")
                                                                        .build(),
                                                                    contentDescription = null,
                                                                    contentScale = ContentScale.Crop
                                                                )
                                                            } else {
                                                                Icon(
                                                                    modifier = Modifier
                                                                        .size(60.dp)
                                                                        .clip(CircleShape)
                                                                        .background(
                                                                            color = MaterialTheme.colorScheme.primary,
                                                                            shape = CircleShape
                                                                        )
                                                                        .padding(12.dp),
                                                                    imageVector = Icons.Rounded.Person,
                                                                    contentDescription = null
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            modifier = Modifier.weight(1f),
                                            text = if (state.selectedPeoples.size > 0) displayName(
                                                state.selectedPeoples[0].displayName,
                                                state.selectedPeoples[0].surname,
                                                state.selectedPeoples[0].name,
                                                state.selectedPeoples[0].lastname
                                            ) + if (state.selectedPeoples.size == 1) "" else ", ..." else "",
                                            fontFamily = caros,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .clickable {
                                                    selectedPeoplesListExpanded =
                                                        !selectedPeoplesListExpanded
                                                },
                                            imageVector = if (selectedPeoplesListExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                AnimatedVisibility(selectedPeoplesListExpanded && state.selectedPeoples.isNotEmpty()) {
                                    Column(modifier = Modifier.animateContentSize()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        state.selectedPeoples.forEach { people ->
                                            key(people.id) {
                                                PeopleRow(people = people,
                                                    onClick = {},
                                                    onLongClick = {},
                                                    showDeletionButton = true,
                                                    onDeleteClick = {
                                                        state.contentState.isLoading.value = true
                                                        state.selectedPeoples.remove(people)
                                                        state.contentState.isLoading.value = false
                                                    })
                                                Spacer(modifier = Modifier.height(4.dp))
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .clickable(
                                    interactionSource = interactionSource, indication = null
                                ) {
                                    viewModel.onEvent(CreateUpdateTaskEvent.ToggleRemindLaterEvent)

                                    if (state.remindLaterIsChecked) {
                                        if (!notificationPermissionState.status.isGranted) viewModel.onEvent(
                                            CreateUpdateTaskEvent.ShowNotificationPermissionInfoDialogEvent
                                        )
                                    }
                                }, verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = state.remindLaterIsChecked, onCheckedChange = {
                                    viewModel.onEvent(CreateUpdateTaskEvent.ToggleRemindLaterEvent)

                                    if (state.remindLaterIsChecked) {
                                        if (!notificationPermissionState.status.isGranted) viewModel.onEvent(
                                            CreateUpdateTaskEvent.ShowNotificationPermissionInfoDialogEvent
                                        )
                                    }
                                }, colors = CheckboxDefaults.colors(
                                    uncheckedColor = MaterialTheme.colorScheme.onSurface,
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    checkmarkColor = MaterialTheme.colorScheme.background
                                )
                            )
                            Text(
                                text = stringResource(R.string.remind_later),
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        AnimatedVisibility(state.remindLaterIsChecked) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            ) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = Icons.Rounded.Notifications,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Row(modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            viewModel.onEvent(
                                                CreateUpdateTaskEvent.ShowDatePickerDialogEvent(
                                                    true
                                                )
                                            )
                                        }) {
                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp, vertical = 8.dp
                                            ), verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = DateFormats.toDayTimeTypeOutputFormat(
                                                    currentLocale
                                                ).format(
                                                    state.notifyDate
                                                ).lowercase(),
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = caros,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Row(modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            viewModel.onEvent(
                                                CreateUpdateTaskEvent.ShowTimePickerDialogEvent(
                                                    true
                                                )
                                            )
                                        }) {
                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp, vertical = 8.dp
                                            ), verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = timeFormatter(
                                                    state.notifyHour, state.notifyMinute
                                                ),
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = caros,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(64.dp))
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
                    .padding(paddingValues)
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
                                interactionSource = bottomButtonInteractionSource,
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
                            color = Color.White
                        )
                    }
                }
                if (paddingValues.calculateBottomPadding() == 0.dp) Spacer(
                    modifier = Modifier.height(
                        24.dp
                    )
                )
                else Spacer(modifier = Modifier.height(8.dp))
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
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                        ) {
                            Text(
                                text = stringResource(R.string.select),
                                color = Color.White,
                                fontFamily = caros,
                                fontWeight = FontWeight.Medium
                            )
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
                    hour = if (!state.selectNotifyDateOrTime) if (state.showTimePickerDialog) state.selectedHour else state.selectedEndHour else state.notifyHour,
                    minute = if (!state.selectNotifyDateOrTime) if (state.showTimePickerDialog) state.selectedMinute else state.selectedEndMinute else state.notifyMinute
                )
            }

            if (state.showMessageDialog) {
                MessageDialog(message = state.contentState.exception.value?.message ?: "",
                    onDismissRequest = {
                        viewModel.onEvent(CreateUpdateTaskEvent.HideMessageDialogEvent)
                    })
            }

            if (state.showPlacesSelectorSheet) {
                ModalBottomSheet(
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    onDismissRequest = {
                        viewModel.onEvent(CreateUpdateTaskEvent.HidePlaceSelectorSheetEvent)
                    },
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column(modifier = Modifier.animateContentSize()) {
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
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .offset(x = -16.dp),
                                text = stringResource(R.string.add_new_place),
                                fontFamily = caros,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        state.places.forEach { place ->
                            key(place.id) {
                                PlaceRow(modifier = Modifier
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
                                    onLongClick = {})
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            if (state.showPeoplesSelectorSheet) {
                ModalBottomSheet(
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    onDismissRequest = {
                        viewModel.onEvent(CreateUpdateTaskEvent.HidePeopleSelectorSheetEvent)
                    },
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    LazyColumn(
                        modifier = Modifier.animateContentSize()
                    ) {
                        item {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.peoples),
                                textAlign = TextAlign.Center,
                                fontFamily = caros,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                        if (state.peoples.isNotEmpty()) {
                            items(state.peoples, key = { people -> people.id }) { people ->
                                PeopleRow(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                    people = people,
                                    onClick = {
                                        viewModel.onEvent(
                                            CreateUpdateTaskEvent.AddPeopleEvent(
                                                people
                                            )
                                        )
                                    },
                                    onLongClick = {},
                                    showDeletionButton = false,
                                    onDeleteClick = {})
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        } else {
                            item {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        text = stringResource(R.string.peoples_list_is_empty),
                                        fontFamily = caros,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
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

            if (state.showNotificationPermissionInfoDialog) {
                PermissionInfoDialog(
                    onDismissRequest = {
                        viewModel.onEvent(CreateUpdateTaskEvent.HideNotificationPermissionInfoDialogEvent)
                        state.remindLaterIsChecked = false
                    },
                    onContinueClick = {
                        viewModel.onEvent(CreateUpdateTaskEvent.HideNotificationPermissionInfoDialogEvent)
                        notificationPermissionState.launchPermissionRequest()
                    },
                    icon = Icons.Rounded.NotificationsActive,
                    text = stringResource(R.string.need_notification_permission)
                )
            }
        }
    }

    BackHandler {
        viewModel.onEvent(CreateUpdateTaskEvent.OnBackPressEvent)
        navHostController.popBackStack(
            Route.HomeScreen.route, inclusive = false, saveState = false
        )
    }
}